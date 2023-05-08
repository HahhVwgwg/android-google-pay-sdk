package android.google.paysdk.data.model.googlePaymentData

import com.google.gson.annotations.SerializedName

data class TokenizationData(
    @SerializedName("token") var token: String? = null,
    @SerializedName("type") var type: String? = null

) {
    override fun toString(): String {
        return "TokenizationData(token=$token, type=$type)"
    }
}