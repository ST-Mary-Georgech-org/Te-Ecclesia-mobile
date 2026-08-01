package com.teEcclesia.shared.domain.exception

abstract class BaseException(message: String) : Exception(message)

class UserIsBlockedException : BaseException("user with mobile number: has many login retries")

class InvalidCredentialsException : BaseException(
    "user with email, doesn't exist or password is incorrect"
)
class UnAuthorizedException : BaseException("user has no access to application")
class UsernameOrPhoneNumberAlreadyExistsException : BaseException("Username or Phone number already exists")
class DuplicatePhoneException(message: String = "This phone number is associated with multiple accounts. Please use your National ID to reset your password.") : BaseException(message)
class TooManyRequestsException : BaseException("Too many requests")
class NoNetworkException : BaseException("No Internet Connection")
class InvalidRequestException(message: String = "Invalid request") : BaseException(message)
class PaymentRequiredException: BaseException("Payment required.")
class ServerErrorException(message: String = "Server error") : BaseException(message)

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

