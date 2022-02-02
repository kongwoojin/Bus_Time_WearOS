package com.kongjak.bustime.utilities

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kongjak.bustime.R
import com.kongjak.bustime.databinding.BusArriveListBinding
import java.util.*

class BusListAdapter(private val BusArrayList: ArrayList<BusList>) :
    RecyclerView.Adapter<BusListAdapter.BusViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.bus_arrive_list, parent, false)

        return BusViewHolder(BusArriveListBinding.bind(view))
    }

    override fun onBindViewHolder(holder: BusViewHolder, position: Int) {
        val data = BusArrayList[position]

        val context = holder.binding.root.context.resources

        holder.binding.apply {
            prevStation.text = context.getString(R.string.route_remain).format(data.prevStation)
            eta.text = context.getString(R.string.route_eta).format(data.etaSecond.toInt() / 60)
            busNumber.text = context.getString(R.string.route_number).format(data.routeNumber)
        }
    }

    override fun getItemCount(): Int {
        return BusArrayList.size
    }

    class BusViewHolder(val binding: BusArriveListBinding) :
        RecyclerView.ViewHolder(binding.root) {}
}
