package com.application.intercom.data.model.local


import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

class ClusterItemAdapter(
    val lat: Double,
    val lng: Double,
    val name: String,
    val dist: String,
    val distic:String,
    val photo: String,
    val bed: String,
    val bath: String,
    val ft: String,
    val price:String,
    val fieldDetailsId:String,
    var buildingId:String,
    var des:String
) : ClusterItem {
    override fun getPosition(): LatLng = LatLng(lat,lng)
    override fun getTitle(): String? = name
    override fun getSnippet(): String? = dist
}