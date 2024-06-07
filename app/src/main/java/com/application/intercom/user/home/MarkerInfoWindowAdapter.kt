package com.application.intercom.user.home

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.application.intercom.R
import com.application.intercom.data.model.local.ClusterItemAdapter
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker

class MarkerInfoWindowAdapter(
    context: Context,
    val item: ClusterItemAdapter?,
    val onPress: MarkerCLick
) : GoogleMap.InfoWindowAdapter {
    private val context: Context
    override fun getInfoContents(p0: Marker): View? {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val v: View = inflater.inflate(R.layout.info_window_layout, null)
        // val latLng = marker.position
        val tvStoreName = v.findViewById<View>(R.id.titleTextView) as TextView
        val tvdisName = v.findViewById<View>(R.id.snippetTextView) as TextView
        tvStoreName.text = item!!.title
        tvdisName.text = "${item!!.dist} km"
       val lay = v.findViewById<View>(R.id.layTest) as LinearLayout
        lay.setOnClickListener {
            Toast.makeText(context,"dshkfhkjds",Toast.LENGTH_LONG).show()
        }
        tvdisName.setOnClickListener {
            Toast.makeText(context,"dshkfhkjds",Toast.LENGTH_SHORT).show()
        }

        return v
    }


    /*override fun getInfoContents(marker: Marker): View {




        //tvLng.text = "Longitude:" + latLng.longitude

    }*/

    override fun getInfoWindow(marker: Marker): View? {

        return null
    }

    interface MarkerCLick {
        fun onMarkerClick(item: ClusterItemAdapter?)
    }

    init {
        this.context = context.getApplicationContext()
    }
}