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
        setupCrashlytics()

        if Bundle.main.path(forResource: "GoogleService-Info", ofType: "plist") != nil {
            if FirebaseApp.app() == nil {
                FirebaseApp.configure()
                PushNotificationsInitializer.shared.initialize(showPushNotification: true)
            }
        } else {
            print("GoogleService-Info.plist not found. Firebase not initialized.")
        }

        return true
    }

    private func setupCrashlytics() {
        #if DEBUG
        Crashlytics.crashlytics().setCrashlyticsCollectionEnabled(false)
        #else
        Crashlytics.crashlytics().setCrashlyticsCollectionEnabled(true)
        #endif

        #if canImport(TeEcclesiaApp)
        // Link Kotlin exceptions to Firebase Crashlytics
        IosCrashLoggerBridge.shared.delegate = { (throwable: KotlinThrowable) in
            let nsError = NSError(
                domain: "KotlinError",
                code: 0,
                userInfo: [
                    NSLocalizedDescriptionKey: throwable.message ?? "Unknown Kotlin Exception",
                    "KotlinStackTrace": String(describing: throwable)
                ]
            )
            Crashlytics.crashlytics().record(error: nsError)
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
