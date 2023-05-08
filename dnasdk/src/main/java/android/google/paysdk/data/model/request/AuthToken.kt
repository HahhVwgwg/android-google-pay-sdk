package android.google.paysdk.data.model.request

data class AuthToken(
    val access_token: String,
    val expires_in: Int? = null,
    val refresh_token: String? = null,
    val scope: String,
    val token_type: String
)