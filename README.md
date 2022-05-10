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

### Limitations

- although the parser supports parsing CSVs without headers and non fixed amount of columns due to UI limitations with compose Grids the APP can only display properly only CVSs with a header line
- new lines in records are not supported

## Architecture overview

### Navigation

[NavRoute](https://github.com/ChristoferAlexander/RabobankAssignment/blob/master/app/src/main/java/com/example/rabobankassignment/ui/nav/NavRoute.kt) is used as a wrapper class around a compose navigation and is responsible for providing the route (supports arguments), the composable content, the ViewModel instance via the [ViewModelModule](https://github.com/ChristoferAlexander/RabobankAssignment/blob/master/app/src/main/java/com/example/rabobankassignment/di/ViewModelModule.kt) and finally handling navigation by observing the [NavigationState](https://github.com/ChristoferAlexander/RabobankAssignment/blob/master/app/src/main/java/com/example/rabobankassignment/ui/nav/NavigationState.kt).

There are two NavRoutes that the app can navigate to, [Download](https://github.com/ChristoferAlexander/RabobankAssignment/blob/master/app/src/main/java/com/example/rabobankassignment/ui/compose/Download.kt) and [Display](https://github.com/ChristoferAlexander/RabobankAssignment/blob/master/app/src/main/java/com/example/rabobankassignment/ui/compose/Display.kt).

### API

[CsvApi](https://github.com/ChristoferAlexander/RabobankAssignment/blob/master/app/src/main/java/com/example/rabobankassignment/api/CsvApi.kt) is the top level interface that handles API calls for the Application. Under it hides a generalization of handling the API responses using a [CallAdapter factory](https://github.com/ChristoferAlexander/RabobankAssignment/blob/master/app/src/main/java/com/example/rabobankassignment/api/utils.kt#L11) for Retrofit that wraps the Response to an [ApiResult](https://github.com/ChristoferAlexander/RabobankAssignment/blob/master/app/src/main/java/com/example/rabobankassignment/api/model.kt).  
  
[CsvApi](https://github.com/ChristoferAlexander/RabobankAssignment/blob/master/app/src/main/java/com/example/rabobankassignment/api/CsvApi.kt) is itself injected into the [CsvRepoImp](https://github.com/ChristoferAlexander/RabobankAssignment/blob/master/app/src/main/java/com/example/rabobankassignment/repository/CsvRepoImp.kt) via the [ApiModule](https://github.com/ChristoferAlexander/RabobankAssignment/blob/master/app/src/main/java/com/example/rabobankassignment/di/ApiModule.kt) which itself is provided as a singleton to the [DownloadViewModel](https://github.com/ChristoferAlexander/RabobankAssignment/blob/master/app/src/main/java/com/example/rabobankassignment/viewModel/DownloadViewModel.kt).

## Not implemented

- UI tests (issue when VM is injected directly to the composable and is not provided with the activity)
- Horizontal scroll for Grid in case of large amount of columns 
- Empty CSV file error 

## Notes

You can do a back action and redownload the CSV file.

## Disclaimer

I spent four days on the project and there was a lot of ground to cover regarding native Android development as I was more focused on the KMM Android side of development for the past year. I also used tech stacks I was not very familiar with which are still not stable such as Compose with Hilt resulting in a steep learning curve which limited my actual development time for business logic and UI polishing. 

The main focus was around making the project as much extendable as possible in the early stage of development so new features can be easily added without a lot of boilerplate code 

I followed the latest best practices such as:
- Composables follow the [Unidirectional data flow](https://developer.android.com/jetpack/compose/architecture#udf) pattern to be easily reusable and previewable
- Dependencies should be easily switchable with the use of Hilt DI framework for testing
- Scoped Dependencies on the VM life cycle
- VM aware Navigation component wrapper
- API calls Response wrapper
- VM regulated UI state
- Single Activity Application

Issues encountered:
- I run into the issue of not being able to inject the VM directly to the composable resulting in not being able to write UI tests. This approach of making layouts is quite recent and there were no examples I could find for my exact use case. Having the activity provide the VM to the compose layout could possibly solve the issue but would alter the architecture I was aiming for, which was one VM per compose layout and a single activity. But you can still find some basic UI testing setup that includes a MockServer and Hilt TestModules
- I run into issues with @Parcelize sealed classes that could not be passed as Nav arguments. The solution was within providing a custom GSON adapter for sealed classes or switching to a pure Serialization API and passing the args as serializable / desiarialiable Strings. Taking that road took more time as expected and for the sake of delivering a working product I took the decision to include the type as enum and the value as a String as seen in [CsvRecordValue](https://github.com/ChristoferAlexander/RabobankAssignment/blob/30b124381c80b445df0d2f8f714b1e6cecf3ad90/app/src/main/java/com/example/rabobankassignment/parser/model.kt#L16). Having more time I would include a custom sealed class serializer / deserializer
- I started with a line by line approach for the parser but later I realized that a char by char approach would be more appropriate (and more complicated) as it would cover easier the '\n' case in the record. Although my approach could be altered to a recursive algorithm that tries to append the next line until a line can be parsed successfully, if given enough time I would swap to a char by char implementation
- Compose [lazyVerticalGrid](https://developer.android.com/jetpack/compose/lists#grids) has a lot of limitations and is still an experimental API. Making a proper layout that displays CSVs of all kinds would require a complete custom approach and I did not have the time for that. In an ideal scenario that would be my go to solution and not a best effort approach for VerticalGrid around its limitations and issues
- Initially I wanted to include parsing to a User model and chose reflection for that. I had no real experience with reflection and although quite interesting it was not a wise decision for a time limited project like this so I dropped it (almost a whole day was discarded unfortunately as a result) 

 

