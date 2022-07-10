# Stock App

### This app uses modern android view toolkit for UI, Room database for local caching and retrofit to get data from server, developed with MVVM architecture.

## App Architecture

- <b>Data Layer</b> -> Data layer uses retrofit and room database to provide latest stocks values in CSV format and parse data and store it.
- <b>Domain Layer</b> -> It communicates between data layer and UI layer to provide only data that is used by UI layer(actually it provides abstraction between these two layers)
- <b>UI Layer</b> -> This layer show latest data in excellent UI.

## Library Used

- Jetpack-Compose
- [Jetpack-Compose-Navigation](https://github.com/raamcosta/compose-destinations)
- [Dagger-hilt](https://developer.android.com/training/dependency-injection/hilt-android)
- [Room](https://developer.android.com/training/data-storage/room)
- [Retrofit](https://square.github.io/retrofit/)
- [CSVParser](https://github.com/cygri/opencsv)
- [SwiperRefreshLayout](https://google.github.io/accompanist/swiperefresh/)
