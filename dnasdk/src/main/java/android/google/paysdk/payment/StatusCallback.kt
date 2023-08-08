package android.google.paysdk.payment

import android.google.paysdk.data.model.PaymentResult

interface StatusCallback {
    fun onResponse(paymentResult: PaymentResult)
}