# Android CogniFit Integration

This project demonstrates how to integrate the CogniFit SDK into an Android application. It includes the necessary code for making API calls directly from the frontend as well as a backend example using PHP.

## Getting Started

### Prerequisites
- Have an account on https://www.cognifit.com/developers
- Android Studio
- Basic knowledge of Android development
- PHP server (for backend integration)

### Setup

1. Clone the repository:
   ```sh
   git clone https://github.com/yourusername/yourrepository.git
   cd yourrepository

2. Open the project (Android_CogniFit_Integration) in Android Studio.

3. Update the clientId, clientSecret, and userToken in the MainActivity.java, FetchAccessTokenTask.java, and ResultActivity.java files with your CogniFit credentials.

4. Run the project on your Android device or emulator.

### IMPORTANT: Obtaining API Credentials

Client ID and Client Secret: These can be found in the CogniFit platform under the API Keys section.
User Token: This is obtained by making a call to the register.php script and should be saved for future use. Make sure to store this token securely. See more in backend readme file
Access Token: This can be obtained either through API calls directly from the frontend (Android Studio) or by making a call to the accesstoken.php script in your backend.


### Project Structure

app/src: Contains the Android application code.
backend: Contains PHP scripts for backend integration.


### Backend Integration
We hard recommend backend integration for best practices (In the frontend project we have commented the backend version, you can change it)
To use the backend integration, deploy the PHP scripts in the backend folder to your PHP server.

### Usage
The application fetches an access token using the FetchAccessTokenTask class.
The MainActivity class initializes the CogniFit SDK and loads the desired activity.
Upon completion, the ResultActivity class handles the report generation and downloading proce

### Notes

Ensure the android device has notifications enablet to recieve download notifications
