package android.google.paysdk.data.model.request

data class AddressInfo(
    val title: String? = null,
    val firstName: String,
    val lastName: String,
    val addressLine1: String,
    val addressLine2: String? = null,
    val addressLine3: String? = null,
    val postalCode: String,
    val city: String,
    val region: String? = null,
    val phone: String? = null,
    val country: String
)
