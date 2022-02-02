package com.kongjak.bustime.activities

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.wear.widget.WearableLinearLayoutManager
import com.kongjak.bustime.R
import com.kongjak.bustime.databinding.ActivityBusArriveBinding
import com.kongjak.bustime.utilities.*

class BusArriveActivity : Activity() {

    private lateinit var binding: ActivityBusArriveBinding
    private lateinit var busListArray: ArrayList<BusList>
    private lateinit var myAdapter: BusListAdapter
    private var stationName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBusArriveBinding.inflate(layoutInflater)
        setContentView(binding.root)

        stationName = intent.getStringExtra("stationName")!!
        val stationID = intent.getStringExtra("stationID")
        val cityCode = intent.getStringExtra("cityCode")

        ArrayLists.destroyBusListArray()

        if (cityCode != null && stationID != null) {
            getBusListData(stationID, cityCode)
        }

    }

    private fun getBusListData(stationID: String, cityCode: String) {
        Thread {
            BusListParser.parseXml(
                getString(R.string.api_key),
                stationID,
                cityCode
            )
            runOnUiThread {
                initView()
            }
        }.start()
    }

    private fun initView() {
        busListArray = ArrayList(ArrayLists.getBusListArray())
        myAdapter = BusListAdapter(busListArray)
        if (busListArray.isEmpty()) {
            with(binding) {
                recyclerView.visibility = View.GONE
                emptyStation.visibility = View.VISIBLE
            }
        }
        binding.recyclerView.apply {
            isEdgeItemsCenteringEnabled = true
            bezelFraction = 0.5f
            scrollDegreesPerScreen = 90f
            layoutManager =
                WearableLinearLayoutManager(
                    this@BusArriveActivity,
                    MainActivity.CustomScrollingLayoutCallback()
                )
            adapter = myAdapter
            val snapHelper = PagerSnapHelper()
            snapHelper.attachToRecyclerView(binding.recyclerView)
            binding.progressLayout.visibility = View.GONE
            if (binding.busArriveTitleS != null) {
                binding.busArriveTitleS!!.text = stationName
            } else if (binding.busArriveTitleR != null) {
                binding.busArriveTitleR!!.text = stationName
            }


        }
    }
}