package android.google.paysdk.domain

enum class DNAPaymentsErrorCode {
    // Unknown error
    UNKNOWN_ERROR,

    // Timeout error
    TIMEOUT_ERROR,

    // No connection error
    NO_CONNECTION_ERROR,

    // Server error
    SERVER_ERROR,

    // Payment cancelled error
    PAYMENT_CANCELLED_ERROR,

    // Payment refused error
    PAYMENT_REFUSED_ERROR,

    // Payment data error
    PAYMENT_DATA_ERROR,
}