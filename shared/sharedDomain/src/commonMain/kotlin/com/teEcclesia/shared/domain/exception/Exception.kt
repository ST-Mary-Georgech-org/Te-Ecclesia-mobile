package com.teEcclesia.shared.domain.exception

abstract class BaseException(message: String) : Exception(message)

class UserIsBlockedException : BaseException("user with mobile number: has many login retries")

class InvalidCredentialsException : BaseException(
    "user with email, doesn't exist or password is incorrect"
)
class UnAuthorizedException : BaseException("user has no access to application")
class UsernameOrPhoneNumberAlreadyExistsException : BaseException("Username or Phone number already exists")
class TooManyRequestsException : BaseException("Too many requests")
class NoNetworkException : BaseException("No Internet Connection")
class InvalidRequestException : BaseException("Invalid request")
class PaymentRequiredException: BaseException("Payment required.")

open class InternetException(errorMessage: String = "") : BaseException(errorMessage) {
    class WifiDisabledException : InternetException()
    class NoInternetException : InternetException()
    class NetworkNotSupportedException : InternetException()
}

class UnknownErrorException(message: String) : BaseException(message)

class IncompleteProfileException(val token: String? = null, val refreshToken: String? = null) : BaseException("User profile is incomplete")

class PhoneNotVerifiedException(val token: String? = null, val refreshToken: String? = null) : BaseException("User phone number is not verified")

class EmailNotVerifiedException : BaseException("User email is not verified")

class AccountPendingApprovalException(val token: String? = null, val refreshToken: String? = null) : BaseException("Account pending approval")

