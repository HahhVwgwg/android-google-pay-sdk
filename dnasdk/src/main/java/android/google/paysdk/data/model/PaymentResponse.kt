package android.google.paysdk.data.model

import com.google.gson.annotations.SerializedName


data class PaymentResponse(
    @SerializedName("accountId") var accountId: String? = null,
    @SerializedName("amount") var amount: Double? = null,
    @SerializedName("authCode") var authCode: String? = null,
    @SerializedName("authDateTimeUTC") var authDateTimeUTC: String? = null,
    @SerializedName("avsHouseNumberResult") var avsHouseNumberResult: String? = null,
    @SerializedName("avsPostcodeResult") var avsPostcodeResult: String? = null,
    @SerializedName("cardExpiryDate") var cardExpiryDate: String? = null,
    @SerializedName("cardPanStarred") var cardPanStarred: String? = null,
    @SerializedName("cardSchemeId") var cardSchemeId: Int? = null,
    @SerializedName("cardSchemeName") var cardSchemeName: String? = null,
    @SerializedName("cardTokenId") var cardTokenId: String? = null,
    @SerializedName("cardholderName") var cardholderName: String? = null,
    @SerializedName("cscResult") var cscResult: String? = null,
    @SerializedName("currency") var currency: String? = null,
    @SerializedName("eftPaymentId") var eftPaymentId: String? = null,
    @SerializedName("errorCode") var errorCode: Int? = null,
    @SerializedName("id") var id: String? = null,
    @SerializedName("invoiceId") var invoiceId: String? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("payerAuthenticationResult") var payerAuthenticationResult: String? = null,
    @SerializedName("paymentMethod") var paymentMethod: String? = null,
    @SerializedName("responseCode") var responseCode: String? = null,
    @SerializedName("rrn") var rrn: String? = null,
    @SerializedName("settled") var settled: Boolean? = null,
    @SerializedName("stan") var stan: Int? = null,
    @SerializedName("status") var status: String? = null,
    @SerializedName("success") var success: Boolean? = null
)