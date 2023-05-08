package android.google.paysdk.data.model.googlePaymentData

import com.google.gson.annotations.SerializedName

data class CardInfo(
    @SerializedName("cardBrand") val cardBrand: String,
    @SerializedName("cardLast4Digits") val cardLast4Digits: String,
    @SerializedName("cardType") val cardType: String,
    @SerializedName("cardExpirationMonth") val cardExpirationMonth: Int,
    @SerializedName("cardExpirationYear") val cardExpirationYear: Int
)