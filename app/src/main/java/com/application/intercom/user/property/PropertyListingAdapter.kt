package com.application.intercom.user.property

import android.content.Context
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.R
import com.application.intercom.data.model.remote.PropertyList.PropertyLisRes
import com.application.intercom.databinding.ItemPropertiesParkingBinding
import com.application.intercom.utils.CommonUtil.DELAY_MS
import com.application.intercom.utils.CommonUtil.PERIOD_MS
import java.util.*

class PropertyListingAdapter(
    private val context: Context,
    val list: ArrayList<PropertyLisRes.Data>,
    val onPress: ClickProperty
) : RecyclerView.Adapter<PropertyListingAdapter.MyViewHolder>() {


    private val viewPool = RecyclerView.RecycledViewPool()

    inner class MyViewHolder(val mView: ItemPropertiesParkingBinding) :
        RecyclerView.ViewHolder(mView.root) {
        var notificationsAdapterItem: UserPropertyViewPagerAdapter

        val myRunnable: Runnable

        init {
            notificationsAdapterItem = UserPropertyViewPagerAdapter(
                mView.root.context,
                arrayListOf()
            )
            mView.viewPager1.adapter = notificationsAdapterItem
            Log.d("notificationsAdapterItem", " here ${notificationsAdapterItem.list}")

            mView.tabLayout1.setupWithViewPager(mView.viewPager1, true)
            myRunnable = Runnable {
                if (currentPos == (notificationsAdapterItem.list.size) - 1)
                    currentPos = 0
                else currentPos++
                if (mView.viewPager1 != null) {
                    mView.viewPager1.setCurrentItem(currentPos, true)
                }

            }

            itemView.setOnClickListener {
                val item = list[absoluteAdapterPosition]
                val disee = "${item.district} , ${item.division}"
                onPress.onCLickProperty(
                    list[absoluteAdapterPosition],
                    absoluteAdapterPosition,
                    if (!item.buildingName.isNullOrEmpty()) {
                        item.buildingName
                    } else {
                        item.title
                    },
                    if (item.district != null) {
                        disee
                    } else {
                        item.address
                    },
                    if (item.flatInfo != null) {
                        item.flatInfo!!.price.toString()
                    } else {
                        item.price.toString()
                    },
                    if (item.flatDetail != null) {
                        item.flatDetail!!.bedroom.toString()
                    } else {
                        item.bedroom.toString()
                    },
                    if (item.flatDetail != null) {
                        item.flatDetail!!.sqft.toString()
                    } else {
                        item.sqft.toString()
                    },
                    if (item.flatDetail != null) {
                        item.flatDetail!!.bathroom.toString()
                    } else {
                        item.bathroom.toString()
                    },
                    /*dis2.toString(),*/"0.0",
                    if (item.flatInfo != null) {
                        item.flatInfo!!.description
                    } else {
                        item.description
                    },
                    if (item.flatDetail != null) {
                        item.flatDetail!!._id
                    } else {
                        item._id
                    },
                    if (item.flatDetail != null) {
                        item.flatInfo!!.buildingId
                    } else {
                        item._id
                    },
                    if (item.flatInfo != null) {
                        item.flatInfo!!.photo
                    } else if (item.photos != null) {
                        item.photos
                    } else arrayListOf(),
                    if (item.flatInfo != null) {
                        item.flatInfo!!.amentities
                    } else {
                        item.amentities
                    },
                    item.latitude,
                    item.longitude,
                    item.addedBy,
                    if (item.flatDetail == null) {
                        item.flatStatus
                    } else {
                        item.flatDetail!!.flatStatus
                    },
                    item.propertyType ?: ""

                )
//            context.startActivity(
//                Intent(
//                    context,
//                    CompleteParkingToLetDetailsActivity::class.java
//                ).putExtra("from", key).putExtra("sendList", sendList)
//            )


            }

            mView.favImg.setOnClickListener {
                val item = list[absoluteAdapterPosition]
                updatelist(absoluteAdapterPosition)
                if (item.flatInfo != null) {
                    onPress.selectFav(item.flatInfo!!.flatId)
                } else {
                    onPress.selectFav(item._id)
                }

            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemPropertiesParkingBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    var currentPos = 0
    val handler = Handler()
    var test = java.util.ArrayList<String>()
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.mView.view.visibility = View.INVISIBLE
        val item = list[position]
        if (item.flatInfo != null) {
            test = item.flatInfo!!.photo
        } else if (item.photos != null) {
            test = item.photos
        } else {
            arrayListOf<String>()
        }
//        val imageList = arrayListOf<String>()
//        imageList.addAll(item.flatInfo?.photo ?: item.photos ?: arrayListOf())
        holder.notificationsAdapterItem.list = test
        Log.d("onBindViewHolder", "imageList: $test")
        holder.notificationsAdapterItem.notifyDataSetChanged()
        holder.mView.tvDate.visibility = View.GONE
        holder.mView.textView143.visibility = View.GONE

        Timer().schedule(object : TimerTask() {
            override fun run() {
                handler.post(holder.myRunnable)
            }
        }, DELAY_MS, PERIOD_MS)

        if (item.addedBy != null) {
            holder.mView.tvPropertyName.text = item.buildingName
            holder.mView.tvLocation.text = item.address
            if (item.flatInfo != null) {
                holder.mView.tvPropertyPrice.text = "৳${item.flatInfo!!.price}"
            } else {
                holder.mView.tvPropertyPrice.text = "৳${item.price}"
            }
            holder.mView.tvDistance.visibility = View.INVISIBLE
            if (item.flatDetail != null) {
                holder.mView.tvFit.text = "${item.flatDetail!!.sqft} ${context.getString(R.string.ft)}"
            } else {
                holder.mView.tvFit.text = "${item.sqft} ${context.getString(R.string.ft)}"
            }
            holder.mView.tvBedroom.text = "${item.bedroom} ${context.getString(R.string.bedroom)}"
            holder.mView.tvBathroom.text = "${item.bathroom} ${context.getString(R.string.bathrooms)}"
            holder.mView.tvPropertyType.text = "${item.flatStatus}"
            holder.mView.tvPropertyType1.text = "${item.propertyType ?: ""}"
        } else {
            // val disee = "${item.district} , ${item.division}"
//        holder.mView.ivProperty.loadImagesWithGlideExt(item.flatInfo.photo.get(0))
            holder.mView.tvPropertyName.text = item.buildingName
            holder.mView.tvLocation.text = "${item.district} , ${item.division}"
            holder.mView.tvPropertyPrice.text = "৳${item.flatInfo!!.price}"
            /*val dis: Double = item.distance / 1000
            val dis2: Double = String.format("%.1f", dis).toDouble()
            holder.mView.tvDistance.text = "${dis2} Km"*/
            holder.mView.tvFit.text = "${item.flatDetail!!.sqft} ${context.getString(R.string.ft)}"
            holder.mView.tvBedroom.text = "${item.flatDetail!!.bedroom} ${context.getString(R.string.bedroom)}"
            holder.mView.tvBathroom.text = "${item.flatDetail!!.bathroom} ${context.getString(R.string.bathrooms)}"
            holder.mView.tvPropertyType.text = "${item.flatDetail!!.flatStatus}"
            holder.mView.tvPropertyType1.text = "${item.propertyType ?: ""}"
        }


        if (item.isWishList) {
            holder.mView.favImg.setImageResource(R.drawable.fav_active_icon)
        } else {
            holder.mView.favImg.setImageResource(R.drawable.unfav)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private fun updatelist(pos: Int) {
        for (i in list.indices) {
            list.get(i).isWishList = i == pos
        }
        notifyDataSetChanged()
    }

    fun notifiyData(list: ArrayList<PropertyLisRes.Data>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()

    }

    interface ClickProperty {
        fun onCLickProperty(
            msg: PropertyLisRes.Data,
            position: Int,
            buildingName: String,
            dis: String,
            price: String,
            bedRoom: String,
            ft: String,
            bathRoom: String,
            disT2: String,
            discrption: String,
            flatDetailsId: String,
            buildingId: String,
            photoList: ArrayList<String>,
            amList: ArrayList<PropertyLisRes.Data.Amentity>,
            lati: Double,
            longi: Double,
            addedBy: String?,
            propertyType: String,
            property_Resi: String?

        )

        fun selectFav(propertyId: String)
    }
}