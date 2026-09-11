//
//  AppDelegate.swift
//  iosApp
//

import UIKit
import TeEcclesiaApp
import FirebaseCore
import FirebaseCrashlytics
import FirebaseMessaging

class AppDelegate: NSObject, UIApplicationDelegate {

    func application(
        _ application: UIApplication,
        didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]? = nil
    ) -> Bool {
        InitKoinKt.doInitKoin()

        if Bundle.main.path(forResource: "GoogleService-Info", ofType: "plist") != nil {
            if FirebaseApp.app() == nil {
                FirebaseApp.configure()
                PushNotificationsInitializer.shared.initialize(showPushNotification: true)
                setupCrashlytics()
            }
        } else {
            print("GoogleService-Info.plist not found. Firebase not initialized.")
        }

        return true
    }

    private func setupCrashlytics() {
        Crashlytics.crashlytics().setCrashlyticsCollectionEnabled(true)
        print("[Crashlytics] Firebase Crashlytics initialized and collection enabled")

        #if canImport(TeEcclesiaApp)
        // Link Kotlin exceptions to Firebase Crashlytics
        IosCrashLoggerBridge.shared.delegate = { (throwable: KotlinThrowable, message: String, stackTrace: String) in
            // 1. Log directly to Crashlytics breadcrumbs / logs
            Crashlytics.crashlytics().log("[FATAL KOTLIN EXCEPTION] \(message)")
            Crashlytics.crashlytics().log("StackTrace:\n\(stackTrace)")

            // 2. Set Custom Keys visible immediately in the Crash page under 'Keys' tab
            Crashlytics.crashlytics().setCustomValue(message, forKey: "FatalKotlinError")
            Crashlytics.crashlytics().setCustomValue(stackTrace, forKey: "FatalKotlinStack")

            // 3. Record non-fatal error with the exact stack trace
            let nsError = NSError(
                domain: "KotlinError",
                code: 0,
                userInfo: [
                    NSLocalizedDescriptionKey: message,
                    "KotlinStackTrace": stackTrace
                ]
            )
            Crashlytics.crashlytics().record(error: nsError)
            print("[Crashlytics] Successfully recorded Kotlin error to Crashlytics: \(message)")
        }

        IosCrashLoggerBridge.shared.customKeyDelegate = { (key: String, value: String) in
            Crashlytics.crashlytics().setCustomValue(value, forKey: key)
        }

        IosCrashLoggerBridge.shared.logDelegate = { (message: String) in
            Crashlytics.crashlytics().log(message)
        }
        #endif
    }

    func application(
        _ application: UIApplication,
        didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data
    ) {
        Messaging.messaging().apnsToken = deviceToken
    }

    func application(
        _ application: UIApplication,
        didFailToRegisterForRemoteNotificationsWithError error: Error
    ) {
        print("Failed to register for remote notifications: \(error.localizedDescription)")
    }
}
