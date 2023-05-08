package android.google.paysdk.utils

import android.google.paysdk.data.model.PaymentResult
import android.google.paysdk.domain.DNAPaymentsErrorCode
import android.system.ErrnoException
import com.google.gson.Gson
import retrofit2.HttpException
import java.net.SocketTimeoutException

fun Throwable.catchError(): PaymentResult {
    this.printStackTrace()
    return when (this) {
        is HttpException -> {
            val message: android.google.paysdk.data.model.APIError = Gson().fromJson(
                this.response()?.errorBody()?.charStream(),
                android.google.paysdk.data.model.APIError::class.java
            )
            PaymentResult(false, DNAPaymentsErrorCode.SERVER_ERROR, message.message)
        }
        is ErrnoException -> {
            PaymentResult(false, DNAPaymentsErrorCode.NO_CONNECTION_ERROR)
        }
        is SocketTimeoutException -> {
            PaymentResult(false, DNAPaymentsErrorCode.TIMEOUT_ERROR)
        }
        else -> {
            PaymentResult(false, DNAPaymentsErrorCode.UNKNOWN_ERROR)
        }
    }
}