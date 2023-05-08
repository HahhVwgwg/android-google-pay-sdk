package android.google.paysdk.data.model.request

data class OrderLine(
    val name: String,
    val quantity: Int,
    val unitPrice: Int,
    val totalAmount: Int,
    val taxRate: Int,
    val totalTaxAmount: Int
)