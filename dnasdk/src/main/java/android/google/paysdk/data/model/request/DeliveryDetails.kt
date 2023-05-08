package android.google.paysdk.data.model.request

data class DeliveryDetails(
    val deliveryAddressUsageDate: String? = null,
    val deliveryAddressUsageIndicator: String? = null,
    val deliveryEmailAddress: String? = null,
    val deliveryIndicator: String? = null,
    val deliveryNameIndicator: String? = null,
    val deliveryTimeFrame: String? = null,
    val deliveryAddress: AddressInfo
)