import SwiftUI
import FirebaseCore
import FirebaseMessaging
import UserNotifications
import TeEcclesiaApp

#if canImport(KMPNotifier)
import KMPNotifier
#endif

class AppDelegate: NSObject, UIApplicationDelegate {
    func application(_ application: UIApplication,
                     didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {
        FirebaseApp.configure()

        #if canImport(KMPNotifier)
        KMPNotifier.shared.initialize(
            configuration: NotificationPlatformConfigurationIos(
                showPushNotification: false,
                askNotificationPermissionOnStart: true,
                notificationSoundName: nil
            )
        )
        #endif

        MainViewControllerKt.onApplicationStart()
        return true
    }

    func application(_ application: UIApplication, didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data) {
        Messaging.messaging().apnsToken = deviceToken
    }
}

@main
struct iOSApp: App {
    // register app delegate for Firebase setup
    @UIApplicationDelegateAdaptor(AppDelegate.self) var delegate

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}