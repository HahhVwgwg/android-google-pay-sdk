package android.google.paysdk.data.model.request

data class OrderDetails(
    val deviceChannel: String? = null,
    val giftCardAmount: Int? = null,
    val giftCardCount: Int? = null,
    val giftCardCurrencyId: String? = null,
    val preOrderDate: String? = null,
    val preOrderPurchaseIndicator: String? = null,
    val purchaseDate: String? = null,
    val reorderItemsIndicator: String? = null,
    val transactionType: String? = null
)
