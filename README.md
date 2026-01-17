# ArtFlow (Android)

ArtFlow is a modern Android-based digital art marketplace that connects artists and customers through a mobile-first experience. The application allows artists to showcase, manage, and sell their artwork, while customers can explore, order, and track custom art commissions seamlessly using their smartphones.

---

## Features

### Artist Features
- **Artist Dashboard**: View orders, commission status, and basic performance insights  
- **Artwork Management**: Add, edit, and manage artworks with images, pricing, and descriptions  
- **Order Management**: Accept or reject commission requests from customers  
- **Profile Management**: Update artist profile information and bio  
- **Real-Time Updates**: Orders and status updates synced instantly via Firebase  

### Customer Features
- **Browse Artwork**: Explore artworks from multiple artists  
- **Artwork Details**: View high-quality images, artist information, price, and category  
- **Order Placement**: Place custom artwork orders  
- **Order Tracking**: Track order status in real time  
- **Profile Management**: Manage personal details and order history  

### General Features
- **User Authentication**: Secure login and signup using Firebase Authentication  
- **Role-Based Access**: Separate flows and dashboards for artists and customers  
- **Cloud Database**: Firebase Realtime Database / Firestore integration  
- **Image Storage**: Artwork images stored securely using Firebase Storage  
- **Modern UI**: Clean Material Design–based user interface  

---

## Tech Stack

- **Language**: Java  
- **IDE**: Android Studio  
- **Minimum SDK**: API 24 (Android 7.0)  
- **Target SDK**: API 34  
- **Backend Services**: Firebase
  - Firebase Authentication  
  - Firebase Realtime Database / Firestore  
  - Firebase Storage  
- **UI Framework**: Android XML + Material Components  

---

## Project Structure
```
ArtFlow-Android/
├── app/
│ └── src/
│ └── main/
│ ├── java/com/example/artflow/
│ │ ├── activities/
│ │ │ ├── MainActivity.java
│ │ │ ├── LoginActivity.java
│ │ │ ├── SignupActivity.java
│ │ │ ├── ArtistDashboardActivity.java
│ │ │ ├── CustomerDashboardActivity.java
│ │ │ ├── AddArtworkActivity.java
│ │ │ ├── MyArtworksActivity.java
│ │ │ ├── ProfileActivity.java
│ │ │ └── ChangePasswordActivity.java
│ │ ├── adapters/
│ │ │ ├── ArtworkAdapter.java
│ │ │ ├── OrderAdapter.java
│ │ │ └── CompletedOrderAdapter.java
│ │ ├── models/
│ │ │ ├── User.java
│ │ │ ├── Artist.java
│ │ │ ├── Customer.java
│ │ │ ├── Artwork.java
│ │ │ └── Order.java
│ │ ├── firebase/
│ │ │ ├── FirebaseHelper.java
│ │ │ └── FirebaseUtil.java
│ │ └── utils/
│ │ └── Select.java
│ ├── res/
│ │ ├── layout/
│ │ ├── drawable/
│ │ ├── values/
│ │ └── mipmap/
│ └── AndroidManifest.xml
├── google-services.json
├── build.gradle
└── settings.gradle
```


---

## Prerequisites
 
### Android
- Android Studio (latest version)  
- Java 8+  
- Android SDK & Emulator / Physical Device  
- Firebase project  

---

## Installation & Setup


### Android

1. **Clone the repository:**
```
git clone <repository-url>
```

2. **Open in Android Studio**  
   - Select **Open an existing project**  
   - Choose the `ArtFlow-Android` directory

3. **Configure Firebase**  
   - Add your Android app to the Firebase project  
   - Download `google-services.json` and place it in `app/`  
   - Enable the following services:  
     - Firebase Authentication (Email/Password)  
     - Firebase Realtime Database or Firestore  
     - Firebase Storage

4. **Build & Run**  
   - Run the app on an emulator or a connected device

---
## Firebase Database Structure 
```
users
 └── userId
     ├── name
     ├── email
     └── role (artist/customer)

artworks
 └── artworkId
     ├── title
     ├── description
     ├── price
     ├── imageUrl
     └── artistName

orders
 └── orderId
     ├── artworkId
     ├── customerName
     ├── artistName
     ├── status
     └── quantity
```

---

## User Workflows

### Artist

- Sign up or login  
- Access dashboard  
- Add/manage artworks  
- Accept/reject customer orders  
- Update profile

### Customer

- Sign up or login  
- Browse artworks  
- View artwork details  
- Place orders  
- Track order status and history

---

## Key Classes (Android)

| Class                     | Purpose |
|----------------------------|---------|
| `LoginActivity`            | Handles user authentication (login/signup) |
| `ArtistDashboardActivity`  | Main dashboard for artists |
| `CustomerDashboardActivity`| Main dashboard for customers |
| `AddArtworkActivity`       | Add or edit artwork |
| `MyArtworksActivity`       | Manage artist's artworks |
| `CustomerMyOrdersActivity` | View customer's orders |
| `ProfileActivity`          | Manage user profile |
| `ArtworkAdapter`           | RecyclerView adapter for artworks |
| `OrderAdapter`             | RecyclerView adapter for orders |
| `FirebaseHelper`           | Handles Firebase operations (Auth, DB, Storage) |
| `User`                     | Android user data model |
| `Artwork`                  | Artwork data model |
| `Order`                    | Order data model |

---

## Troubleshooting (Android)

| Issue                        | Solution |
|-------------------------------|---------|
| App crashes on launch          | Verify `google-services.json` is in `app/` |
| Firebase permission denied     | Update Firebase Realtime Database / Firestore rules |
| Images not loading             | Check Firebase Storage rules and permissions |
| Gradle sync failed             | Update Gradle plugin and Android SDK versions |
| Authentication not working     | Ensure Firebase Authentication is enabled for Email/Password |


---

## License

Provided as-is for educational and commercial use.

---

## Support

For feature requests or issues, review the source code and inline comments within controllers, activities, and adapters.

---

**Last Updated**: January 2026  
**Version**: 1.0
