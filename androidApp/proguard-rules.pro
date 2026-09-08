# Proguard rules for androidApp

# 1. Keep generic signatures, annotations, metadata and stack trace attributes (required for Crashlytics)
-keepattributes *Annotation*, Signature, InnerClasses, EnclosingMethod, SourceFile, LineNumberTable

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

# 4. Keep JNI Native Methods and Native Libraries (MMKV, Skiko, SQLite)
-keepclasseswithmembernames class * {
    native <methods>;
}

# Keep MMKV JNI native classes & methods
-keep class com.tencent.mmkv.** { *; }
-dontwarn com.tencent.mmkv.**

# Keep Skiko & Compose native rendering
-keep class org.jetbrains.skiko.** { *; }
-dontwarn org.jetbrains.skiko.**

# Keep SQLite bundled native classes
-keep class androidx.sqlite.db.** { *; }

# KMPNotifier & Firebase
-dontwarn com.mmk.kmpnotifier.**
-dontwarn com.google.firebase.**


