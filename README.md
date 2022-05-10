# RabobankAssignment

A simple CSV parser assignment that downloads a CSV file and parses it to view its contents 

## Repository overview

### Dependencies

- [Compose](https://developer.android.com/jetpack/compose) for UI
- [Hilt](https://developer.android.com/training/dependency-injection/hilt-android) for DI 
- [Koil compose](https://coil-kt.github.io/coil/compose/) for image loading

### Parsing strategy 

- Fields with embedded commas or double-quote characters must be quoted
- Each of the embedded double-quote characters must be represented by a pair of double-quote characters
- Leading and trailing spaces and tabs are trimmed (ignored)

### Parsing configuration

[CsvSourceConfig](https://github.com/ChristoferAlexander/RabobankAssignment/blob/master/app/src/main/java/com/example/rabobankassignment/parser/config.kt) can be used to set custom parsing options like the separator char, quote char, input / output date format and if to parse with header or not.

## Architecture overview

### Navigation

[NavRoute](https://github.com/ChristoferAlexander/RabobankAssignment/blob/master/app/src/main/java/com/example/rabobankassignment/ui/nav/NavRoute.kt) is used as a wrapper class around a compose navigation and is responsible for providing the route (supports arguments), the composable content, the ViewModel instance via the [ViewModelModule](https://github.com/ChristoferAlexander/RabobankAssignment/blob/master/app/src/main/java/com/example/rabobankassignment/di/ViewModelModule.kt) and finally handling navigation by observing the [NavigationState](https://github.com/ChristoferAlexander/RabobankAssignment/blob/master/app/src/main/java/com/example/rabobankassignment/ui/nav/NavigationState.kt).

There are two NavRoutes that the app can navigate to, [Download](https://github.com/ChristoferAlexander/RabobankAssignment/blob/master/app/src/main/java/com/example/rabobankassignment/ui/compose/Download.kt) and [Display](https://github.com/ChristoferAlexander/RabobankAssignment/blob/master/app/src/main/java/com/example/rabobankassignment/ui/compose/Display.kt).

### API

[CsvApi](https://github.com/ChristoferAlexander/RabobankAssignment/blob/master/app/src/main/java/com/example/rabobankassignment/api/CsvApi.kt) is the top level interface that handles API calls for the Application. Under it hides a generalization of handling the API responses using a [CallAdapter factory](https://github.com/ChristoferAlexander/RabobankAssignment/blob/master/app/src/main/java/com/example/rabobankassignment/api/utils.kt#L11) for Retrofit that wraps the Response to an [ApiResult](https://github.com/ChristoferAlexander/RabobankAssignment/blob/master/app/src/main/java/com/example/rabobankassignment/api/model.kt).  
  
[CsvApi](https://github.com/ChristoferAlexander/RabobankAssignment/blob/master/app/src/main/java/com/example/rabobankassignment/api/CsvApi.kt) is itself injected into the [CsvRepoImp](https://github.com/ChristoferAlexander/RabobankAssignment/blob/master/app/src/main/java/com/example/rabobankassignment/repository/CsvRepoImp.kt) via the [ApiModule](https://github.com/ChristoferAlexander/RabobankAssignment/blob/master/app/src/main/java/com/example/rabobankassignment/di/ApiModule.kt) which itself is provided as a singleton to the [DownloadViewModel](https://github.com/ChristoferAlexander/RabobankAssignment/blob/master/app/src/main/java/com/example/rabobankassignment/viewModel/DownloadViewModel.kt).

## Limitations

- although the parser supports parsing CSVs without headers and non fixed amount of columns due to UI limitations with compose Grids the APP can only display properly only CVSs with a header line
- new lines in records are not supported

## Not implemented

- UI tests (issue when VM is injected directly to the composable and is not provided with the activity)
- Horizontal navigation for Grid in case of large amount of columns (requires custom compose implementation as Grid API is not mature enough) 
- Empty CSV file error 

## Notes

You can do a back action and redownload the CSV file.


