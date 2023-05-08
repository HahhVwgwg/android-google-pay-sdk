package android.google.paysdk.data.model.googlePaymentData

import com.google.gson.annotations.SerializedName


data class PaymentMethodData (

  @SerializedName("description"      ) var description      : String?           = null,
  @SerializedName("info"             ) var info             : Info?             = Info(),
  @SerializedName("tokenizationData" ) var tokenizationData : TokenizationData? = TokenizationData(),
  @SerializedName("type"             ) var type             : String?           = null

) {
  override fun toString(): String {
    return "PaymentMethodData(description=$description, info=$info, tokenizationData=$tokenizationData, type=$type)"
  }
}