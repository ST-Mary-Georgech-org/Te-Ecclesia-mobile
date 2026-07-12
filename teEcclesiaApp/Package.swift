// swift-tools-version: 5.9
import PackageDescription

let package = Package(
    name: "TeEcclesiaApp",
    platforms: [
        .iOS(.v16)
    ],
    products: [
        .library(
            name: "TeEcclesiaApp",
            targets: ["TeEcclesiaApp"]
        ),
    ],
    targets: [
        .binaryTarget(
            name: "TeEcclesiaApp",
            path: "./build/XCFrameworks/release/TeEcclesiaApp.xcframework"
        ),
    ]
)
