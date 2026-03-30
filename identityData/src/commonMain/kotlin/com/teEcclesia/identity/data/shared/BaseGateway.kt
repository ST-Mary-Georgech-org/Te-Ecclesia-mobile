package com.teEcclesia.identity.data.shared

import com.teEcclesia.identity.domain.exception.UsernameOrPhoneNumberAlreadyExistsException
import com.teEcclesia.identity.domain.exception.InternetException
import com.teEcclesia.identity.domain.exception.InvalidCredentialsException
import com.teEcclesia.identity.domain.exception.InvalidRequestException
import com.teEcclesia.identity.domain.exception.NoNetworkException
import com.teEcclesia.identity.domain.exception.TooManyRequestsException
import com.teEcclesia.identity.domain.exception.UnAuthorizedException
import com.teEcclesia.identity.domain.exception.UnknownErrorException
import com.teEcclesia.identity.domain.exception.UserIsBlockedException
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ResponseException
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.CancellationException

abstract class BaseGateway(val client: HttpClient) {
    suspend inline fun <reified T> tryToExecute(method: HttpClient.() -> HttpResponse): T {
        try {
            return client.method().body()
        } catch (e: ResponseException) {
            val status = e.response.status
            val errorResponse = runCatching { e.response.body<ErrorResponse>() }.getOrNull()
            val message = errorResponse?.message ?: e.message ?: "Request failed"

            throw when {
                status == HttpStatusCode.Unauthorized -> UnAuthorizedException()
                status == HttpStatusCode.NotFound -> InvalidCredentialsException()
                status == HttpStatusCode.Forbidden -> UserIsBlockedException()
                status == HttpStatusCode.TooManyRequests -> TooManyRequestsException()
                status == HttpStatusCode.BadRequest -> InvalidRequestException()
                status == HttpStatusCode.Conflict -> UsernameOrPhoneNumberAlreadyExistsException()
                status.value in 400..499 -> InvalidRequestException()
                status.value in 500..599 -> UnknownErrorException("HTTP ${status.value}: $message")
                else -> UnknownErrorException("HTTP ${status.value}: $message")
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