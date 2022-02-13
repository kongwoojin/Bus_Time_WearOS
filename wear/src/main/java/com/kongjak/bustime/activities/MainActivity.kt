package com.kongjak.bustime.activities

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.wear.widget.WearableLinearLayoutManager
import com.kongjak.bustime.R
import com.kongjak.bustime.databinding.ActivityMainBinding
import com.kongjak.bustime.utilities.*

class MainActivity : Activity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var stationArray: ArrayList<Station>
    private lateinit var myAdapter: StationAdapter
    private val gpsLocation = GPSLocation()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestPermissions()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        gpsLocation.initLocation(this)

        getStationData()
    }

    private fun getStationData() {
        val location = gpsLocation.getLocation()
        Thread {
            if (location != null) {
                StationParser.parseXml(
                    getString(R.string.api_key),
                    location.latitude.toString(),
                    location.longitude.toString()
                )
                runOnUiThread {
                    initView()
                }
            } else {
                Thread.sleep(2000)
                getStationData()
            }
        }.start()
    }

    private fun requestPermissions() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), 1000
            )
            return
        }
    }

    private fun initView() {
        stationArray = ArrayList(ArrayLists.stationArray)
        myAdapter = StationAdapter(stationArray)
        if (stationArray.isEmpty()) {
            with(binding) {
                recyclerView.visibility = View.GONE
                emptyStation.visibility = View.VISIBLE
            }
        }

        binding.recyclerView.apply {
            bezelFraction = 0.5f
            scrollDegreesPerScreen = 90f
            setHasFixedSize(true)
            layoutManager =
                WearableLinearLayoutManager(this@MainActivity, CustomScrollingLayoutCallback())
            adapter = myAdapter
        }
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding.recyclerView)
        binding.progressLayout.visibility = View.GONE
    }


    class CustomScrollingLayoutCallback : WearableLinearLayoutManager.LayoutCallback() {

        override fun onLayoutFinished(child: View, parent: RecyclerView) {
            child.apply {
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ArrayLists.busListArray.clear()
        ArrayLists.stationArray.clear()
    }
}