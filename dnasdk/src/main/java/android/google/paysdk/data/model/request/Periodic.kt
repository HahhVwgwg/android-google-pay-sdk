package android.google.paysdk.data.model.request

data class Periodic(
    val recurringFrequency: String? = null,
    val recurringExpirationDate: String? = null,
    val periodicType: String,
    val numberOfInstallments: String? = null,
    val sequenceType: String
)