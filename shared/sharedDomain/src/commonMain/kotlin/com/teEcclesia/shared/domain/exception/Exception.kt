package com.teEcclesia.shared.domain.exception

abstract class BaseException(message: String) : Exception(message)

class UserIsBlockedException(message: String?) :
    BaseException(message ?: "المستخدم بهذا الرقم: لديه محاولات تسجيل دخول كثيرة")

class InvalidCredentialsException(message: String?) : BaseException(
    message ?: "المستخدم بهذا البريد الإلكتروني غير موجود، أو كلمة المرور غير صحيحة"
)
class UnAuthorizedException(message: String?) : BaseException(message ?: "المستخدم ليس لديه صلاحية الوصول إلى التطبيق")
class UsernameOrPhoneNumberAlreadyExistsException(message: String?) :
    BaseException(message ?: "اسم المستخدم أو رقم الهاتف موجود بالفعل")
class DuplicatePhoneException(message: String?) : BaseException(message ?: "رقم الهاتف هذا مرتبط بحسابات متعددة. يرجى استخدام الرقم القومي لإعادة تعيين كلمة المرور.")
class TooManyRequestsException(message: String?) : BaseException(message ?: "طلبات كثيرة جدًا")
class NoNetworkException(message: String?) : BaseException(message ?: "لا يوجد اتصال بالإنترنت")
class InvalidRequestException(message: String?) : BaseException(message ?: "طلب غير صالح")
class PaymentRequiredException(message: String?) : BaseException(message ?: "الدفع مطلوب.")
class ServerErrorException(message: String?) : BaseException(message ?: "خطأ في السرفر")

open class InternetException(errorMessage: String = "") : BaseException(errorMessage) {
    class WifiDisabledException : InternetException()
    class NoInternetException : InternetException()
    class NetworkNotSupportedException : InternetException()
}

class UnknownErrorException(message: String) : BaseException(message)

class IncompleteProfileException(val token: String? = null, val refreshToken: String? = null, message: String?) : BaseException(message ?: "ملف المستخدم غير مكتمل")

class PhoneNotVerifiedException(val token: String? = null, val refreshToken: String? = null, message: String?) : BaseException(message ?: "رقم هاتف المستخدم غير موثق")

class EmailNotVerifiedException(message: String?) : BaseException(message ?: "البريد الإلكتروني للمستخدم غير موثق")

class AccountPendingApprovalException(val token: String? = null, val refreshToken: String? = null, message: String?) : BaseException(message ?: "الحساب في انتظار الموافقة")

class AccountDeletedException(message: String?) : BaseException(message ?: "تم حذف الحساب ويمكن إعادة تفعيله")