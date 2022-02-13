package com.kongjak.bustime.utilities

import android.util.Log
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.net.URLEncoder

object BusListParser {

    fun parseXml(serviceID: String, stationID: String?, cityCode: String?) {

        Log.d("Test", cityCode!!)

        val urlBuilder =
            ("http://apis.data.go.kr/1613000/ArvlInfoInqireService/getSttnAcctoArvlPrearngeInfoList"
                    + "?" + URLEncoder.encode("serviceKey", "UTF-8")
                    + "=" + serviceID
                    + "&" + URLEncoder.encode("nodeId", "UTF-8")
                    + "=" + URLEncoder.encode(stationID, "UTF-8") /* 위도 */
                    + "&" + URLEncoder.encode("cityCode", "UTF-8")
                    + "=" + URLEncoder.encode(cityCode, "UTF-8")) /* 걍도도 */
        val url = URL(urlBuilder)

        Log.d("Test", url.toString())

        val busListArray = ArrayList<BusList>(ArrayLists.busListArray)

        var inputStream: InputStream? = null
        try {
            inputStream = url.openStream()

            val xmlPullParserFactory: XmlPullParserFactory = XmlPullParserFactory.newInstance()
            val xmlPullParser: XmlPullParser = xmlPullParserFactory.newPullParser()

            xmlPullParser.setInput(inputStream, "UTF-8")

            var eventType: Int = xmlPullParser.eventType
            var tagName = ""
            var prevStation = ""
            var etaSecond = ""
            var routeID = ""
            var routeNumber = ""

            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    tagName = xmlPullParser.name
                } else if (eventType == XmlPullParser.TEXT) {
                    val text: String = xmlPullParser.text

                    when (tagName) {
                        "arrprevstationcnt" -> {
                            prevStation = text
                        }
                        "arrtime" -> {
                            etaSecond = text
                        }
                        "routeid" -> {
                            routeID = text
                        }
                        "routeno" -> {
                            routeNumber = text
                        }
                    }
                } else if (eventType == XmlPullParser.END_TAG) {
                    if (xmlPullParser.name.equals("item")) {
                        Log.d("Test", routeID)
                        busListArray.add(
                            BusList(
                                prevStation,
                                etaSecond,
                                routeID,
                                routeNumber
                            )
                        )
                    }
                }
                eventType = xmlPullParser.next()
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }


        try {
            inputStream?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        ArrayLists.busListArray = busListArray
    }
}