# Firebase Setup Instructions for ArtFlow App

To complete the Firebase integration in your ArtFlow app, you need to follow these steps:

## Step 1: Create a Firebase Project
1. Go to the [Firebase Console](https://console.firebase.google.com/)
2. Click "Add Project" and follow the setup steps
3. Enter your project name (e.g., "ArtFlow")
4. Accept the terms and click "Continue"
5. Optionally enable Google Analytics (recommended)
6. Click "Create Project"

## Step 2: Register Your Android App
1. In the Firebase console, click the Android icon to add an Android app
2. Enter your package name (find it in `app/build.gradle` as `applicationId`)
   - For this project it should be `com.example.artflow`
3. Enter an app nickname (optional)
4. Download the `google-services.json` file and place it in your `app/` directory
   - Path should be: `C:\Users\Dell\ArtFlow\app\google-services.json`

## Step 3: Enable Firebase Services
In the Firebase Console, enable the services you want to use:
1. Authentication:
   - Go to "Authentication" in the left panel
   - Click "Get Started"
   - Enable "Email/Password" sign-in method
2. Database (if needed):
   - Go to "Firestore Database" or "Realtime Database" 
   - Click "Create Database" and follow the setup wizard
3. Storage (if needed):
   - Go to "Storage" in the left panel
   - Click "Get Started" and follow the setup wizard

## Step 4: Sync Your Project
After adding the `google-services.json` file, sync your project with Gradle files in Android Studio.

## Step 5: Test Firebase Integration
Run your app and test the sign-up and login functionality to ensure Firebase integration is working properly.

## Troubleshooting
- If you encounter build errors, make sure the `google-services.json` file is in the correct location (`app/` directory)
- Ensure your device has an active internet connection when testing authentication
- Check that the package name in Firebase matches exactly with your `applicationId` in `build.gradle`
- If authentication fails, verify that the sign-in methods are enabled in the Firebase Console