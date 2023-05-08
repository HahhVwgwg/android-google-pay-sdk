package android.google.paysdk.data.model.request

data class RequestorDetails(
    val merchantCountryCode: String? = null,
    val merchantCategoryCode: String? = null,
    val merchantName: String? = null,
    val merchantNumber: String? = null,
    val requestorAuthenticationData: String? = null,
    val requestorAuthenticationMethod: String? = null,
    val requestorAuthenticationTimeStamp: String? = null,
    val requestorId: String? = null,
    val requestorName: String? = null,
    val requestorUrl: String? = null,
    val scaExemptionIndicator: String? = null,
    val merchantUrl: String? = null,
    val merchantDepartmentId: Int? = null,
    val merchantStoreId: Int? = null,
    val visaMid: String? = null,
    val mastercardMid: String? = null,
    val amexMid: String? = null,
    val unionPayMid: String? = null
)