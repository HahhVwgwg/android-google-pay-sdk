package android.google.paysdk.data.model

import android.google.paysdk.domain.DNAPaymentsErrorCode

class PaymentResult(
    val success: Boolean = false,
    val errorCode: DNAPaymentsErrorCode? = null,
    val errorDescription: String? = null
) {
    override fun toString(): String {
        return "PaymentResult(success=$success, errorCode=$errorCode, errorDescription=$errorDescription)"
    }
}