package android.google.paysdk.payment

import android.content.Context
import android.google.paysdk.data.model.AuthTokenRequest
import android.google.paysdk.data.model.PaymentResult
import android.google.paysdk.data.model.StatusCallback
import android.google.paysdk.data.model.googlePaymentData.GooglePaymentData
import android.google.paysdk.data.model.request.CardDetails
import android.google.paysdk.data.model.request.CustomerDetails
import android.google.paysdk.data.model.request.PaymentRequest
import android.google.paysdk.data.network.getPaymentDataSource
import android.google.paysdk.domain.DataFactory
import android.google.paysdk.domain.Environment
import android.google.paysdk.domain.SupportedNetworks
import android.google.paysdk.utils.catchError
import com.google.android.gms.tasks.Task
import com.google.android.gms.wallet.PaymentsClient


/**
 * Util class that manage the bridge with Google pay
 * execute method calls Google Pay and then on result calls merchant server
 */
object DNAPayment {
    private lateinit var environment: Environment
    lateinit var paymentRequest: PaymentRequest
    lateinit var authTokenRequest: AuthTokenRequest

    /**
     * Initializes Google Pay context.
     *
     * @param activity The activity that is currently in focus.
     * @param environment The environment to use for the payment request.
     * @param supportedNetworks The list of supported networks.
     * @return A PaymentsClient object that can be used to create payment requests.
     */
    fun init(
        context: Context,
        environment: Environment,
        supportedNetworks: List<SupportedNetworks> = SupportedNetworks.values().asList()
    ): PaymentsClient {
        DNAPayment.environment = environment
        return GooglePayManagement.init(context, environment, supportedNetworks)
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
     * @param googlePayData String
     */
    suspend fun executeTransaction(
        googlePayData: GooglePaymentData,
        statusCallback: StatusCallback
    ) {
        val paymentService = getPaymentDataSource(environment.serverUrl)
        try {
            val getAuthToken = paymentService.getAuthToken(
                grantType = authTokenRequest.grantType,
                scope = authTokenRequest.scope,
                clientId = authTokenRequest.clientId,
                clientSecret = authTokenRequest.clientSecret,
                invoiceId = authTokenRequest.invoiceId,
                terminal = authTokenRequest.terminal,
                amount = authTokenRequest.amount,
                currency = authTokenRequest.currency,
                paymentFormURL = authTokenRequest.paymentFormURL
            )

            paymentService.pay(
                token = "Bearer ${getAuthToken.access_token}",
                paymentRequest.copy(
                    auth = getAuthToken,
                    cardDetails = CardDetails(
                        cryptogram = googlePayData.paymentMethodData?.tokenizationData?.token.orEmpty(),
                        cardholderName = googlePayData.shippingAddress?.name.orEmpty()
                    ),
                    customerDetails = CustomerDetails(
                        browserDetails = DataFactory.getBrowserDetails()
                    )
                )
            )
            statusCallback.onResponse(PaymentResult(true, null))
        } catch (e: Exception) {
            val errorPaymentResult = e.catchError()

            statusCallback.onResponse(
                PaymentResult(
                    false,
                    errorPaymentResult.errorCode,
                    errorPaymentResult.errorDescription
                )
            )
        }
    }
}