package android.google.paysdk.data.model.request

data class CustomerDetails(
    val accountDetails: AccountDetails? = null,
    val billingAddress: AddressInfo? = null,
    val browserDetails: BrowserDetails? = null,
    val deliveryDetails: DeliveryDetails? = null,
    val addressMatch: String? = null,
    val email: String? = null,
    val homePhone: String? = null,
    val mobilePhone: String? = null,
    val workPhone: String? = null,
    val title: String? = null,
    val firstName: String? = null,
    val lastName: String? = null
)