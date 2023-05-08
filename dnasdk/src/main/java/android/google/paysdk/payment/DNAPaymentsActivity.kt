package android.google.paysdk.payment

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.wallet.AutoResolveHelper
import com.google.android.gms.wallet.PaymentData
import com.google.gson.Gson
import kotlinx.coroutines.launch
import android.google.paysdk.data.model.PaymentResult
import android.google.paysdk.data.model.googlePaymentData.GooglePaymentData
import android.google.paysdk.domain.DNAPaymentsErrorCode

/**
 * DNAPaymentsActivity
 */
abstract class DNAPaymentsActivity : AppCompatActivity() {
    /**
     * Handle payment result
     *
     * @param result PaymentResult
     */
    abstract fun handlePaymentResult(result: PaymentResult)

    /**
     * Allow to retrieve to payment status
     *
     * @param requestCode Int
     * @param resultCode Int
     * @param data Intent?
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Manage Google Pay result
        if (requestCode == GooglePayManagement.GOOGLE_PAYMENT_CODE_RESULT) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    if (data != null) {
                        val paymentData = PaymentData.getFromIntent(data)
                        val gson = Gson()
                        // Execute payment
                        lifecycleScope.launch {
                            DNAPayment.executeTransaction(
                                gson.fromJson(
                                    paymentData!!.toJson(),
                                    GooglePaymentData::class.java
                                ), this@DNAPaymentsActivity
                            )
                        }
                    } else {
                        DNAPayment.returnsResult(
                            this,
                            false,
                            DNAPaymentsErrorCode.UNKNOWN_ERROR
                        )
                    }
                }
                Activity.RESULT_CANCELED -> {
                    DNAPayment.returnsResult(
                        this,
                        false,
                        DNAPaymentsErrorCode.PAYMENT_CANCELLED_ERROR
                    )
                }
                AutoResolveHelper.RESULT_ERROR -> {
                    val status = AutoResolveHelper.getStatusFromIntent(data)
                    DNAPayment.returnsResult(
                        this,
                        false,
                        DNAPaymentsErrorCode.UNKNOWN_ERROR,
                        status?.statusMessage
                    )
                }
            }
        }
    }
}