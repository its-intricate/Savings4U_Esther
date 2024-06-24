# Savings4U Project

## Table of Contents
- [About the Project](#about-the-project)
- [Built With](#built-with)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
- [Usage](#usage)
- [Running Tests](#running-tests)
  - [Snapshot Tests](#snapshot-tests)
  - [Permissions and SDK Path](#permissions-and-sdk-path)

## About The Project

Savings4U is an Android application designed to help users manage their savings goals, display account information, and handle transactions efficiently. It leverages modern Android technologies such as Jetpack Compose for UI, Hilt for dependency injection, and Paparazzi for snapshot testing.

## Built With

* [Jetpack Compose](https://developer.android.com/jetpack/compose)
* [Hilt](https://dagger.dev/hilt/)
* [Paparazzi](https://github.com/cashapp/paparazzi)
* [MockK](https://mockk.io/)
* [JUnit 5](https://junit.org/junit5/)

## Getting Started

To get a local copy up and running, follow these steps.

### Prerequisites

Make sure you have the following installed:
* [Android Studio](https://developer.android.com/studio)
* [Java Development Kit (JDK)](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
* Android SDK

### Installation

1. Clone the repo
   ```sh
   git clone https://github.com/its-intricate/Savings4U.git
   ```
   
2. Navigate to the project directory
    ```sh
   cd Savings4U
   ```
3. Open the project in Android Studio.

4. Sync the project with Gradle files.

5. Use https://developer.starlingbank.com/docs#calling-the-starling-apis-1 to create your own access token and update in `NetworkModule`
   - Ideally use a secrets file or config file so that the access token is hidden. I have left it available for code review purposes.


## Usage

After setting up the project, you can run the application on an Android emulator or a physical device. The application will display the user's account information, including savings goals and transaction details.

## Running Tests

### Snapshot Tests

1. **Create Snapshot Test Class:**
   Create a test class for your composable UI components, using Paparazzi for snapshot testing.

2. **Run Tests:**
   Run the tests using your IDE or command line to ensure that snapshots are taken and compared correctly.

### Permissions and SDK Path

Ensure you have the necessary permissions for the directories where Paparazzi will store snapshots, and verify the `sdk.dir` path in `local.properties`.

