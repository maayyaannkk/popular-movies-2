# popular-movies-2
Project 2 for udacity

In this second and final stage, I have added additional functionality to the app built in Stage 1.

<b>Added more information to movie details view:</b>
<ul>
<li>Allow users to view and play trailers ( either in the youtube app or a web browser).</li>
<li>Allow users to read reviews of a selected movie.</li>
<li>Also allow users to mark a movie as a favorite in the details view by tapping a button. 
This is for a local movies collection that is maintained on device and does not require an API request*.</li>
<li>Modified the existing sorting criteria for the main view to include an additional pivot to show favorites collection.</li>
<li>Modified the existing layouts to fully utilize the tablet real estate.</li>
</ul>
##How to Install
###Requirements

Java 7/8
Latest version of Android SDK and Android Build Tools
API Key

The app uses themoviedb.org API to get movie information and posters. You must provide your own API key in order to build the app.

Just put your API key into lib/JSONParser.java file:

`public static final String API_KEY = ""; //Input api key in this string`


###Building

You can build the app with Android Studio or with ./gradlew assembleDebug command.

<img src="https://github.com/maayyaannkk/popular-movies-2/blob/master/Screenshot19.png" height="520" width="290">
<img src="https://github.com/maayyaannkk/popular-movies-2/blob/master/Screenshot20.png" height="290" width="520">
<img src="https://github.com/maayyaannkk/popular-movies-2/blob/master/Screenshot21.png" height="520" width="290">
<img src="https://github.com/maayyaannkk/popular-movies-2/blob/master/Screenshot22.png" height="520" width="290">
<img src="https://github.com/maayyaannkk/popular-movies-2/blob/master/Screenshot23.png" >
<img src="https://github.com/maayyaannkk/popular-movies-2/blob/master/Screenshot24.png" >
