package android.google.paysdk.data.model.googlePaymentData

import com.google.gson.annotations.SerializedName


data class AssuranceDetails(
    @SerializedName("accountVerified") var accountVerified: Boolean? = null,
    @SerializedName("cardHolderAuthenticated") var cardHolderAuthenticated: Boolean? = null
)