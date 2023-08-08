package android.google.paysdkdemo

import android.app.Activity
import android.content.Intent
import android.google.paysdk.data.model.AuthTokenRequest
import android.google.paysdk.data.model.PaymentResult
import android.google.paysdk.data.model.request.*
import android.google.paysdk.domain.DNAPaymentsErrorCode
import android.google.paysdk.domain.Environment
import android.google.paysdk.payment.DNAPayment
import android.google.paysdk.payment.GooglePayManagement
import android.google.paysdk.payment.StatusCallback
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.wallet.AutoResolveHelper
import com.google.android.gms.wallet.PaymentsClient

/**
 * DNAPaymentsActivity
 */
class DNAPaymentsActivity : AppCompatActivity() {

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
                        // Execute payment
                        DNAPayment.executeTransaction(
                            data,
                            object : StatusCallback {
                                override fun onResponse(paymentResult: PaymentResult) {
                                    handlePaymentResult(paymentResult)
                                }
                            })
                    } else {
                        handlePaymentResult(
                            PaymentResult(
                                false,
                                DNAPaymentsErrorCode.UNKNOWN_ERROR
                            )
                        )
                    }
                }
                Activity.RESULT_CANCELED -> {
                    handlePaymentResult(
                        PaymentResult(
                            false,
                            DNAPaymentsErrorCode.PAYMENT_CANCELLED_ERROR
                        )
                    )
                }
                AutoResolveHelper.RESULT_ERROR -> {
                    val status = AutoResolveHelper.getStatusFromIntent(data)
                    handlePaymentResult(
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

    private lateinit var payBtn: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var paymentsClient: PaymentsClient

    /**
     * onCreate method
     * Activity creation
     *
     * @param savedInstanceState Bundle?
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        payBtn = findViewById(R.id.payBtn)
        progressBar = findViewById(R.id.progressBar)

        // Environment TEST or PRODUCTION
        paymentsClient =
            DNAPayment.init(this, Environment.TEST)
        payBtn.visibility = View.VISIBLE
//        DNAPayment.isPaymentPossible(paymentsClient).addOnCompleteListener { task ->
//            try {
//                val result = task.getResult(ApiException::class.java)
//                if (result) {
//                    println("ema")
//                    // show Google Pay as a payment option
//                    payBtn.visibility = View.VISIBLE
//                } else {
//                    println("hey")
//                    Toast.makeText(this, "isPaymentPossible return false", Toast.LENGTH_LONG).show()
//                }
//            } catch (e: ApiException) {
//                Toast.makeText(this, "isPaymentPossible exception catched", Toast.LENGTH_LONG)
//                    .show()
//            }
//
//        }
    }

    /**
     * onPayClick method
     * Payment execution
     *
     * @param view View Pay button
     */
    fun onPayClick(view: View) {
        val authTokenResult = AuthTokenRequest(
            grantType = "client_credentials",
            scope = "payment integration_hosted integration_embedded integration_seamless",
            clientId = "Test Merchant",
            clientSecret = "PoF84JqIG8Smv5VpES9bcU31kmfSqLk8Jdo7",
            invoiceId = "1683194969490",
            terminal = "8911a14f-61a3-4449-a1c1-7a314ee5774c",
            amount = 24.4,
            currency = "GBP",
            paymentFormURL = "https://test-pay.dnapayments.com/checkout/"
        )
        val paymentResult = PaymentRequest(
            currency = "GBP",
            paymentMethod = "googlepay",
            description = "Car Service",
            paymentSettings = PaymentSettings(
                terminalId = "8911a14f-61a3-4449-a1c1-7a314ee5774c",
                returnUrl = "https://example.com/return",
                failureReturnUrl = "https://example.com/failure",
                callbackUrl = "https://example.com/callback",
                failureCallbackUrl = "https://example.com/failure-callback"
            ),
            customerDetails = CustomerDetails(
                accountDetails = AccountDetails(
                    accountId = "uuid000001"
                ),
                billingAddress = AddressInfo(
                    firstName = "John",
                    lastName = "Doe",
                    addressLine1 = "123 Main Street",
                    postalCode = "12345",
                    city = "Anytown",
                    country = "GB"
                ),
                deliveryDetails = DeliveryDetails(
                    deliveryAddress = AddressInfo(
                        firstName = "Jane",
                        lastName = "Doe",
                        addressLine1 = "456 Elm Street",
                        postalCode = "54321",
                        city = "Anytown",
                        country = "GB"
                    )
                ),
                email = "aaa@dnapayments.com",
                mobilePhone = "+441234567890"
            ),
            orderLines = listOf(
                OrderLine(
                    name = "Running shoe",
                    quantity = 1,
                    unitPrice = 24,
                    taxRate = 20,
                    totalAmount = 24,
                    totalTaxAmount = 4
                )
            ),
            deliveryType = "service",
            invoiceId = "1683194969490",
            amount = 24.4
        )
        progressBar.visibility = View.VISIBLE
        DNAPayment.execute(
            this,
            Environment.TEST,
            paymentsClient,
            paymentRequest = paymentResult,
            authTokenResult,
            object : StatusCallback {
                override fun onResponse(paymentResult: PaymentResult) {
                    handlePaymentResult(paymentResult)
                }
            }
        )
    }

    /**
     * Handle payment result
     *
     * @param result PaymentResult
     */
    private fun handlePaymentResult(result: PaymentResult) {
        progressBar.visibility = View.GONE

        if (result.success) {
            Toast.makeText(this, "Payment successful", Toast.LENGTH_LONG).show()
            return
        }

        Toast.makeText(
            this,
            "Payment failed. errorCode = " + result.errorCode?.name + " and description = " + result.errorDescription,
            Toast.LENGTH_LONG
        ).show()
    }
}