package android.google.paysdk.domain

enum class Environment(val serverUrl: String) {
    TEST("https://test-pay.dnapayments.com"),
    PRODUCTION("https://pay.dnapayments.com")
}