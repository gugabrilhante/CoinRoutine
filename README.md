🚀 Getting Started

  To run this Kotlin Multiplatform (KMP) project, make sure your environment is properly set up.

✅ Prerequisites

  Follow the official JetBrains guide to configure your environment:
  👉 https://kotlinlang.org/docs/multiplatform/get-started.html
  
  This includes:
  
    Installing Android Studio (with KMP support)
    
    Setting up Xcode (for iOS)
    
    Configuring Kotlin Multiplatform environment

🔐 API Configuration (CoinRanking)

  This project uses the CoinRanking API to fetch cryptocurrency data.
  Create an account at CoinRanking and generate your API key.
  Store the API credentials locally as described below:

📱 Android
  Create (or update) a secrets.properties file in the root project and add:

    API_KEY=""
    API_BASE_URL=""

🍎 iOS
  Using Xcode, create a Secrets.plist file and add the following properties:

    apiUrl : String
    apiKey : String

📱 About the project

    This application is a virtual cryptocurrency wallet that simulates portfolio management, buy/sell operations, and fetches real-time market data from external APIs.
    
    It follows modern mobile development best practices, focusing on scalability, performance, and clean code organization.

✨ Key Features

  💰 Simulated Crypto Wallet

    Manage a portfolio with buy/sell logic.
  
  🌐 Real-time API Integration

    Fetch live data using Ktor 3.
  
  🎨 Modern UI with Compose Multiplatform

    Animations
    Dark/Light themes
    Custom components (TextFields, etc.)
    Type-safe navigation (Safe Args)
  
  🖼️ Image Loading

    Powered by Coil 3.
  
  💾 Local Data Persistence

    Using Room 2.7.0, shared across platforms.

🧱 Architecture

  🧼 Clean Architecture

    Clear separation of concerns (Domain, Data, Presentation)
  
  🔌 Dependency Injection

    Managed with Koin 4

🧪 Testing & Reliability

  ✅ Unit and UI testing with:
  
    Kotlin Test
    Turbine (Flow testing)
    AssertK
  
  ⚠️ Robust error handling and state management

🛠️ Tech Stack

  Kotlin 2
  Compose Multiplatform
  Ktor 3
  Coil 3
  Room 2.7.0
  Koin 4

🎯 Goal 
  Demonstrate how to build modern cross-platform applications using Kotlin, applying advanced concepts in architecture, API integration, and shared UI development.

This is a Kotlin Multiplatform project targeting Android, iOS.

* [/composeApp](./composeApp/src) is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
  - [commonMain](./composeApp/src/commonMain/kotlin) is for code that’s common for all targets.
  - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.
    For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
    the [iosMain](./composeApp/src/iosMain/kotlin) folder would be the right place for such calls.
    Similarly, if you want to edit the Desktop (JVM) specific part, the [jvmMain](./composeApp/src/jvmMain/kotlin)
    folder is the appropriate location.

* [/iosApp](./iosApp/iosApp) contains iOS applications. Even if you’re sharing your UI with Compose Multiplatform,
  you need this entry point for your iOS app. This is also where you should add SwiftUI code for your project.

### Build and Run Android Application

To build and run the development version of the Android app, use the run configuration from the run widget
in your IDE’s toolbar or build it directly from the terminal:
- on macOS/Linux
  ```shell
  ./gradlew :composeApp:assembleDebug
  ```
- on Windows
  ```shell
  .\gradlew.bat :composeApp:assembleDebug
  ```

### Build and Run iOS Application

To build and run the development version of the iOS app, use the run configuration from the run widget
in your IDE’s toolbar or open the [/iosApp](./iosApp) directory in Xcode and run it from there.

---

Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)…