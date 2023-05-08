package android.google.paysdk.data.model.request

data class PaymentSettings(
    val terminalId: String? = null,
    val threeDSVersion: String? = null,
    val paMatrixV2: String? = null,
    val avsHouseMatrix: Int? = null,
    val avsPostCodeMatrix: Int? = null,
    val cscMatrix: Int? = null,
    val paMatrix: Int? = null,
    val returnUrl: String? = null, // BackLink
    val failureReturnUrl: String? = null, // FailureBackLink
    val callbackUrl: String? = null, // PostLink
    val failureCallbackUrl: String? = null // FailurePostLink
)