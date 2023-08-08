package android.google.paysdk.payment

import android.app.Activity
import android.content.Intent
import android.google.paysdk.data.model.AuthTokenRequest
import android.google.paysdk.data.model.PaymentResponse
import android.google.paysdk.data.model.PaymentResult
import android.google.paysdk.data.model.googlePaymentData.GooglePaymentData
import android.google.paysdk.data.model.request.AuthToken
import android.google.paysdk.data.model.request.CardDetails
import android.google.paysdk.data.model.request.PaymentRequest
import android.google.paysdk.data.network.getPaymentDataSource
import android.google.paysdk.domain.DNAPaymentsErrorCode
import android.google.paysdk.domain.Environment
import android.google.paysdk.domain.SupportedNetworks
import com.google.android.gms.tasks.Task
import com.google.android.gms.wallet.PaymentData
import com.google.android.gms.wallet.PaymentsClient
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * Util class that manage the bridge with Google pay
 * execute method calls Google Pay and then on result calls merchant server
 */
object DNAPayment {
    private lateinit var environment: Environment
    private lateinit var paymentRequest: PaymentRequest
    private lateinit var authTokenRequest: AuthTokenRequest

    /**
     * Executes Google Pay.
     *
     * @param activity The activity that is currently in focus.
     * @param environment The environment to use for the payment request.
     * @param paymentsClient The PaymentsClient object to use to create the payment request.
     * @param paymentRequest The payment request to send to Google Pay.
     * @param authTokenRequest The auth token request to send to Google Pay.
     */
    fun execute(
        activity: Activity,
        environment: Environment,
        paymentsClient: PaymentsClient,
        paymentRequest: PaymentRequest,
        authTokenRequest: AuthTokenRequest,
        statusCallback: StatusCallback,
    ) {

        // Set the payment request and auth token request on the DNAPayment object.
        DNAPayment.paymentRequest = paymentRequest
        DNAPayment.authTokenRequest = authTokenRequest

        // Execute the Google Pay payment request.
        GooglePayManagement.execute(
            paymentRequest.amount.toString(),
            paymentRequest.currency.toString(),
            environment,    
            authTokenRequest.terminal,
            activity,
            paymentsClient,
            statusCallback
        )
    }

    /**
     * Initializes Google Pay context.
     *
     * @param activity The activity that is currently in focus.
     * @param environment The environment to use for the payment request.
     * @param supportedNetworks The list of supported networks.
     * @return A PaymentsClient object that can be used to create payment requests.
     */
    fun init(
        activity: Activity,
        environment: Environment,
        supportedNetworks: List<SupportedNetworks> = SupportedNetworks.values().asList()
    ): PaymentsClient {
        DNAPayment.environment = environment
        return GooglePayManagement.init(activity, environment, supportedNetworks)
    }

    /**
     * Determines if Google Pay payment is possible
     *
     * @param paymentsClient PaymentsClient
     * @return Task<Boolean>
     */
    fun isPaymentPossible(paymentsClient: PaymentsClient): Task<Boolean> {
        return GooglePayManagement.isPossible(paymentsClient)
    }

    /**
     * Executes transaction on merchant server
     * @param data String from Google Pay
     */
    fun executeTransaction(
        data: Intent,
        statusCallback: StatusCallback
    ) {
        val paymentData = PaymentData.getFromIntent(data)
        val gson = Gson()
        val googlePayData = gson.fromJson(
            paymentData!!.toJson(),
            GooglePaymentData::class.java
        )
        val paymentService = getPaymentDataSource(environment.serverUrl)

        val getAuthTokenCall = paymentService.getAuthToken(
            grantType = authTokenRequest.grantType,
            scope = authTokenRequest.scope,
            clientId = authTokenRequest.clientId,
            clientSecret = authTokenRequest.clientSecret,
            invoiceId = "1683194969490",
            terminal = "8911a14f-61a3-4449-a1c1-7a314ee5774c",
            amount = 0.8,
            currency = authTokenRequest.currency,
            paymentFormURL = authTokenRequest.paymentFormURL
        )

        getAuthTokenCall.enqueue(object : Callback<AuthToken> {
            override fun onResponse(call: Call<AuthToken>, response: Response<AuthToken>) {
                if (response.isSuccessful) {
                    val getAuthToken = response.body()

                    val payCall = paymentService.pay(
                        token = "Bearer ${getAuthToken?.access_token}",
                        paymentRequest.copy(
                            auth = getAuthToken,
                            cardDetails = CardDetails(
                                cryptogram = googlePayData.paymentMethodData?.tokenizationData?.token.orEmpty(),
                                cardholderName = googlePayData.shippingAddress?.name.orEmpty()
                            ),
                            customerDetails = paymentRequest.customerDetails.copy(
                                browserDetails = DataFactory.getBrowserDetails()
                            )
                        )
                    )

                    payCall.enqueue(object : Callback<PaymentResponse> {
                        override fun onResponse(
                            call: Call<PaymentResponse>,
                            response: Response<PaymentResponse>
                        ) {
                            if (response.isSuccessful) {
                                statusCallback.onResponse(PaymentResult(true, null))
                            } else {
                                val message: android.google.paysdk.data.model.APIError =
                                    Gson().fromJson(
                                        response.errorBody()?.charStream(),
                                        android.google.paysdk.data.model.APIError::class.java
                                    )

                                statusCallback.onResponse(
                                    PaymentResult(
                                        false,
                                        DNAPaymentsErrorCode.SERVER_ERROR,
                                        message.message
                                    )
                                )
                            }
                        }

                        override fun onFailure(call: Call<PaymentResponse>, t: Throwable) {
                            statusCallback.onResponse(
                                PaymentResult(
                                    false,
                                    null,
                                    t.message
                                )
                            )
                        }
                    })
                } else {
                    statusCallback.onResponse(
                        PaymentResult(
                            false,
                            null,
                            "Failed to get auth token"
                        )
                    )
                }
            }

            override fun onFailure(call: Call<AuthToken>, t: Throwable) {
                statusCallback.onResponse(
                    PaymentResult(
                        false,
                        null,
                        t.message
                    )
                )
            }
        })
    }
}