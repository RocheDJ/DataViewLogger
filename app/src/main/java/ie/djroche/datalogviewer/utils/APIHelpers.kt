package ie.djroche.datalogviewer.utils

import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import ie.djroche.datalogviewer.main.MainApp
import ie.djroche.datalogviewer.models.SiteKPIModel
import ie.djroche.datalogviewer.models.SiteModel
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import timber.log.Timber

// use volly to read jason data from webserver
//ref https://google.github.io/volley/


var testReply : String  =""
val testTAG = "TestRequestTag"
val readQRTag ="QRRequestTag"
fun sendTestRequest(app: MainApp){
// Request a string response from the provided URL.
    val testURL = app.preferences.getString("TestEndPoint","https://www.google.com")
    val stringRequest = StringRequest(
        Request.Method.GET, testURL,
        Response.Listener<String> { response ->
            // Display the first 500 characters of the response string.
            testReply = "Response is: ${response.substring(0, 500)}"
        },
        Response.ErrorListener {
            testReply = "That didn't work!"
        })

    stringRequest.tag=testTAG

  app.httpQueue.add(stringRequest)   // add to the app queue
}

fun  sendReadQRDataRequest(app: MainApp,QRcode :String){
// Request a string response from the provided URL.
    var url = app.preferences.getString("Endpoint","http://127.0.0.1")
    url = url + "/read/id"
    val request: StringRequest =
        object : StringRequest(Request.Method.POST, url, object : Response.Listener<String?> {
            override fun onResponse(response: String?) {
                processGetSiteByQRResponse(response)
            }
        }, object : Response.ErrorListener {
            override fun onErrorResponse(error: VolleyError?) {
                // displaying toast message on response failure.
                Timber.i("Send ReadQRDataRequest  Error ", "error is " + error!!.message)
            }
        }) {
            override fun getParams(): Map<String, String>? {
                // below line we are creating a map for storing
                // our values in key and value pair.
                val params: MutableMap<String, String> = HashMap()
                // on below line we are passing our key
                // and value pair to our parameters.
                params["siteQR"] = QRcode
                return params
            }
        }

    request.tag=readQRTag
    app.httpQueue.add(request)   // add to the app queue

}
//--------------------------------------------------------------------------------------------------
fun processGetSiteByQRResponse(response: String?){
    try {
        // on below line we are extracting data from our json object
        // and passing our response to our json object.
        val jsonObject = JSONObject(response)
        val siteKpiData = (jsonObject.getString("data"))
        val siteKPIDataJASONArray = JSONArray(siteKpiData)
        var  mySite : SiteModel = SiteModel(data=mutableListOf<SiteKPIModel>())
        mySite.description = jsonObject.getString("description")
        mySite.qrcode = jsonObject.getString("qrcode")

        for (i in 0 until siteKPIDataJASONArray.length()) {
            val valueset = siteKPIDataJASONArray.getJSONObject(i)
            mySite.data.add(SiteKPIModel(id=valueset.getInt("id"),
                title=valueset.getString("title"),
                icon=valueset.getString("icon"),
                value=valueset.getDouble("value"),
                unit=valueset.getString("unit")).copy())
        }

        // our string to our text view.
        // ToDo: add return value event here
    } catch (e: JSONException) {
        e.printStackTrace()
    }

}




fun cancelRequests(app: MainApp){
    app.httpQueue?.cancelAll(testTAG)
    app.httpQueue?.cancelAll(readQRTag)
}


