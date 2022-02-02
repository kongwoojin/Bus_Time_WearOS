package com.kongjak.bustime.utilities

object ArrayLists {

    private var stationArray = ArrayList<Station>()
    private var busListArray = ArrayList<BusList>()

    fun getStationArray(): ArrayList<Station> {
        return stationArray
    }

    fun setStationArray(arrayList: ArrayList<Station>) {
        stationArray = arrayList
    }

    fun getBusListArray(): ArrayList<BusList> {
        return busListArray
    }

    fun setBusListArray(arrayList: ArrayList<BusList>) {
        busListArray = arrayList
    }

    fun destroyBusListArray() {
        busListArray.clear()
    }

    fun destroyStationListArray() {
        stationArray.clear()
    }

    fun destroyArrayLists() {
        destroyBusListArray()
        destroyStationListArray()
    }

}