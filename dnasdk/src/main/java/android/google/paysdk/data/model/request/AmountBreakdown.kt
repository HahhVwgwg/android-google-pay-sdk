package android.google.paysdk.data.model.request
data class AmountBreakdown(
    val itemTotal: Amount? = null,
    val shipping: Amount? = null,
    val handling: Amount? = null,
    val taxTotal: Amount? = null
)
