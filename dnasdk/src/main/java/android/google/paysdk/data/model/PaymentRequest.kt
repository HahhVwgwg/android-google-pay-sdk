package android.google.paysdk.data.model

import com.google.gson.annotations.SerializedName

data class AuthTokenRequest(
    @SerializedName("grant_type") val grantType: String,
    @SerializedName("scope") val scope: String,
    @SerializedName("client_id") val clientId: String,
    @SerializedName("client_secret") val clientSecret: String,
    @SerializedName("invoice_id") val invoiceId: String,
    @SerializedName("terminal") val terminal: String,
    @SerializedName("amount") val amount: Double,
    @SerializedName("currency") val currency: String,
    @SerializedName("payment_form_url") val paymentFormURL: String
)