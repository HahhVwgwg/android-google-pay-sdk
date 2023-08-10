package android.google.paysdk.payment

import android.content.Context
import android.google.paysdk.data.model.request.*
import android.google.paysdk.domain.Environment
import android.google.paysdk.domain.SupportedNetworks
import com.google.android.gms.tasks.Task
import com.google.android.gms.wallet.*
import org.json.JSONArray
import org.json.JSONObject

/**
 * Google Pay Management
 */
object GooglePayManagement {

    private val SUPPORTED_METHODS = listOf(
        "CRYPTOGRAM_3DS"
    )

    // The name of your payment processor / gateway. Please refer to their documentation for
    // more information.
    private const val GATEWAY_TOKENIZATION_NAME = "dnapayments"

    private lateinit var supportedNetworks: List<String>


    /**
     * Get allowed card networks
     */
    private fun getAllowedCardNetworks(): JSONArray {
        val allowedCardNetworks = JSONArray()

        for (network in supportedNetworks) {
            allowedCardNetworks.put(network)
        }

        return allowedCardNetworks
    }


    /**
     * Get allow card auth methods
     */
    private fun getAllowedCardAuthMethods(): JSONArray {
        val allowedCardAuthMethods = JSONArray()

        for (method in SUPPORTED_METHODS) {
            allowedCardAuthMethods.put(method)
        }

        return allowedCardAuthMethods
    }

    /**
     * Get card payment method
     * That uses getAllowedCardAuthMethods, getAllowedCardNetworks methods and maybe some additional parameters
     */
    private fun getCardPaymentMethod(
        additionalParams: JSONObject?,
        tokenizationSpecification: JSONObject?
    ): JSONObject {
        val params = JSONObject()
            .put("allowedAuthMethods", getAllowedCardAuthMethods())
            .put("allowedCardNetworks", getAllowedCardNetworks())

        // Additional parameters provided?
        if (additionalParams != null && additionalParams.length() > 0) {
            val keys = additionalParams.keys()
            while (keys.hasNext()) {
                val key = keys.next()
                params.put(key, additionalParams.get(key))
            }
        }

        val cardPaymentMethod = JSONObject()
        cardPaymentMethod.put("type", "CARD")
        cardPaymentMethod.put(
            "parameters", params
        )

        if (tokenizationSpecification != null) {
            cardPaymentMethod.put(
                "tokenizationSpecification", tokenizationSpecification
            )
        }

        return cardPaymentMethod
    }

    /**
     * Create base request by specifying api version
     */
    private fun getBaseRequest(): JSONObject {
        return JSONObject()
            .put("apiVersion", 2)
            .put("apiVersionMinor", 0)
    }

    /**
     * Initializes PaymentsClient object
     *
     * @param activity Activity
     * @param mode String
     * @return PaymentsClient
     */
    private fun initPaymentsClient(context: Context, environment: Environment): PaymentsClient {
        val builder = Wallet.WalletOptions.Builder()
        builder.setEnvironment(
            if (environment == Environment.TEST) WalletConstants.ENVIRONMENT_TEST
            else WalletConstants.ENVIRONMENT_PRODUCTION
        )
        return Wallet.getPaymentsClient(context, builder.build())
    }

    /**
     * Prepare payment data Request by creating a PaymentDataRequest object
     *
     * @param price String
     * @param currency String
     * @param gatewayMerchantId String
     * @return PaymentDataRequest
     */
    fun preparePaymentDataRequest(
        price: String,
        currency: String,
        gatewayMerchantId: String
    ): PaymentDataRequest {
        val paymentDataRequestJson = getBaseRequest()
        val additionalParams = JSONObject()

        val transactionJson = JSONObject()
        transactionJson
            .put("totalPriceStatus", "FINAL")
            .put("totalPrice", price)
            .put("currencyCode", currency)

        additionalParams.put("billingAddressRequired", false)

        paymentDataRequestJson
            .put(
                "allowedPaymentMethods", JSONArray()
                    .put(
                        getCardPaymentMethod(
                            additionalParams,
                            getTokenizationSpecification(gatewayMerchantId)
                        )
                    )
            )

        paymentDataRequestJson.put("shippingAddressRequired", false)
        paymentDataRequestJson.put("emailRequired", true)

        paymentDataRequestJson.put("transactionInfo", transactionJson)

        return PaymentDataRequest.fromJson(paymentDataRequestJson.toString())
    }

    /**
     * Get tokenization specification
     */
    private fun getTokenizationSpecification(gatewayMerchantId: String): JSONObject {
        val params = JSONObject().put("gateway", GATEWAY_TOKENIZATION_NAME)
        params.put("gatewayMerchantId", gatewayMerchantId)

        return JSONObject().put("type", "PAYMENT_GATEWAY").put("parameters", params)
    }

    /**
     * Prepare ready Request by creating an IsReadyToPayRequest object
     *
     * @return IsReadyToPayRequest
     */
    private fun prepareIsReadyToPayRequest(): IsReadyToPayRequest {
        val isReadyToPayRequest = getBaseRequest()
        isReadyToPayRequest.put(
            "allowedPaymentMethods", JSONArray()
                .put(getCardPaymentMethod(null, null))
        )
        return IsReadyToPayRequest.fromJson(isReadyToPayRequest.toString())
    }

    /**
     * Initializes payments client
     *
     * @param activity Activity
     * @param mode String
     * @param supportedNetworks String
     */
    internal fun init(
        context: Context,
        environment: Environment,
        supportedNetworks: List<SupportedNetworks>
    ): PaymentsClient {
        GooglePayManagement.supportedNetworks = supportedNetworks.map { it.value }
        return initPaymentsClient(context, environment)
    }

    /**
     * Determines if Google Pay is available
     *
     * @param paymentsClient PaymentsClient
     * @return Task<Boolean>
     */
    internal fun isPossible(paymentsClient: PaymentsClient): Task<Boolean> {
        val isReadyToPayRequest = prepareIsReadyToPayRequest()
        return paymentsClient.isReadyToPay(isReadyToPayRequest)
    }
}

