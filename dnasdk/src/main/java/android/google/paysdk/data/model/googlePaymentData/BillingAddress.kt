package android.google.paysdk.data.model.googlePaymentData

import com.google.gson.annotations.SerializedName


data class BillingAddress (

  @SerializedName("address1"           ) var address1           : String? = null,
  @SerializedName("address2"           ) var address2           : String? = null,
  @SerializedName("address3"           ) var address3           : String? = null,
  @SerializedName("administrativeArea" ) var administrativeArea : String? = null,
  @SerializedName("countryCode"        ) var countryCode        : String? = null,
  @SerializedName("locality"           ) var locality           : String? = null,
  @SerializedName("name"               ) var name               : String? = null,
  @SerializedName("postalCode"         ) var postalCode         : String? = null,
  @SerializedName("sortingCode"        ) var sortingCode        : String? = null

)