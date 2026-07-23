package com.teEcclesia.shared.data.shared

import com.teEcclesia.shared.data.dataSource.remote.dto.IncompleteProfileResponse
import com.teEcclesia.shared.domain.exception.AccountPendingApprovalException
import com.teEcclesia.shared.domain.exception.EmailNotVerifiedException
import com.teEcclesia.shared.domain.exception.IncompleteProfileException
import com.teEcclesia.shared.domain.exception.PhoneNotVerifiedException
import com.teEcclesia.shared.domain.exception.UsernameOrPhoneNumberAlreadyExistsException
import com.teEcclesia.shared.domain.exception.InternetException
import com.teEcclesia.shared.domain.exception.InvalidCredentialsException
import com.teEcclesia.shared.domain.exception.InvalidRequestException
import com.teEcclesia.shared.domain.exception.NoNetworkException
import com.teEcclesia.shared.domain.exception.TooManyRequestsException
import com.teEcclesia.shared.domain.exception.UnAuthorizedException
import com.teEcclesia.shared.domain.exception.UnknownErrorException
import com.teEcclesia.shared.domain.exception.UserIsBlockedException
import com.teEcclesia.shared.domain.exception.PaymentRequiredException
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ResponseException
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.CancellationException

abstract class BaseRepository(val client: HttpClient) {

    suspend inline fun <reified T> tryToExecute(method: HttpClient.() -> HttpResponse): T {
        try {
            return client.method().body()
        } catch (e: ResponseException) {
            val status = e.response.status
            val message = e.message ?: "Request failed"

            throw when {
                status == HttpStatusCode.PreconditionRequired -> {
                    val body = runCatching { e.response.body<IncompleteProfileResponse>() }.getOrNull()
                    IncompleteProfileException(token = body?.token, refreshToken = body?.refreshToken)
                }
                status == HttpStatusCode.PreconditionFailed -> {
                    val body = runCatching { e.response.body<IncompleteProfileResponse>() }.getOrNull()
                    PhoneNotVerifiedException(token = body?.token, refreshToken = body?.refreshToken)
                }
                status == HttpStatusCode.Locked -> {
                    val body = runCatching { e.response.body<IncompleteProfileResponse>() }.getOrNull()
                    AccountPendingApprovalException(token = body?.token, refreshToken = body?.refreshToken)
                }
                status == HttpStatusCode.UnprocessableEntity -> EmailNotVerifiedException()
                status == HttpStatusCode.PaymentRequired -> PaymentRequiredException()
                status == HttpStatusCode.Unauthorized -> UnAuthorizedException()
                status == HttpStatusCode.NotFound -> InvalidCredentialsException()
                status == HttpStatusCode.Forbidden -> UserIsBlockedException()
                status == HttpStatusCode.TooManyRequests -> TooManyRequestsException()
                status == HttpStatusCode.BadRequest -> InvalidRequestException()
                status == HttpStatusCode.Conflict -> UsernameOrPhoneNumberAlreadyExistsException()
                status.value in 400..499 -> InvalidRequestException()
                status.value in 500..599 -> UnknownErrorException(message)
                else -> UnknownErrorException(message)
            }

        } catch (e: InternetException.NoInternetException) {
            throw e
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            when (e) {
                is UnresolvedAddressException -> throw NoNetworkException()
                is HttpRequestTimeoutException -> throw NoNetworkException()
                else -> throw UnknownErrorException(e.message.toString())
            }
        }
    }
}

val HttpStatusCode.Companion.PreconditionRequired
    get() = HttpStatusCode(428, "Precondition Required")

val HttpStatusCode.Companion.Locked
    get() = HttpStatusCode(423, "Locked")
