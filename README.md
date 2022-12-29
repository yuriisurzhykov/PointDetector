<h1 align="center">PointDetector</h1>
<p align="center">Point Detector - is an application that may help you to find closest places to your current location. The app built using modern tech-stack tools such as Room, Firebase, Dagger 2 and MVVM for the architechture.</p>

The application is now available on Google Play Market! <br>
[<img alt="The application is now available in play market!" width="190px" src="https://github.com/mhemmings/play-store-button/blob/master/play-store-button.svg" />](https://play.google.com/store/apps/details?id=com.yuriisurzhykov.pointdetector)

# Tech-stack description
* [Room](https://developer.android.com/jetpack/androidx/releases/room) - is using to cache data from remote source of data and to store user created data.
* [Dagger 2](https://developer.android.com/training/dependency-injection/hilt-android) - is using to manage the dependencies for each component in project.
* [Firebase RealtimeDatabase](https://firebase.google.com/docs/database) - is using as a remote data store; in application used only to cache localy data about food banks points.
* [Kotlin Coroutines](https://developer.android.com/kotlin/coroutines) - is using for asynchronous jobs.
* [GitHub Actions](https://github.com/features/actions) - is using for CI/CD

# Application architecture
* Clean + MVVM artchitechture </br>
You can see that project contains core, data, domain and presentation layers. Core and data layers are independent from other layers. </br>
<img alt="The application is now available in play market!" width="200px" src="https://user-images.githubusercontent.com/44873047/209905238-b4dcc3be-84c5-4f2e-8d18-97fe94fcfa46.png"/>
And general diagram of layers is the next </br>
<img alt="The application is now available in play market!" width="200px" src="https://user-images.githubusercontent.com/44873047/209905834-538bf8e3-931d-469d-8a3a-69d2071275f0.jpg"/>

# Example of using
[Example of using v1.0.1](https://user-images.githubusercontent.com/44873047/209906415-040cec0c-3818-429b-82ae-5626a16f12f8.webm)

