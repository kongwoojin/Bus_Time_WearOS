package com.kongjak.bustime.utilities

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kongjak.bustime.R
import com.kongjak.bustime.activities.BusArriveActivity
import com.kongjak.bustime.databinding.StationListBinding
import java.util.*

class StationAdapter(private val StationArrayList: ArrayList<Station>) :
    RecyclerView.Adapter<StationAdapter.StationViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.station_list, parent, false)

        return StationViewHolder(StationListBinding.bind(view))
    }

    override fun onBindViewHolder(holder: StationViewHolder, position: Int) {
        val data = StationArrayList[position]
        holder.binding.stationName.text = data.name

        holder.binding.root.setOnClickListener {
            val intent = Intent(it.context, BusArriveActivity::class.java)
            intent.putExtra("stationName", data.name)
            intent.putExtra("stationID", data.id)
            intent.putExtra("cityCode", data.citycode)
            it.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return StationArrayList.size
    }

    class StationViewHolder(val binding: StationListBinding) :
        RecyclerView.ViewHolder(binding.root)
}
