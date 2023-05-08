package android.google.paysdk.data.model.googlePaymentData

import com.google.gson.annotations.SerializedName


data class Info (

  @SerializedName("assuranceDetails" ) var assuranceDetails : AssuranceDetails? = AssuranceDetails(),
  @SerializedName("billingAddress"   ) var billingAddress   : BillingAddress?   = BillingAddress(),
  @SerializedName("cardDetails"      ) var cardDetails      : String?           = null,
  @SerializedName("cardNetwork"      ) var cardNetwork      : String?           = null

)