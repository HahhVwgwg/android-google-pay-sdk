package android.google.paysdk.payment

import android.app.Activity
import android.google.paysdk.data.model.AuthTokenRequest
import android.google.paysdk.data.model.PaymentResult
import android.google.paysdk.data.model.StatusCallback
import android.google.paysdk.data.model.googlePaymentData.GooglePaymentData
import android.google.paysdk.data.model.request.PaymentRequest
import android.google.paysdk.domain.DNAPaymentsErrorCode
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.wallet.AutoResolveHelper
import com.google.android.gms.wallet.PaymentData
import com.google.android.gms.wallet.PaymentsClient
import com.google.gson.Gson
import kotlinx.coroutines.launch

class GooglePayHelper(
    private val fragment: Fragment,
    private val statusCallback: StatusCallback
) : LifecycleObserver {

    private lateinit var paymentResultLauncher: ActivityResultLauncher<IntentSenderRequest>

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun registerForActivityResult() {
        paymentResultLauncher = fragment.registerForActivityResult(
            ActivityResultContracts.StartIntentSenderForResult()
        ) { result ->
            val resultCode = result.resultCode
            val data = result.data

            // Manage Google Pay result
            when (resultCode) {
                Activity.RESULT_OK -> {
                    when {
                        data != null -> {
                            val paymentData = PaymentData.getFromIntent(data)
                            val gson = Gson()
                            // Execute payment
                            fragment.lifecycleScope.launch {
                                DNAPayment.executeTransaction(
                                    gson.fromJson(
                                        paymentData!!.toJson(),
                                        GooglePaymentData::class.java
                                    ), statusCallback
                                )
                            }
                        }
                        else -> {
                            statusCallback.onResponse(
                                PaymentResult(
                                    false,
                                    DNAPaymentsErrorCode.UNKNOWN_ERROR
                                )
                            )
                        }
                    }
                }
                Activity.RESULT_CANCELED -> {
                    statusCallback.onResponse(
                        PaymentResult(
                            false,
                            DNAPaymentsErrorCode.PAYMENT_CANCELLED_ERROR
                        )
                    )
                }
                AutoResolveHelper.RESULT_ERROR -> {
                    val status = AutoResolveHelper.getStatusFromIntent(data)
                    statusCallback.onResponse(
                        PaymentResult(
                            false,
                            DNAPaymentsErrorCode.UNKNOWN_ERROR,
                            status?.statusMessage
                        )
                    )
                }
            }
        }
    }

    /**
     * Executes Google Pay
     *
     * @param paymentsClient The PaymentsClient object to use to create the payment request.
     * @param paymentRequest The payment request to send to Google Pay.
     * @param authTokenRequest The auth token request to send to Google Pay.
     */
    fun execute(
        paymentRequest: PaymentRequest,
        authTokenRequest: AuthTokenRequest,
        paymentsClient: PaymentsClient,
    ) {

        // Set the payment request and auth token request on the DNAPayment object.
        DNAPayment.paymentRequest = paymentRequest
        DNAPayment.authTokenRequest = authTokenRequest


        if (paymentRequest.amount == null) {

            statusCallback.onResponse(
                PaymentResult(
                    false,
                    DNAPaymentsErrorCode.PAYMENT_DATA_ERROR,
                    "Amount is required"
                )
            )
            return
        }

        if (paymentRequest.currency == null) {
            statusCallback.onResponse(
                PaymentResult(
                    false,
                    DNAPaymentsErrorCode.PAYMENT_DATA_ERROR,
                    "Currency is required"
                )
            )
            return
        }

        val paymentDataRequest =
            GooglePayManagement.preparePaymentDataRequest(
                paymentRequest.amount.toString(),
                paymentRequest.currency.toString(),
                DNAPayment.authTokenRequest.terminal
            )

        // Launch the payment process with the paymentDataRequest
        val intent = paymentsClient.loadPaymentData(paymentDataRequest)
        intent.addOnCompleteListener { completedTask ->
            when {

                completedTask.exception is ResolvableApiException -> {
                    paymentResultLauncher.launch(
                        IntentSenderRequest.Builder((completedTask.exception as ResolvableApiException).resolution)
                            .build()
                    )
                }
                completedTask.isSuccessful -> {
                    PaymentResult(
                        false,
                        DNAPaymentsErrorCode.UNKNOWN_ERROR
                    )
                }
                else -> {
                    PaymentResult(
                        false,
                        DNAPaymentsErrorCode.UNKNOWN_ERROR
                    )
                }
            }
        }
    }

    /**
     * Cleanup resources when the context's lifecycle is destroyed.
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        // Unregister from activity result when the context is destroyed
        fragment.lifecycle.removeObserver(this)
    }
}