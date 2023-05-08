package android.google.paysdk.payment

import android.google.paysdk.data.model.request.BrowserDetails

object DataFactory {
    fun getBrowserDetails() = BrowserDetails(
        javaEnabled = false,
        language = "en-GB",
        challengeWindowSize = "03",
        acceptHeader = "text/html",
        screenColorDepth = "24",
        screenHeight = "1080",
        screenWidth = "1920",
        timeZone = "-360",
        userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Safari/537.36"
    )
}