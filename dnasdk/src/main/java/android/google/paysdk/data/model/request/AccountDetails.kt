package android.google.paysdk.data.model.request

data class AccountDetails(
    val accountAgeIndicator: String? = null,
    val accountChangeDate: String? = null,
    val accountChangeIndicator: String? = null,
    val accountDate: String? = null,
    val accountDayTransactions: String? = null,
    val accountId: String? = null,
    val accountPasswordChangeDate: String? = null,
    val accountPasswordChangeIndicator: String? = null,
    val accountProvisioningAttempts: String? = null,
    val accountPurchaseCount: String? = null,
    val accountType: String? = null,
    val accountYearTransactions: String? = null,
    val paymentAccountAgeIndicator: String? = null,
    val paymentAccountDate: String? = null,
    val priorAuthenticationData: String? = null,
    val priorAuthenticationMethod: String? = null,
    val priorAuthenticationReference: String? = null,
    val priorAuthenticationTimestamp: String? = null,
    val suspiciousAccountActivity: String? = null
)