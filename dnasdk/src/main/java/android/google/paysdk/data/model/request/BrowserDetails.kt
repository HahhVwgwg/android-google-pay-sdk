package android.google.paysdk.data.model.request


data class BrowserDetails(
    val acceptHeader: String? = null,
    val challengeWindowSize: String,
    val javaEnabled: Boolean,
    val language: String,
    val screenColorDepth: String,
    val screenHeight: String,
    val screenWidth: String,
    val timeZone: String,
    val userAgent: String
)
