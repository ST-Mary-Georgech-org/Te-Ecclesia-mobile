// swift-tools-version: 5.9
import PackageDescription
let package = Package(
  name: "KotlinMultiplatformLinkedPackage",
  platforms: [
    .iOS("16.0")
  ],
  products: [
    .library(
      name: "KotlinMultiplatformLinkedPackage",
      type: .none,
      targets: ["KotlinMultiplatformLinkedPackage"]
    )
  ],
  dependencies: [
    .package(
      url: "https://github.com/firebase/firebase-ios-sdk.git",
      from: "12.13.0"
    ),
    .package(path: "subpackages/io_github_mirzemehdi_kmpnotifier_push_firebase_2_0_0")
  ],
  targets: [
    .target(
      name: "KotlinMultiplatformLinkedPackage",
      dependencies: [
        .product(
          name: "FirebaseCore",
          package: "firebase-ios-sdk"
        ),
        .product(
          name: "FirebaseCrashlytics",
          package: "firebase-ios-sdk"
        ),
        .product(name: "io_github_mirzemehdi_kmpnotifier_push_firebase_2_0_0", package: "io_github_mirzemehdi_kmpnotifier_push_firebase_2_0_0")
      ]
    )
  ]
)
