package com.kongjak.bustime.utilities

import android.util.Log
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.net.URLEncoder

object StationParser {

    fun parseXml(serviceID: String, latitude: String?, longitude: String?) {

        val urlBuilder =
            ("http://apis.data.go.kr/1613000/BusSttnInfoInqireService/getCrdntPrxmtSttnList"
                    + "?" + URLEncoder.encode("serviceKey", "UTF-8")
                    + "=" + serviceID
                    + "&" + URLEncoder.encode("gpsLati", "UTF-8")
                    + "=" + URLEncoder.encode(latitude, "UTF-8") /* 위도 */
                    + "&" + URLEncoder.encode("gpsLong", "UTF-8")
                    + "=" + URLEncoder.encode(longitude, "UTF-8")) /* 걍도도 */
        val url = URL(urlBuilder)

        Log.d("Test", url.toString())

        val stationArray = ArrayList<Station>(ArrayLists.stationArray)

        var inputStream: InputStream? = null
        try {
            inputStream = url.openStream()

            val xmlPullParserFactory: XmlPullParserFactory = XmlPullParserFactory.newInstance()
            val xmlPullParser: XmlPullParser = xmlPullParserFactory.newPullParser()

            xmlPullParser.setInput(inputStream, "UTF-8")

            var eventType: Int = xmlPullParser.eventType
            var tagName = ""
            var stationCityCode = ""
            var stationLatitude = ""
            var stationLongitude = ""
            var stationNodeID = ""
            var stationNodeName = ""

            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    tagName = xmlPullParser.name
                } else if (eventType == XmlPullParser.TEXT) {
                    val text: String = xmlPullParser.text

                    when (tagName) {
                        "citycode" -> {
                            stationCityCode = text
                        }
                        "gpslati" -> {
                            stationLatitude = text
                        }
                        "gpslong" -> {
                            stationLongitude = text
                        }
                        "nodeid" -> {
                            stationNodeID = text
                        }
                        "nodenm" -> {
                            stationNodeName = text
                        }
                    }
                } else if (eventType == XmlPullParser.END_TAG) {
                    if (xmlPullParser.name.equals("item")) {
                        Log.d("Test", stationNodeName)
                        stationArray.add(
                            Station(
                                stationCityCode,
                                stationLatitude,
                                stationLongitude,
                                stationNodeID,
                                stationNodeName
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

        ArrayLists.stationArray = stationArray
    }
}