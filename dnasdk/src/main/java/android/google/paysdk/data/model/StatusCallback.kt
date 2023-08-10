package android.google.paysdk.data.model

interface StatusCallback {
    fun onResponse(paymentResult: PaymentResult)
}