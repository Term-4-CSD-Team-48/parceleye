# ParcelEye

ParcelEye is an Android application designed to help users track and manage their parcels efficiently.

## Overview

ParcelEye provides a seamless experience for users to monitor their parcels, view delivery history, and manage their profile. The app features a user-friendly interface with a bottom navigation bar for easy access to different functionalities.

## Features

- **User Authentication**: Secure login and registration system
- **Home Dashboard**: Quick overview of current parcels and status
- **Parcel History**: Track past deliveries and their details
- **Camera Integration**: Live parcel tracking with push notifications
- **Recording**: Document delivery information
- **User Profile**: Manage personal information and preferences

## Technical Details

- **Platform**: Android
- **Minimum SDK**: 24 (Android 7.0 Nougat)
- **Target SDK**: 35
- **Version**: 1.0
- **Language**: Java
- **Architecture**: Fragment-based navigation with Activity hosts
- **Dependencies**:
  - AndroidX components
  - Material Design components
  - Firebase integration for analytics

## Getting Started

### Prerequisites

- Android Studio
- JDK 11 or higher
- Android device or emulator running Android 7.0 (API 24) or higher

### Installation

1. Clone this repository
2. Open the project in Android Studio
3. Sync Gradle files
4. Run the application on your device or emulator

### Interacting with API and AI
Modify ApiClient.BASE_URL and line 93 of PlayerViewModel as needed.

If both API (any port, 80 as example) and AI (8080 always) are on same machine in the same network at 192.168.1.2 then

private static final String BASE_URL = "http://192.168.1.2:80";
.createMediaSource(MediaItem.fromUri("http://192.168.1.2:8080/hls/stream.m3u8"));

## Project Structure

- **MainActivity**: Handles user authentication (login)
- **Register**: Manages user registration
- **Home**: Main activity with bottom navigation to different fragments:
  - HomeFragment: Dashboard view
  - HistoryFragment: Past deliveries
  - RecordingFragment: Document deliveries
  - ProfileFragment: User settings and information
  - CameraFragment: Live parcel tracking

## Future Enhancements

- Cloud storage for parcel data
- Barcode scanning functionality
- Maps integration for tracking

## License

This project is part of the SUTD 50.001 course requirements.
