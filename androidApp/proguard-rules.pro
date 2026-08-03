# Proguard rules for androidApp

# 1. Keep generic signatures, annotations and metadata (required by Kotlin and kotlinx.serialization)
-keepattributes *Annotation*, Signature, InnerClasses, EnclosingMethod

# Keep Kotlin Metadata
-keep class kotlin.Metadata { *; }

# 2. kotlinx.serialization rules
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations
-keepclassmembers class * {
    @kotlinx.serialization.Serializable *;
    @kotlinx.serialization.SerialName *;
}
-keep class kotlinx.serialization.internal.** { *; }
-keepclassmembers class kotlinx.serialization.internal.** {
    void <init>(...);
}

# 3. Keep all DTOs, Requests, Responses, and Data Models
# This prevents R8 from obfuscating them which breaks JSON reflection and deserialization
-keep class com.teEcclesia.**.dto.** { *; }
-keep class com.teEcclesia.**.model.** { *; }
-keep class com.teEcclesia.**.data.dataSource.remote.dto.** { *; }

# Ignore warnings for unresolved types in third-party libraries (optional but recommended)
-dontwarn kotlinx.serialization.**
