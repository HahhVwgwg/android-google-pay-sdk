package android.google.paysdk.data.model.request

import android.google.paysdk.data.model.TransactionType

data class PaymentRequest(
    val auth: AuthToken? = null,
    val transactionType: TransactionType? = null,
    val operation: String? = null,
    val invoiceId: String? = null,
    val description: String? = null,
    val merchantCustomData: String? = null,
    val amount: Double? = null,
    val currency: String? = null,
    val entryMode: String? = null,
    val paymentSettings: PaymentSettings,
    val customerDetails: CustomerDetails,
    val periodic: Periodic? = null,
    val orderDetails: OrderDetails? = null,
    val requestorDetails: RequestorDetails? = null,
    val cardDetails: CardDetails? = null,
    val paymentTo: String? = null,
    val deliveryType: String? = null,
    val taxAmount: Int? = null,
    val language: String? = null,
    val paymentMethod: String? = null,
    val amountBreakdown: AmountBreakdown? = null,
    val orderLines: List<OrderLine>,
    val productType: String? = null,
    val CVC2RC: String? = null
) {
    override fun toString(): String {
        return "PaymentRequest(auth=$auth, transactionType=$transactionType, operation=$operation, invoiceId=$invoiceId, description=$description, merchantCustomData=$merchantCustomData, amount=$amount, currency=$currency, entryMode=$entryMode, paymentSettings=$paymentSettings, customerDetails=$customerDetails, periodic=$periodic, orderDetails=$orderDetails, requestorDetails=$requestorDetails, paymentTo=$paymentTo, deliveryType=$deliveryType, taxAmount=$taxAmount, language=$language, paymentMethod=$paymentMethod, amountBreakdown=$amountBreakdown, orderLines=$orderLines, productType=$productType, CVC2RC=$CVC2RC)"
    }
}


