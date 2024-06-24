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
- [Additional Notes](#additional-notes)


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

## Additional Notes

Evaluation / Things I would change
- It is good practice to extract string / colour resources to a res file - this improves code readibilty but also reusabilty and maintainabilty.
- I spent a while trying to understand why my snapshot tests would not work, I think it is something to do with Java versioning or using coroutines but I accept defeat for this submission (but will secretly continue to debug as I am a hopeless problem solver and finding a solution brings me joy) - I still committed the class so you know I tried!
- There is some light calculation in the ui screen layer for the pie chart, I think this should be extracted out to it's own use case and tested properly, however as it was not a requirement for this submission, I chose to focus my time on the requirements)
- I was not clear from the instructions whether I should have displayed the transactions but thought about this after submission. If I were to, I would include a button "Show latest transactions" which would show a dialog or bottom sheet with the week's transactions. As it seemed like the round up feature was the main goal of this submission, this is likely an oversight on my part. Apologies :)
- As the Api responses were fairly straughtforward I used one model for both data and ui, however, I don't believe this is the best way to do things, ideally, I would have seperate models for each and use mappers so that I would not have added responsibilty to the use cases to map data to the format needed for the uiState. For example converting minorUnits into majorUnits then formatting in the viewModel. (Good it wasn't in the ui though!)
- Last but not least, I could have done a kot better with the error handling! I set a quick dialog to show my exception handler works and forgot that I hadn't handled it properly in the Repo. I would have loved to create some specific expection types which include the error but for now I have made sure to include them in the logs.


