import SwiftUI
import FirebaseCore
import FirebaseCrashlytics
import FirebaseMessaging
import UserNotifications
import TeEcclesiaApp

// TODO: review and refactor
class AppDelegate: NSObject, UIApplicationDelegate {
    
    func application(
        _ application: UIApplication,
        didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]? = nil
    ) -> Bool {
        // 1. Initialize Firebase
        FirebaseApp.configure()
        
        // 2. Setup Crashlytics Bridge (Must be before Kotlin initialization if possible)
        setupCrashlytics()
        
        // 3. Initialize Kotlin Shared Module (Koin, KMPNotifier, etc.)
        MainViewControllerKt.onApplicationStart()
        
        // 4. Setup Push Notifications
        setupNotifications(application: application)
        
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
    
    private func setupNotifications(application: UIApplication) {
        UNUserNotificationCenter.current().delegate = self
        
        let authOptions: UNAuthorizationOptions = [.alert, .badge, .sound]
        UNUserNotificationCenter.current().requestAuthorization(
            options: authOptions,
            completionHandler: { _, _ in }
        )
        
        application.registerForRemoteNotifications()
        
        Messaging.messaging().delegate = self
    }

    func application(_ application: UIApplication, didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data) {
        Messaging.messaging().apnsToken = deviceToken
    }
    
    func application(_ application: UIApplication, didFailToRegisterForRemoteNotificationsWithError error: Error) {
        print("Failed to register for remote notifications: \(error.localizedDescription)")
    }
}

// MARK: - UNUserNotificationCenterDelegate
extension AppDelegate: UNUserNotificationCenterDelegate {
    func userNotificationCenter(
        _ center: UNUserNotificationCenter,
        willPresent notification: UNNotification,
        withCompletionHandler completionHandler: @escaping (UNNotificationPresentationOptions) -> Void
    ) {
        // Show notifications even when the app is in foreground
        completionHandler([[.banner, .list, .sound]])
    }

    func userNotificationCenter(
        _ center: UNUserNotificationCenter,
        didReceive response: UNNotificationResponse,
        withCompletionHandler completionHandler: @escaping () -> Void
    ) {
        // Handle notification click
        completionHandler()
    }
}

// MARK: - MessagingDelegate
extension AppDelegate: MessagingDelegate {
    func messaging(_ messaging: Messaging, didReceiveRegistrationToken fcmToken: String?) {
        print("Firebase registration token: \(String(describing: fcmToken))")
        // Token can be sent to server if needed
    }
}

@main
struct iOSApp: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self) var delegate

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
