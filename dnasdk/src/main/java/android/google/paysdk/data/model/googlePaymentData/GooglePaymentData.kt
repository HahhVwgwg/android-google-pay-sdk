package android.google.paysdk.data.model.googlePaymentData

import com.google.gson.annotations.SerializedName

data class GooglePaymentData(
    @SerializedName("apiVersion") var apiVersion: Int? = null,
    @SerializedName("apiVersionMinor") var apiVersionMinor: Int? = null,
    @SerializedName("email") var email: String? = null,
    @SerializedName("paymentMethodData") var paymentMethodData: PaymentMethodData? = PaymentMethodData(),
    @SerializedName("shippingAddress") var shippingAddress: ShippingAddress? = ShippingAddress(),
    @SerializedName("versionCode") val versionCode: Int,
    @SerializedName("merchantId") val merchantId: String,
    @SerializedName("countryCode") val countryCode: String,
    @SerializedName("currencyCode") val currencyCode: String,
    @SerializedName("totalPrice") val totalPrice: Double,
    @SerializedName("optionalParameters") val optionalParameters: Map<String, Any>?,
    @SerializedName("paymentMethodToken") val paymentMethodToken: String
) {
    override fun toString(): String {
        return "GooglePaymentData(apiVersion=$apiVersion, apiVersionMinor=$apiVersionMinor, email=$email, paymentMethodData=$paymentMethodData, shippingAddress=$shippingAddress)"
    }
}

