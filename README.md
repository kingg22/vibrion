# 🎵 Vibrion App - Music Showcase — Android Demo App

![Kotlin](https://img.shields.io/badge/kotlin-%237F52FF.svg?style=flat-square&logo=kotlin&logoColor=white)
![Kotlin Multiplatform](https://img.shields.io/badge/Kotlin_Multiplatform-D3D5EC?logo=kotlin&logoColor=#9C40F6)
![Compose Multiplatform](https://img.shields.io/badge/Compose_Multiplatform-white.svg?logo=jetpackcompose&logoColor=%234E8BFF)
![Android](https://img.shields.io/badge/Android-3DDC84?style=flat-square&logo=android&logoColor=white)

![GitHub License](https://img.shields.io/github/license/bambanah/deemix)

> **Vibrion music app demo built as a technical showcase.**
> This project demonstrates my skills as an Android developer using modern Android architecture and tools.

---

## 📱 About the project

**Music Showcase** is an **Android demo application** that consumes the **public API of Deezer** to display music
content such as artists, albums, and tracks.

⚠️ **Important**

This project is **NOT** intended for production use.

- ❌ Not published on Google Play
- ❌ Not distributed to users
- ❌ No monetization
- ✅ Code showcase only
- ✅ Portfolio & technical demonstration

The purpose of this app is to demonstrate:
- Android / Compose Multiplatform development best practices
- Clean architecture and modular design
- Modern UI with Compose, animations, transitions
- API integration and asynchronous data handling

---

## ✨ Features

- 🔍 Music search (artists, albums, tracks)
- 🎶 Track listing and previews (API-dependent)
- 🧭 Modern navigation
- 🎨 Clean and modern UI using Compose
- ⚙️ API integration using HTTP client Ktor
- 🧱 Scalable architecture prepared for growth

---

## 🛠️ Tech Stack

- **Language:** Kotlin
- **UI:** Compose Multiplatform
- **Architecture:** Clean Architecture + MVVM
- **Concurrency:** Kotlin Coroutines & Flow
- **Networking:** HTTP client Ktor consuming [Deezer](https://www.deezer.com/mx/) Public
[API](https://developers.deezer.com/login?redirect=/api) through
[my client library](https://github.com/kingg22/deezer-client-kt)
- **State handling:** Immutable UI state
- **Dependency management:** Gradle Version Catalog

---

## 🧠 Architecture Overview

The project follows a **clean and layered architecture**:

```
data/
├── remote (API through client lib)
├── repository implementations
domain/
├── models
├── usecases
├── repository
ui/
├── screens
├── components
├── theme
```


Key goals:
- Clear separation of concerns
- Testability
- Easy maintenance and scalability

---

## 🖼️ Screenshots

> Screenshots below show the current state of the UI and are part of the technical demonstration.

![Home](assets/home.png)
![Search](assets/search.png)
![Album](assets/detail.png)
![Notification](assets/notification.png)
![Search 2](assets/search_list.png)

_(Screenshots will be updated as the UI evolves)_

---

## 🔑 API Disclaimer

This app uses the **public endpoints of Deezer** provided by [my client library](https://github.com/kingg22/deezer-client-kt).

- No private or authenticated endpoints are used
- All data belongs to Deezer and its respective owners
- This project is not affiliated with Deezer in any way

---

## 📜 Disclaimer

This repository is a **personal demo project** created exclusively for educational and portfolio purposes.

- Not affiliated with Deezer
- No commercial intent
- No guarantee of API availability or stability

---

## 🚀 Why this project exists

I built this app to:

- Showcase my Android development skills
- Apply modern Android patterns and tools
- Experiment with UI/UX decisions
- Serve as a real-world reference project

---

## 📄 License

This project is licensed under the **GNU 2.0 License**
See the `LICENSE` file for more details.
