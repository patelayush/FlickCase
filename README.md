# FlickCase - Your Movie & TV Show Discovery App

[![Compose Multiplatform](https://img.shields.io/badge/Compose%20Multiplatform-1.7.x-brightgreen)](https://kotlinlang.org/docs/compose-multiplatform.html)
[![Platform](https://img.shields.io/badge/platform-Android%20%7C%20iOS%20%7C%20Desktop%20%7C%20Web-lightgrey)](https://kotlinlang.org/docs/compose-multiplatform.html)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.1.20-blue.svg)](https://kotlinlang.org/)
[![TMDb API](https://img.shields.io/badge/TMDb%20API-Used-yellow)](https://www.themoviedb.org/documentation/api)

**FlickCase** is a sleek and intuitive application built with **Compose Multiplatform** that allows you to discover new movies and TV shows, explore by genre, and find detailed information about your favorites. Enjoy a consistent experience across various devices, powered by the vast data of the TMDb API. 
*<br>Note - This is still under Development.*

**Download FlickCase today:**
* [**Google Play Store**](https://play.google.com/store/apps/details?id=org.appsmith.flickcase)
* [**Apple App Store:**](https://apps.apple.com/us/app/flickcase/id6745903913)
* [**Web Link**](https://patelayush.github.io/FlickCase/)

## Features

* **Trending Content:** Discover the most popular and currently trending movies and TV shows.
* **Genre Exploration:** Easily browse content by a wide variety of genres for both movies and TV shows.
* **Effortless Search:** Quickly find specific movies or TV shows by title.
* **Detailed Information:** Access comprehensive details including:
    * Release dates
    * Genre listings
    * Engaging overviews and synopses
    * Cast and crew information
    * User ratings
* **Cross-Platform:** Built with Compose Multiplatform for a consistent experience on Android, iOS, Desktop, and Web (depending on your target platforms).
* **Beautiful Interface:** A clean and user-friendly design powered by Material 3.

## Screenshots

## Tech Stack

* **Compose Multiplatform:** A declarative UI framework for building native applications across multiple platforms.
* **Kotlin:** The primary programming language.
* **TMDb API:** Used as the data source for movie and TV show information.
* **Ktor:** Asynchronous HTTP client for making API requests.
* **Kotlinx.serialization:** For handling JSON data from the API.
* **Coil:** For asynchronous image loading and caching.
* **Material 3:** For a modern and consistent UI design.

## Getting Started

### Prerequisites

* [Kotlin SDK](https://kotlinlang.org/docs/getting-started.html)
* [JDK (Java Development Kit)](https://openjdk.java.net/)
* [Android Studio](https://developer.android.com/studio/) (for Android development)
* [Xcode](https://developer.apple.com/xcode/) (for iOS development)

### Building and Running

1.  Clone the repository:
    ```bash
    git clone https://github.com/patelayush/FlickCase.git
    cd FlickCase
    ```
2.  *(Provide specific build commands for each platform. For example:)*
    * **Android:** Open the `androidApp` directory in Android Studio and run the app.
    * **iOS:** Open the `iosApp/iosApp.xcodeproj` in Xcode and run on a simulator or device.
    * **Desktop:** Run the appropriate Gradle task (e.g., `./gradlew desktopApp:run`).
    * **Web:** Run the appropriate Gradle task (e.g., `./gradlew webApp:browserDevelopmentRun`).

## API Key

This application utilizes the TMDb API to fetch movie and TV show data. You will need to obtain your own API key from [https://www.themoviedb.org/settings/api](https://www.themoviedb.org/settings/api) and include it in your project as an environment variable or within your code (be cautious about security when handling API keys).


## Contact

Ayush Patel - ayushpatel95@gmail.com

---

**Thank you for checking out FlickCase! Happy exploring!**
