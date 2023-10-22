package ie.djroche.datalogviewer.helpers

import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import ie.djroche.datalogviewer.main.MainApp

// use volly to read jason data from webserver
//ref https://google.github.io/volley/

val testURL ="https://www.google.ie"
var testReply : String  =""
val testTAG = "TestRequestTag"

fun sendTestRequest(app: MainApp){
// Request a string response from the provided URL.
    val stringRequest = StringRequest(
        Request.Method.GET, testURL,
        Response.Listener<String> { response ->
            // Display the first 500 characters of the response string.
            testReply = "Response is: ${response.substring(0, 500)}"
        },
        Response.ErrorListener { testReply = "That didn't work!" })

    stringRequest.tag=testTAG

  app.httpQueue.add(stringRequest)   // add to the app queue
}

fun cancelRequests(app: MainApp){
    app.httpQueue?.cancelAll(testTAG)
}