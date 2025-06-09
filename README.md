# E-commerce Android Client

This repository contains the official Android client application for the **E-commerce System Backend**. The app is built entirely with modern Android development technologies, following **Clean Architecture** and **MVVM (Model-View-ViewModel)** principles to ensure a scalable, maintainable, and robust codebase.

The application provides a seamless user experience for browsing products, viewing details, and managing a shopping cart. It features an offline-first approach using a local database cache, with background data synchronization to keep the product catalog up-to-date. User identity is persisted across sessions using **Jetpack DataStore**.

## Table of Contents
- [Overview](#overview)
- [System Architecture](#system-architecture)
- [Technologies Used](#technologies-used)
- [Setup](#setup)
- [Building and Running the Application](#building-and-running-the-application)
- [Core Functionalities](#core-functionalities)
- [Highlights](#highlights)
- [License](#license)

## Overview

This Android client application provides the front-end interface for the e-commerce system. Key features include:

-   **View Product Catalog**: Fetches and displays a list of all available products from the backend.
-   **View Product Details**: Shows detailed information for a selected product.
-   **Shopping Cart**: Allows users to add products to their personal shopping cart.
-   **Offline Caching**: Serves product data from a local Room database, ensuring a fast and responsive UI even with poor network connectivity.
-   **Background Synchronization**: Uses WorkManager to periodically sync product data with the backend, ensuring the local cache is always fresh.
-   **Persistent User Identity**: Automatically generates and saves a unique user ID on the first launch using Jetpack DataStore, which is used for all cart-related operations.

## System Architecture

The application is structured according to **Clean Architecture** and **MVVM** principles, separating concerns into distinct layers[2]:

-   **Presentation Layer**: Built entirely with **Jetpack Compose** for a declarative and modern UI. **ViewModels** manage UI state and handle user interactions, exposing data via reactive streams (`StateFlow`).
-   **Domain Layer**: Contains core business logic and domain models. This layer is independent of any Android framework or external library.
-   **Data Layer**: Manages all data operations. It implements the **Repository Pattern** to act as a Single Source of Truth. This layer includes:
    -   **Remote Data Source**: Uses **Retrofit** and **OkHttp** for making network requests to the backend REST API.
    -   **Local Data Source**: Uses **Room Database** for caching product data offline.
    -   **Preferences Data Source**: Uses **Jetpack DataStore** to persist the user's unique ID.

**Dependency Injection** is managed throughout the app by **Hilt**, simplifying the creation and management of objects and their dependencies.

## Technologies Used

-   **Kotlin** (Primary language)
-   **Jetpack Compose** (Declarative UI Toolkit)
-   **Coroutines & Flow** (Asynchronous programming and reactive data streams)
-   **MVVM Architecture** (Model-View-ViewModel)
-   **Hilt** (Dependency Injection)
-   **Retrofit & OkHttp** (Networking for REST API communication)
-   **Room Database** (Local database for offline caching)
-   **WorkManager** (Background data synchronization)
-   **DataStore** (Persistent key-value storage for user ID)
-   **Jetpack Navigation** (Navigation between composable screens)

## Setup

### Prerequisites

-   Android Studio (latest stable version recommended)
-   Java 21 (JDK 21)
-   An Android Emulator or a physical Android device

### Backend Requirement

This client application **requires the corresponding backend system to be running**. The backend provides the API that this app consumes.

-   You can find the backend repository here: [shop-backend](https://github.com/dminior8/shop-backend)
-   Follow the instructions in the backend's README to set up and run the microservices using Docker Compose.
-   **Important**: When running the Android app on an emulator, the client will connect to the backend using the special IP address `http://10.0.2.2:8080`, which points to the host machine's `localhost`.

### Clone the repository

```bash
git clone https://github.com/dminior8/shop-client.git
cd shop-client
```

## Building and Running the Application

1.  Open the cloned project in **Android Studio**.
2.  Wait for Gradle to sync all project dependencies.
3.  Ensure the **backend services are running**.
4.  Select an emulator or connect a physical device.
5.  Run the `app` configuration.

## Core Functionalities

### User Identification

On the first launch, the application automatically generates a unique `userId` (UUID) and saves it persistently using **Jetpack DataStore**. This `userId` is then used for all subsequent operations that require user identification, such as creating a cart and adding products to it.

### Data Synchronization (Offline-First)

The application follows an **offline-first** strategy.
1.  The UI primarily displays data from the local **Room Database**, making it fast and available without a network connection.
2.  A **WorkManager** background task (`SyncProductsWorker`) is scheduled to run periodically.
3.  This worker fetches the latest product list from the backend API, clears the local database, and inserts the fresh data.
4.  The UI, observing the database via `Flow`, automatically updates to reflect the new data.

### Reactive UI

The UI is fully reactive. ViewModels expose state using `StateFlow`, and Jetpack Compose screens collect this flow. Any changes to the state (e.g., new products loaded, an item added to the cart) are automatically reflected in the UI without manual updates.

## Highlights

-   **Modern Android Stack**: Built with the latest recommended libraries from Google, including Kotlin, Compose, Hilt, and Coroutines.
-   **Declarative UI**: A fully-composable UI built with Jetpack Compose, leading to less code and a more intuitive development process.
-   **Offline-First Architecture**: Provides a reliable and smooth user experience by serving data from a local cache, resilient to network issues.
-   **Clean & Scalable Architecture**: Adherence to Clean Architecture and MVVM makes the codebase easy to test, maintain, and scale.
-   **Automated Dependency Management**: Hilt manages dependencies, reducing boilerplate and potential for errors.

## License

This project is licensed under the [MIT License](https://github.com/dminior8/shop-client/blob/main/LICENSE) - see the LICENSE file for details.
