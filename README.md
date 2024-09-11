# Jetpack Compose Image App (Unsplash API Integration)

## Features

- **Unsplash API Integration**: Fetch and display high-quality images from Unsplash.
- **Pinch-to-Zoom & Double-Tap Zoom**: Smooth zoom-in and zoom-out interactions with pinch gestures or double-tapping.
- **Image Preview**: Long-press to preview the image in fullscreen mode.
- **Network Monitoring**: Real-time network connection detection to handle offline scenarios gracefully.
- **Save Favorites**: Save your favorite images locally using Room Database for offline access.
- **Download Images**: Download and store images on your device for offline use.
- **Ktor for Network Requests**: Ktor is used to make efficient network requests to Unsplash API.
- **Navigation**: Seamless in-app navigation using Jetpack Compose's navigation component.
- **Dependency Injection**: Powered by Dagger-Hilt for clean architecture and scalability.

## Prerequisites

- Android Studio Flamingo or newer
- Kotlin 1.5+
- Unsplash API key (get it from [Unsplash Developers](https://unsplash.com/developers))

## Setup

1. Clone the repository:
   ```bash
   git clone https://github.com/Sumit123rox/ImageViewer.git
   ```
2. Navigate to the project directory:
   ```bash
   cd ImageViewer
   ```
3. Add your Unsplash API key in `local.properties`:
   ```properties
   UNSPLASH_ACCESS_KEY = your_access_key
   ```
4. Build and run the project in Android Studio.

## Usage

- **Browse Images**: Fetch and display images from Unsplash in a dynamic grid.
- **Zoom Interactions**: Pinch or double-tap to zoom in/out on images.
- **Preview**: Long-press on an image to open it in fullscreen mode.
- **Save Favorites**: Mark images as favorites and store them locally using Room Database for offline viewing.
- **Download Images**: Download images and store them on your device for offline access.
- **Offline Mode**: Handle poor or no network connections with graceful fallbacks.

## Key Components

- **Jetpack Compose**: Entire UI built using Jetpack Compose.
- **Ktor**: Ktor is used for efficient network communication with the Unsplash API.
- **Navigation**: Jetpack Compose Navigation to handle screen transitions.
- **Room Database**: Store and retrieve favorite images from a local database.
- **Download Manager**: Download images to the device’s storage.
- **Dagger-Hilt**: Dependency Injection for better modularity and testability.
- **Network Monitoring**: Real-time observation of the network state to manage offline/online behavior.

## Screenshots
<img src="https://github.com/user-attachments/assets/999f0734-50d4-4051-ab34-cd3275f1a715" width="100" height="200">&nbsp;&nbsp;&nbsp;
<img src="https://github.com/user-attachments/assets/7fb5dbd2-dc07-4bdb-8b57-c4bf8853d533" width="100" height="200">&nbsp;&nbsp;&nbsp;
<img src="https://github.com/user-attachments/assets/68c0aad4-a551-43a9-a7c4-f544f869742c" width="100" height="200">&nbsp;&nbsp;&nbsp;
<img src="https://github.com/user-attachments/assets/0768574d-06a3-48a5-b0ba-97f66d8cc292" width="100" height="200">&nbsp;&nbsp;&nbsp;
<img src="https://github.com/user-attachments/assets/1bdf1697-8b44-4de1-af6f-e906b5771552" width="100" height="200">


