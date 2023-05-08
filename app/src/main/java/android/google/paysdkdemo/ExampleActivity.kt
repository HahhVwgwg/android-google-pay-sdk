package android.google.paysdkdemo

import android.google.paysdk.data.model.AuthTokenRequest
import android.google.paysdk.data.model.PaymentResult
import android.google.paysdk.data.model.request.*
import android.google.paysdk.domain.Environment
import android.google.paysdk.payment.DNAPayment
import android.google.paysdk.payment.DNAPaymentsActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.wallet.PaymentsClient

/**
 * Main activity
 *
 * This main activity allows the user to fill in payment data (amount, order id, etc.).
 *
 * Before retrieving this payment data, the following steps must be completed:

 * Initialize the payment context with the `DNAPayment.init()` method.
 * Check if payment is possible with the `DNAPayment.isPaymentPossible()` method.

 * After retrieving this payment data, the following steps must be completed:

 * The `DNAPayment.execute()` method is executed.
 * The payment result is handled by the `handlePaymentResult()` method.

 * For readability purposes in this example, we do not use logs.
 *
 * @author DNA Network
 */
class ExampleActivity : DNAPaymentsActivity() {

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

        DNAPayment.isPaymentPossible(paymentsClient).addOnCompleteListener { task ->
            try {
                val result = task.getResult(ApiException::class.java)
                if (result) {
                    // show Google Pay as a payment option
                    payBtn.visibility = View.VISIBLE
                } else {
                    Toast.makeText(this, "isPaymentPossible return false", Toast.LENGTH_LONG).show()
                }
            } catch (e: ApiException) {
                Toast.makeText(this, "isPaymentPossible exception catched", Toast.LENGTH_LONG)
                    .show()
            }

        }
    }

    /**
     * onPayClick method
     * Payment execution
     *
     * @param view View Pay button
     */
    fun onPayClick(view: View) {
        progressBar.visibility = View.VISIBLE
        DNAPayment.execute(
            this,
            Environment.TEST,
            paymentsClient,
            paymentRequest = PaymentRequest(
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
                amount = 24
            ), authTokenRequest = AuthTokenRequest(
                grantType = "client_credentials",
                scope = "payment integration_hosted integration_embedded integration_seamless",
                clientId = "Test Merchant",
                clientSecret = "PoF84JqIG8Smv5VpES9bcU31kmfSqLk8Jdo7",
                invoiceId = "1683194969490",
                terminal = "8911a14f-61a3-4449-a1c1-7a314ee5774c",
                amount = 24,
                currency = "GBP",
                paymentFormURL = "https://test-pay.dnapayments.com/checkout/"
            )
        )
    }

    /**
     * Handle payment result
     *
     * @param result PaymentResult
     */
    override fun handlePaymentResult(result: PaymentResult) {
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