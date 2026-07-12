package com.teEcclesia.identity.domain.exception

abstract class AuthenticationException(message: String) : Exception(message)

class UserIsBlockedException : AuthenticationException("user with mobile number: has many login retries")

class InvalidCredentialsException : AuthenticationException(
    "user with email, doesn't exist or password is incorrect"
)
class UnAuthorizedException : AuthenticationException("user has no access to application")
class UsernameOrPhoneNumberAlreadyExistsException : AuthenticationException("Username or Phone number already exists")
class TooManyRequestsException : AuthenticationException("Too many requests")
class NoNetworkException : AuthenticationException("No Internet Connection")
class InvalidRequestException : AuthenticationException("Invalid request")

open class InternetException(errorMessage: String = "") : AuthenticationException(errorMessage) {
    class WifiDisabledException : InternetException()
    class NoInternetException : InternetException()
    class NetworkNotSupportedException : InternetException()
}

class UnknownErrorException(message: String) : AuthenticationException(message)
