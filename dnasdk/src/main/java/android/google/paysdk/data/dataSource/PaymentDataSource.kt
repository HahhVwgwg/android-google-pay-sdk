package android.google.paysdk.data.dataSource

import android.google.paysdk.data.model.PaymentResponse
import android.google.paysdk.data.model.request.AuthToken
import android.google.paysdk.data.model.request.PaymentRequest
import retrofit2.http.*

// Create a service interface to define the API endpoints that you want to call.
interface PaymentDataSource {

    @POST("/checkout/api/v2/payments")
    suspend fun pay(@Header("Authorization") token: String, @Body request: PaymentRequest): PaymentResponse

    @FormUrlEncoded
    @POST("/checkout/oauth/oauth2/token")
    suspend fun getAuthToken(
        @Field("grant_type") grantType: String,
        @Field("scope") scope: String,
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("invoice_id") invoiceId: String,
        @Field("terminal") terminal: String,
        @Field("amount") amount: Int,
        @Field("currency") currency: String,
        @Field("payment_form_url") paymentFormURL: String
    ): AuthToken
}

