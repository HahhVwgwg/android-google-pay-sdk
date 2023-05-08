package android.google.paysdk.data.model

import android.google.paysdk.domain.DNAPaymentsErrorCode

class PaymentResult(
    val success: Boolean = false,
    val errorCode: DNAPaymentsErrorCode? = null,
    val errorDescription: String? = null
)