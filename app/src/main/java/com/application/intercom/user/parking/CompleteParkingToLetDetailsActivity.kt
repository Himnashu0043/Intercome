package com.application.intercom.user.parking

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Html
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.application.intercom.MainActivity
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.local.PropertyListPostModel.PropertyListUserPostModel
import com.application.intercom.data.model.local.UserViewContact.UserViewContactPostModel
import com.application.intercom.data.model.local.gateKeeper.AddRegularVisitorEntryPostModel
import com.application.intercom.data.model.local.localModel.LocalAmentitiesModel
import com.application.intercom.data.model.local.localModel.MapDataSendModel
import com.application.intercom.data.model.remote.PropertyList.PropertyLisRes
import com.application.intercom.data.model.remote.UserParkingListRes.UserParkingList
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.UserHomeRepository
import com.application.intercom.databinding.ActivityCompleteParkingToLetDetailsBinding
import com.application.intercom.helper.GPSService
import com.application.intercom.helper.setNewFormatDate
import com.application.intercom.owner.adapter.OwnerSimilarPropertyAdapter
import com.application.intercom.owner.adapter.ShowImgAdapter
import com.application.intercom.user.home.UserHomeFactory
import com.application.intercom.user.home.UserHomeViewModel
import com.application.intercom.user.newflow.NewSubUserActivity
import com.application.intercom.user.property.AmenitiesAdapter
import com.application.intercom.user.property.PropertyDetailsActivity
import com.application.intercom.user.property.SlidingImagesAdapter
import com.application.intercom.user.subscription.GetIntercomPremiumActivity
import com.application.intercom.utils.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*

class CompleteParkingToLetDetailsActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityCompleteParkingToLetDetailsBinding
    private var mAdapter: AmenitiesAdapter? = null
    private lateinit var imagesArray: ArrayList<String>
    private lateinit var handler: Handler
    private var currentPage = 0
    private lateinit var slidingImageDots: Array<ImageView?>
    private var slidingDotsCount = 0
    private var from: String = ""
    private var sendList = ArrayList<PropertyLisRes.Data>()
    private var propertyList = ArrayList<PropertyLisRes.Data>()
    private var parking_sendList = ArrayList<UserParkingList.Data>()
    private var id: String = ""
    private lateinit var viewModel: UserHomeViewModel

    private var buildingId: String = ""
    private var flatId: String = ""
    private var parkingId: String = ""
    private var building_Name: String = ""
    private var get_dis: String = ""
    private var get_price: String = ""
    private var get_bedRoom: String = ""
    private var get_ft: String = ""
    private var get_bathRoom: String = ""
    private var get_url = java.util.ArrayList<String>()
    private var get_discription: String = ""
    private var get_diste: String = ""
    private var get_filedDetailsId: String = ""
    private var get_buildingId: String = ""
    private var get_ParkingId: String = ""
    private var get_property_Resi: String = ""
    private var get_propertyType: String = ""
    private var addedBy: String = ""
    private var get_lati: Double = 0.0
    private var get_longi: Double = 0.0
    private var getLatitude: Double = 0.0
    private var getLogitude: Double = 0.0
    private lateinit var mMap: GoogleMap
    private var getamem_List = java.util.ArrayList<PropertyLisRes.Data.Amentity>()
    private var availableContacts: Int = 0
    private var totalContacts: Int = 0
    private val slidingCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            for (i in 0 until slidingDotsCount) {
                slidingImageDots[i]?.setImageDrawable(
                    ContextCompat.getDrawable(
                        this@CompleteParkingToLetDetailsActivity, R.drawable.default_dot
                    )
                )
            }

            slidingImageDots[position]?.setImageDrawable(
                ContextCompat.getDrawable(
                    this@CompleteParkingToLetDetailsActivity, R.drawable.selected_dot
                )
            )
        }
    }

    private fun setPropertyAdapter() {
        /*if (from.equals("user_parking")) {
            binding.rvAmenities.layoutManager = GridLayoutManager(this, 4)
            mAdapter = AmenitiesAdapter(this, list, from, sendList)
            binding.rvAmenities.adapter = mAdapter
        } else {*/
        binding.rvAmenities.layoutManager = GridLayoutManager(this, 4)
        mAdapter = AmenitiesAdapter(this, getamem_List, from)
        binding.rvAmenities.adapter = mAdapter
        // }


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCompleteParkingToLetDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        CommonUtil.themeSet(this, window)
        var subscription = prefs.getBoolean(SessionConstants.SUBSCRIPTION)
        println("-----subb$subscription")


        from = intent.getStringExtra("from").toString()
        availableContacts = prefs.getInt(SessionConstants.AVAILABLE_CONTACTS).toInt()
        totalContacts = prefs.getInt(SessionConstants.TOTALS_CONTACTS).toInt()
        println("---availableContacts$availableContacts")
        println("---totalContacts${prefs.getInt(SessionConstants.TOTALS_CONTACTS)}")
        println("----from$from")
        if (from.equals("tenant_parking")) {
            binding.layoutDistance.visibility = View.GONE
            binding.layoutBedroom.visibility = View.GONE
            binding.layoutBathroom.visibility = View.GONE
            binding.tvDate.visibility = View.GONE
            /* binding.tvMembership.visibility = View.INVISIBLE
             binding.tvPriceNew.visibility = View.VISIBLE
             binding.tvPropertyPrice.visibility = View.GONE*/
            ///
            parking_sendList =
                intent.getSerializableExtra("parking_send_list") as ArrayList<UserParkingList.Data>
            println("----parking_sendList${parking_sendList}")
            println("----parking_build${building_Name}")
            building_Name = intent.getStringExtra("build").toString()
            get_dis = intent.getStringExtra("dis").toString()
            get_price = intent.getStringExtra("price").toString()
            get_bedRoom = intent.getStringExtra("bed").toString()
            get_bathRoom = intent.getStringExtra("bath").toString()
            get_ft = intent.getStringExtra("ft").toString()
            get_url = intent.getSerializableExtra("url") as java.util.ArrayList<String>
            get_diste = intent.getStringExtra("dist").toString()
            get_discription = intent.getStringExtra("discription").toString()
            get_filedDetailsId = intent.getStringExtra("filedDetails_ID").toString()
            get_buildingId = intent.getStringExtra("building_Id").toString()
            get_ParkingId = intent.getStringExtra("parking_Id").toString()
            get_lati = intent.getDoubleExtra("lati", 0.0)
            get_longi = intent.getDoubleExtra("longi", 0.0)
            if (subscription) {
                //new chnages
//                binding.tvMembership.visibility = View.INVISIBLE
//                // binding.paynow.visibility = View.VISIBLE
//                binding.ivCalling.visibility = View.VISIBLE
                //new chnages
                binding.tvPriceNew.visibility = View.VISIBLE
                binding.tvPropertyPrice.visibility = View.GONE
            } else {
                //new chnages
//                binding.tvMembership.visibility = View.INVISIBLE
//                // binding.paynow.visibility = View.INVISIBLE
//                binding.ivCalling.visibility = View.VISIBLE
                //new chnages
                binding.tvPriceNew.visibility = View.INVISIBLE
                binding.tvPropertyPrice.visibility = View.VISIBLE
            }
        } else if (from.equals("tenant_property")) {
            binding.layoutDistance.visibility = View.GONE
            binding.layoutBedroom.visibility = View.GONE
            binding.layoutBathroom.visibility = View.GONE
            binding.tvDate.visibility = View.GONE
            // binding.tvMembership.visibility = View.INVISIBLE
            /* binding.tvPriceNew.visibility = View.VISIBLE
             binding.tvPropertyPrice.visibility = View.GONE*/
            ////
            sendList = intent.getSerializableExtra("sendList") as ArrayList<PropertyLisRes.Data>
            println("----sendListTenant${sendList}")
            get_propertyType = intent.getStringExtra("propertyType").toString()
            get_property_Resi = intent.getStringExtra("property_Resi").toString()
            addedBy = intent.getStringExtra("added").toString()
            println("----addedBy$addedBy")
            building_Name = intent.getStringExtra("build").toString()
            get_dis = intent.getStringExtra("dis").toString()
            get_price = intent.getStringExtra("price").toString()
            get_bedRoom = intent.getStringExtra("bed").toString()
            get_bathRoom = intent.getStringExtra("bath").toString()
            get_ft = intent.getStringExtra("ft").toString()
            get_url = intent.getSerializableExtra("url") as java.util.ArrayList<String>
            get_diste = intent.getStringExtra("dist").toString()
            get_discription = intent.getStringExtra("discription").toString()
            get_filedDetailsId = intent.getStringExtra("filedDetails_ID").toString()
            get_buildingId = intent.getStringExtra("building_Id").toString()
            get_lati = intent.getDoubleExtra("lati", 0.0)
            get_longi = intent.getDoubleExtra("longi", 0.0)

            println("---getlati${get_lati}")
            println("---getlongi${get_longi}")
            println("---pppi${get_url}")
            getamem_List =
                intent.getSerializableExtra("amm_list") as java.util.ArrayList<PropertyLisRes.Data.Amentity>
            if (subscription) {
                //new chnages
//                binding.tvMembership.visibility = View.INVISIBLE
//                // binding.paynow.visibility = View.VISIBLE
//                binding.ivCalling.visibility = View.VISIBLE
                //new chnages
                binding.tvPriceNew.visibility = View.VISIBLE
                binding.tvPropertyPrice.visibility = View.GONE
            } else {
                //new chnages
//                binding.tvMembership.visibility = View.INVISIBLE
//                // binding.paynow.visibility = View.INVISIBLE
//                binding.ivCalling.visibility = View.VISIBLE
                //new chnages
                binding.tvPriceNew.visibility = View.INVISIBLE
                binding.tvPropertyPrice.visibility = View.VISIBLE
            }
        } else if (from.equals("ownerSide_parking")) {
            parking_sendList =
                intent.getSerializableExtra("parking_send_list") as ArrayList<UserParkingList.Data>
            println("----parking_sendList${parking_sendList}")
            println("----parking_build${building_Name}")
            building_Name = intent.getStringExtra("build").toString()
            get_dis = intent.getStringExtra("dis").toString()
            get_price = intent.getStringExtra("price").toString()
            get_bedRoom = intent.getStringExtra("bed").toString()
            get_bathRoom = intent.getStringExtra("bath").toString()
            get_ft = intent.getStringExtra("ft").toString()
            get_url = intent.getSerializableExtra("url") as java.util.ArrayList<String>
            get_diste = intent.getStringExtra("dist").toString()
            get_discription = intent.getStringExtra("discription").toString()
            get_filedDetailsId = intent.getStringExtra("filedDetails_ID").toString()
            get_buildingId = intent.getStringExtra("building_Id").toString()
            get_ParkingId = intent.getStringExtra("parking_Id").toString()
            get_lati = intent.getDoubleExtra("lati", 0.0)
            get_longi = intent.getDoubleExtra("longi", 0.0)
            binding.tvMembership.visibility = View.INVISIBLE
            //binding.paynow.visibility = View.VISIBLE
            binding.ivCalling.visibility = View.INVISIBLE
            binding.ivDiamond.visibility = View.INVISIBLE
            binding.tvDate.visibility = View.GONE
            binding.layRent.visibility = View.INVISIBLE
            binding.layResidental.visibility = View.INVISIBLE
            if (subscription) {
                //new chnages
//                binding.tvMembership.visibility = View.INVISIBLE
//                // binding.paynow.visibility = View.VISIBLE
//                binding.ivCalling.visibility = View.VISIBLE
                //new chnages
                binding.tvPriceNew.visibility = View.VISIBLE
                binding.tvPropertyPrice.visibility = View.GONE
            } else {
                //new chnages
//                binding.tvMembership.visibility = View.INVISIBLE
//                // binding.paynow.visibility = View.INVISIBLE
//                binding.ivCalling.visibility = View.VISIBLE
                //new chnages
                binding.tvPriceNew.visibility = View.INVISIBLE
                binding.tvPropertyPrice.visibility = View.VISIBLE
            }
        } else if (from.equals("ownerSide_property")) {
            sendList = intent.getSerializableExtra("sendList") as ArrayList<PropertyLisRes.Data>
            println("----sendList${sendList}")
            addedBy = intent.getStringExtra("added").toString()
            println("----addedBy$addedBy")
            get_propertyType = intent.getStringExtra("propertyType").toString()
            get_property_Resi = intent.getStringExtra("property_Resi").toString()
            building_Name = intent.getStringExtra("build").toString()
            get_dis = intent.getStringExtra("dis").toString()
            get_price = intent.getStringExtra("price").toString()
            get_bedRoom = intent.getStringExtra("bed").toString()
            get_bathRoom = intent.getStringExtra("bath").toString()
            get_ft = intent.getStringExtra("ft").toString()
            get_url = intent.getSerializableExtra("url") as java.util.ArrayList<String>
            get_diste = intent.getStringExtra("dist").toString()
            get_discription = intent.getStringExtra("discription").toString()
            get_filedDetailsId = intent.getStringExtra("filedDetails_ID").toString()
            get_buildingId = intent.getStringExtra("building_Id").toString()
            get_lati = intent.getDoubleExtra("lati", 0.0)
            get_longi = intent.getDoubleExtra("longi", 0.0)
            binding.tvDate.visibility = View.GONE
            println("---getlati${get_lati}")
            println("---getlongi${get_longi}")
            println("---pppi${get_url}")
            getamem_List =
                intent.getSerializableExtra("amm_list") as java.util.ArrayList<PropertyLisRes.Data.Amentity>
            binding.tvMembership.visibility = View.INVISIBLE
            //binding.paynow.visibility = View.VISIBLE
            binding.ivCalling.visibility = View.INVISIBLE
            binding.ivDiamond.visibility = View.INVISIBLE
            if (subscription) {
                /// new chnages
//                binding.tvMembership.visibility = View.INVISIBLE
//                // binding.paynow.visibility = View.VISIBLE
//                binding.ivCalling.visibility = View.VISIBLE
                /// new chnages
                binding.tvPriceNew.visibility = View.VISIBLE
                binding.tvPropertyPrice.visibility = View.GONE
            } else {
                /// new chnages
//                binding.tvMembership.visibility = View.INVISIBLE
//                // binding.paynow.visibility = View.INVISIBLE
//                binding.ivCalling.visibility = View.VISIBLE
                /// new chnages
                binding.tvPriceNew.visibility = View.INVISIBLE
                binding.tvPropertyPrice.visibility = View.VISIBLE
            }
        } else {
            binding.tvDate.visibility = View.GONE
            //binding.tvMembership.visibility = View.VISIBLE
            binding.tvPriceNew.visibility = View.INVISIBLE
            binding.tvPropertyPrice.visibility = View.VISIBLE
            binding.layoutDistance.visibility = View.VISIBLE
            binding.layoutBedroom.visibility = View.VISIBLE
            binding.layoutBathroom.visibility = View.VISIBLE
            binding.ivDiamond.visibility = View.VISIBLE

            if (from.equals("user_parking")) {
                parking_sendList =
                    intent.getSerializableExtra("parking_send_list") as ArrayList<UserParkingList.Data>
                println("----parking_sendList${parking_sendList}")
                println("----parking_build${building_Name}")
                building_Name = intent.getStringExtra("build").toString()
                get_dis = intent.getStringExtra("dis").toString()
                get_price = intent.getStringExtra("price").toString()
                get_bedRoom = intent.getStringExtra("bed").toString()
                get_bathRoom = intent.getStringExtra("bath").toString()
                get_ft = intent.getStringExtra("ft").toString()
                get_url = intent.getSerializableExtra("url") as java.util.ArrayList<String>
                get_diste = intent.getStringExtra("dist").toString()
                get_discription = intent.getStringExtra("discription").toString()
                get_filedDetailsId = intent.getStringExtra("filedDetails_ID").toString()
                get_buildingId = intent.getStringExtra("building_Id").toString()
                get_ParkingId = intent.getStringExtra("parking_Id").toString()
                get_lati = intent.getDoubleExtra("lati", 0.0)
                get_longi = intent.getDoubleExtra("longi", 0.0)
                if (subscription) {
                    /// new Chnages
//                    binding.tvMembership.visibility = View.INVISIBLE
//                    //binding.paynow.visibility = View.INVISIBLE
//                    binding.ivCalling.visibility = View.INVISIBLE
                    /// new Chnages
                } else {
                    /// new Chnages
//                    binding.tvMembership.visibility = View.VISIBLE
//                    //binding.paynow.visibility = View.INVISIBLE
//                    binding.ivCalling.visibility = View.VISIBLE
                    /// new Chnages
                }
            } else {
                sendList = intent.getSerializableExtra("sendList") as ArrayList<PropertyLisRes.Data>
                println("----sendList${sendList}")
                addedBy = intent.getStringExtra("added").toString()
                get_propertyType = intent.getStringExtra("propertyType").toString()
                get_property_Resi = intent.getStringExtra("property_Resi").toString()
                println("----addedBy$addedBy")
                building_Name = intent.getStringExtra("build").toString()
                get_dis = intent.getStringExtra("dis").toString()
                get_price = intent.getStringExtra("price").toString()
                get_bedRoom = intent.getStringExtra("bed").toString()
                get_bathRoom = intent.getStringExtra("bath").toString()
                get_ft = intent.getStringExtra("ft").toString()
                get_url = intent.getSerializableExtra("url") as java.util.ArrayList<String>
                get_diste = intent.getStringExtra("dist").toString()
                get_discription = intent.getStringExtra("discription").toString()
                get_filedDetailsId = intent.getStringExtra("filedDetails_ID").toString()
                get_buildingId = intent.getStringExtra("building_Id").toString()
                get_lati = intent.getDoubleExtra("lati", 0.0)
                get_longi = intent.getDoubleExtra("longi", 0.0)

                println("---getlati${get_lati}")
                println("---getlongi${get_longi}")
                println("---pppi${get_url}")
                getamem_List =
                    intent.getSerializableExtra("amm_list") as java.util.ArrayList<PropertyLisRes.Data.Amentity>
                if (subscription) {
                    ///new Changes
//                    binding.tvMembership.visibility = View.INVISIBLE
//                    // binding.paynow.visibility = View.INVISIBLE
//                    binding.ivCalling.visibility = View.VISIBLE
                    ///new Changes
                } else {
                    ///new Chnages
//                    binding.tvMembership.visibility = View.VISIBLE
//                    // binding.paynow.visibility = View.INVISIBLE
//                    binding.ivCalling.visibility = View.INVISIBLE
                    ///new Chnages
                }
            }
        }
        val map = SupportMapFragment()
        supportFragmentManager.beginTransaction().replace(binding.mapview.id, map)
            .commitAllowingStateLoss()
        map.getMapAsync(this)
        handler = Handler(Looper.myLooper()!!)
        binding.imageView52.setOnClickListener {
            if (from.equals("user_property")) {
                startActivity(
                    Intent(this, MainActivity::class.java).putExtra("from", "from_side_home")
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK and Intent.FLAG_ACTIVITY_CLEAR_TOP)
                )
                finish()
            } else if (from.equals("user_parking")) {
                startActivity(
                    Intent(this, MainActivity::class.java).putExtra("from", "from_side_home")
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK and Intent.FLAG_ACTIVITY_CLEAR_TOP)
                )
                finish()
            } else {
                finish()
            }
        }
        binding.tvMembership.setOnClickListener {
            if (from.equals("user_parking")) {
                startActivity(
                    Intent(this, GetIntercomPremiumActivity::class.java).putExtra(
                        "from", "user_parking_details"
                    ).putExtra("id", id)
                )
                getUserViewContact()
            } else if (from.equals("tenant_parking")) {
                startActivity(
                    Intent(this, GetIntercomPremiumActivity::class.java).putExtra(
                        "from", "tenant_parking_details"
                    ).putExtra("id", id)
                )
                getUserViewContact()
            } else if (from.equals("tenant_property")) {
                startActivity(
                    Intent(this, GetIntercomPremiumActivity::class.java).putExtra(
                        "from", "tenant_property_details"
                    ).putExtra("id", id)
                )
                getUserViewContact()
            } else if (from.equals("ownerSide_property")) {
                startActivity(
                    Intent(this, GetIntercomPremiumActivity::class.java).putExtra(
                        "from", "ownerSide_property"
                    ).putExtra("id", id)
                )
                getUserViewContact()
            } else {
                startActivity(
                    Intent(this, NewSubUserActivity::class.java).putExtra(
                        "from", "user_property_details"
                    ).putExtra("id", id)
                )
                getUserViewContact()
            }
        }
        if (subscription) {
            binding.laycontactowner.setOnClickListener {
                if (from.equals("user_parking")) {
                    startActivity(
                        Intent(this, PropertyDetailsActivity::class.java).putExtra(
                            "from", "user_parking_details"
                        ).putExtra("id", id)
                    )
                    getUserViewContact()
                } else if (from.equals("tenant_Side_property")) {
                    startActivity(
                        Intent(this, PropertyDetailsActivity::class.java).putExtra(
                            "from", "tenant_Side_property"
                        ).putExtra("id", id)
                    )
                    getUserViewContact()
                } else if (from.equals("ownerSide_property")) {
                    startActivity(
                        Intent(this, PropertyDetailsActivity::class.java).putExtra(
                            "from", "ownerSide_property"
                        ).putExtra("id", id)
                            .putExtra("addedBy", addedBy)
                    )
                    getUserViewContact()
                } else if (from.equals("ownerSide_parking")) {
                    startActivity(
                        Intent(this, PropertyDetailsActivity::class.java).putExtra(
                            "from", "ownerSide_parking"
                        ).putExtra("id", id)
                    )
                    getUserViewContact()
                } else if (from.equals("tenant_parking")) {
                    startActivity(
                        Intent(this, PropertyDetailsActivity::class.java).putExtra(
                            "from", from
                        ).putExtra("id", parkingId)
                    )
                    getUserViewContact()
                } else if (from.equals("tenant_property")) {
                    startActivity(
                        Intent(this, PropertyDetailsActivity::class.java).putExtra(
                            "from", from
                        ).putExtra("id", id)
                    )
                    getUserViewContact()
                } else
                    if (availableContacts == 0) {
                        userSubscriptionPopup()
                    } else {
                        startActivity(
                            Intent(this, PropertyDetailsActivity::class.java).putExtra(
                                "from", "user_property_details"
                            ).putExtra("id", id)
                                .putExtra("addedBy", addedBy)
                        )
                        getUserViewContact()
                    }

                /* startActivity(
                     Intent(this, GetIntercomPremiumActivity::class.java).putExtra(
                         "from", "user_property_details"
                     ).putExtra("id", id)
                 )
                 getUserViewContact()*/


            }
        } else {
            binding.laycontactowner.setOnClickListener {
                if (from.equals("user_parking")) {
                    if (availableContacts > 0) {
                        startActivity(
                            Intent(this, PropertyDetailsActivity::class.java).putExtra(
                                "from", "user_parking_details"
                            ).putExtra("id", id)
                        )
                        getUserViewContact()
                    } else {
                        startActivity(
                            Intent(this, NewSubUserActivity::class.java).putExtra(
                                "from", "user_parking_details"
                            ).putExtra("id", id)
                        )
                        getUserViewContact()
                    }
                    /*startActivity(
                        Intent(this, PropertyDetailsActivity::class.java).putExtra(
                            "from", "user_parking_details"
                        ).putExtra("id", id)
                    )*/

                } else if (from.equals("tenant_Side_property")) {
                    startActivity(
                        Intent(this, PropertyDetailsActivity::class.java).putExtra(
                            "from", "tenant_Side_property"
                        ).putExtra("id", id)
                    )
                    getUserViewContact()
                } else if (from.equals("ownerSide_property")) {
                    startActivity(
                        Intent(this, PropertyDetailsActivity::class.java).putExtra(
                            "from", "ownerSide_property"
                        ).putExtra("id", id)
                    )
                    getUserViewContact()
                } else if (from.equals("ownerSide_parking")) {
                    startActivity(
                        Intent(this, PropertyDetailsActivity::class.java).putExtra(
                            "from", "ownerSide_parking"
                        ).putExtra("id", id)
                    )
                    getUserViewContact()
                } else if (from.equals("tenant_parking")) {
                    startActivity(
                        Intent(this, PropertyDetailsActivity::class.java).putExtra(
                            "from", from
                        ).putExtra("id", parkingId)
                    )
                    getUserViewContact()
                } else if (from.equals("tenant_property")) {
                    startActivity(
                        Intent(this, PropertyDetailsActivity::class.java).putExtra(
                            "from", from
                        ).putExtra("id", id)
                    )
                    getUserViewContact()
                } else {
                    /*if (availableContacts > 0) {
                        startActivity(
                            Intent(this, PropertyDetailsActivity::class.java).putExtra(
                                "from", "user_property_details"
                            ).putExtra("id", id)
                        )
                        getUserViewContact()
                    } else {*/
                    startActivity(
                        Intent(this, NewSubUserActivity::class.java).putExtra(
                            "from", "user_property_details"
                        ).putExtra("id", id)
                    )
                    getUserViewContact()
                }

                // }

            }
        }
        binding.layoutMapview.setOnClickListener {
            val geoUri = "http://maps.google.com/maps?q=loc:$get_lati,$get_longi ($building_Name)"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(geoUri))
            startActivity(intent)
        }
        setPropertyAdapter()
        setUpSlidingViewPager()

        fetchData()
        initialize()
        observer()
        getUserPropertyListList()
    }

    private fun userSubscriptionPopup() {
        val dialog = this.let { Dialog(this) }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.user_subscription_popup)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
        val tvAv = dialog.findViewById<TextView>(R.id.textView1661)
        val tvTotal = dialog.findViewById<TextView>(R.id.textView16612)
        val tvYes = dialog.findViewById<TextView>(R.id.tvUserSubyes)
        val tvNo = dialog.findViewById<TextView>(R.id.textView166314)
        val tvdurtion = dialog.findViewById<TextView>(R.id.textView16611)
        val dur = prefs.getInt(SessionConstants.DURATION)
        if (dur != null) {
            tvdurtion.text = "$dur left"
        }
        tvAv.text = "${availableContacts}/"
        tvTotal.text = totalContacts.toString()
        tvYes.setOnClickListener {
            dialog.dismiss()
            startActivity(
                Intent(this, NewSubUserActivity::class.java).putExtra(
                    "from", "user_property_details"
                ).putExtra("id", id)
            )
            getUserViewContact()
        }
        tvNo.setOnClickListener {
            dialog.dismiss()
        }


        val window = dialog.window
        if (window != null) {
            window.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT
            )
        }

    }

    private fun initialize() {
        val repo = UserHomeRepository(BaseApplication.apiService)
        viewModel = ViewModelProvider(this, UserHomeFactory(repo))[UserHomeViewModel::class.java]

    }

    private fun getUserViewContact() {
        val token = prefs.getString(
            SessionConstants.TOKEN, ""
        )
        if (from.equals("user_parking")) {
            val model = UserViewContactPostModel(flatId = null, "parking", parkingId)
            viewModel.getuserViewContact(token, model)
        } else if (from.equals("tenant_parking")) {
            val model = UserViewContactPostModel(flatId = null, "parking", parkingId)
            viewModel.getuserViewContact(token, model)
        } else if (from.equals("tenant_property")) {
            val model = UserViewContactPostModel(flatId, "tolet_flat", parkingId = null)
            viewModel.getuserViewContact(token, model)
        } else {
            val model = UserViewContactPostModel(flatId, "tolet_flat", parkingId = null)
            viewModel.getuserViewContact(token, model)
        }


    }

    private fun getUserPropertyListList() {
        val token = prefs.getString(
            SessionConstants.TOKEN, ""
        )
        val model = PropertyListUserPostModel(
            getLatitude,
            getLogitude,
            minValue = null,
            maxValue = null,
            sort = null,
            parkingStatus = null,
            flatType = null
        )
        viewModel.getUserPropertyListList(token, model)

    }

    private fun observer() {
        viewModel.userViewContactLiveData.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            prefs.put(
                                SessionConstants.AVAILABLE_CONTACTS,
                                it.data.dataUpdate.availableContacts
                            )
                            prefs.put(
                                SessionConstants.TOTALS_CONTACTS,
                                it.data.dataUpdate.totalContacts
                            )
                        } else if (it.status == AppConstants.STATUS_500) {
                            this.longToast(it.message)
                        } else if (it.status == AppConstants.STATUS_404) {
                            this.longToast(it.message)
                        } else {

                        }
                    }
                }

                is EmpResource.Failure -> {
                    EmpCustomLoader.hideLoader()
                    ErrorUtil.handlerGeneralError(this, it.throwable!!)
                }
                else -> {}
            }
        })
        viewModel.userPropertyListLiveData.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }
                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            propertyList.clear()
                            propertyList.addAll(it.data)

                            binding.rcydetails.layoutManager =
                                LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
                            val adptr = OwnerSimilarPropertyAdapter(this, propertyList)
                            binding.rcydetails.adapter = adptr
                            adptr!!.notifyDataSetChanged()

                        } else if (it.status == AppConstants.STATUS_404) {
                            this.longToast(it.message)
                        } else {

                        }
                    }
                }

                is EmpResource.Failure -> {
                    EmpCustomLoader.hideLoader()
                    ErrorUtil.handlerGeneralError(this, it.throwable!!)
                }
                else -> {}
            }
        })

    }

    private fun setUpSlidingViewPager() {
        imagesArray = arrayListOf()
        imagesArray.clear()
//
        imagesArray.addAll(get_url)

        val landingImagesAdapter = SlidingImagesAdapter(this, imagesArray)

        binding.viewpager2.apply {
            adapter = landingImagesAdapter
            registerOnPageChangeCallback(slidingCallback)

        }

        binding.viewpager2.offscreenPageLimit = 1
        binding.viewpager2.clipToPadding = false
        binding.viewpager2.clipChildren = false
//        viewPager2.setCurrentItem(2, false)
//        binding.viewpager2.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        slidingDotsCount = imagesArray.size
        slidingImageDots = arrayOfNulls(slidingDotsCount)

        for (i in 0 until slidingDotsCount) {
            slidingImageDots[i] = ImageView(this@CompleteParkingToLetDetailsActivity)
            slidingImageDots[i]?.setImageDrawable(
                ContextCompat.getDrawable(
                    this, R.drawable.selected_dot
                )
            )
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
            )

            params.setMargins(8, 0, 0, 8)
            binding.sliderDots.addView(slidingImageDots[i], params)
        }

        try {
            slidingImageDots[0]?.setImageDrawable(
                ContextCompat.getDrawable(
                    this, R.drawable.default_dot
                )
            )
        } catch (_: java.lang.Exception) {

        }


        val update = Runnable {
            if (currentPage == imagesArray.size) {
                currentPage = 0
            }
            binding.viewpager2.setCurrentItem(currentPage++, true)

        }

        Timer().schedule(object : TimerTask() {
            override fun run() {
                handler.post(update)
            }
        }, 3000, 2000)
    }

    private fun fetchData() {
        var subscription = prefs.getBoolean(SessionConstants.SUBSCRIPTION)
        println("-----subb$subscription")
        if (from.equals("user_parking")) {
            parkingId = get_ParkingId
            id = get_filedDetailsId
            buildingId = get_buildingId
            flatId = get_filedDetailsId
            binding.tvPropertyName.text = building_Name
            binding.tvLocation.text = "${get_dis}"
            binding.tvDistance.text = "${get_diste} ${getString(R.string.km)}"
            binding.tvFit.text = "${get_ft} ${getString(R.string.ft)}"
            binding.tvBedroom.text = "${get_bedRoom} ${getString(R.string.bedroom)}"
            binding.tvBathroom.text = "${get_bathRoom} ${getString(R.string.bathrooms)}"
            val htmlAsString = get_discription
            val htmlAsSpanned = Html.fromHtml(htmlAsString)
            binding.tvDetailsDesc.text = htmlAsSpanned
            if (htmlAsSpanned.length > 100) {
                binding.tvReadMore.visibility = View.VISIBLE
            } else {
                binding.tvReadMore.visibility = View.GONE

            }
            binding.tvPropertyPrice.text = "৳${get_price}"

        } else if (from.equals("tenant_property")) {
            id = get_filedDetailsId
            buildingId = get_buildingId
            flatId = get_filedDetailsId
            binding.tvPropertyName.text = building_Name
            binding.tvLocation.text = get_dis
            binding.tvDistance.text = "${get_diste} ${getString(R.string.km)}"
            binding.tvFit.text = "${get_ft} ${getString(R.string.ft)}"
            binding.tvBedroom.text = "${get_bedRoom} ${getString(R.string.bedroom)}"
            binding.tvBathroom.text = "${get_bathRoom} ${getString(R.string.bathrooms)}"
            val htmlAsString = get_discription
            val htmlAsSpanned = Html.fromHtml(htmlAsString)
            binding.tvDetailsDesc.text = htmlAsSpanned
            if (htmlAsSpanned.length > 100) {
                binding.tvReadMore.visibility = View.VISIBLE
            } else {
                binding.tvReadMore.visibility = View.GONE

            }
            if (subscription) {
                binding.tvPriceNew.text = "৳${get_price}"
            } else {
                binding.tvPropertyPrice.text = "৳${get_price}"
            }
            binding.tvPropertyType.text = get_propertyType
            binding.tvPropertyType1.text = get_property_Resi

        } else if (from.equals("tenant_parking")) {
            parkingId = get_ParkingId
            id = get_filedDetailsId
            buildingId = get_buildingId
            flatId = get_filedDetailsId
            binding.tvPropertyName.text = building_Name
            binding.tvLocation.text = "${get_dis}"
            binding.tvDistance.text = "${get_diste} ${getString(R.string.km)}"
            binding.tvFit.text = "${get_ft} ${getString(R.string.ft)}"
            binding.tvBedroom.text = "${get_bedRoom} ${getString(R.string.bedroom)}"
            binding.tvBathroom.text = "${get_bathRoom} ${getString(R.string.bathrooms)}"
            val htmlAsString = get_discription
            val htmlAsSpanned = Html.fromHtml(htmlAsString)
            binding.tvDetailsDesc.text = htmlAsSpanned
            if (htmlAsSpanned.length > 100) {
                binding.tvReadMore.visibility = View.VISIBLE
            } else {
                binding.tvReadMore.visibility = View.GONE

            }
            if (subscription) {
                binding.tvPriceNew.text = "৳${get_price}"
            } else {
                binding.tvPropertyPrice.text = "৳${get_price}"
            }

        } else {
            id = get_filedDetailsId
            buildingId = get_buildingId
            flatId = get_filedDetailsId
            /* binding.tvPropertyName.text = sendList[0].buildingName
             binding.tvLocation.text = "${sendList[0].district} , ${sendList[0].division}"
             val dis: Double = sendList[0].distance / 1000
             val dis2: Double = String.format("%.1f", dis).toDouble()
             binding.tvDistance.text = "${dis2} Km"
             binding.tvFit.text = "${sendList[0].flatDetail.sqft} ft"
             binding.tvBedroom.text = "${sendList[0].flatDetail.bedroom} Bedroom"
             binding.tvBathroom.text = "${sendList[0].flatDetail.bathroom} Bathroom"
             binding.tvDetailsDesc.text = sendList[0].description
             binding.tvPropertyPrice.text = "৳${sendList[0].flatInfo.price}"*/

            binding.tvPropertyName.text = building_Name
            binding.tvLocation.text = get_dis
//            val dis: Double = sendList[0].distance / 1000
//            val dis2: Double = String.format("%.1f", dis).toDouble()
            binding.tvDistance.text = "${get_diste} ${getString(R.string.km)}"
            binding.tvFit.text = "${get_ft} ${getString(R.string.ft)}"
            binding.tvBedroom.text = "${get_bedRoom} ${getString(R.string.bedroom)}"
            binding.tvBathroom.text = "${get_bathRoom} ${getString(R.string.bathrooms)}"
            val htmlAsString = get_discription
            val htmlAsSpanned = Html.fromHtml(htmlAsString)
            binding.tvDetailsDesc.text = htmlAsSpanned
            if (htmlAsSpanned.length > 100) {
                binding.tvReadMore.visibility = View.VISIBLE
            } else {
                binding.tvReadMore.visibility = View.GONE

            }
            binding.tvPropertyPrice.text = "৳${get_price}"
            binding.tvPropertyType.text = get_propertyType
            binding.tvPropertyType1.text = get_property_Resi

        }


    }

    override fun onDestroy() {
        super.onDestroy()
        binding.viewpager2.unregisterOnPageChangeCallback(slidingCallback)
    }

    override fun onMapReady(p0: GoogleMap) {
        mMap = p0
        val sydney = LatLng(get_lati, get_longi)
        mMap.addMarker(MarkerOptions().position(sydney).title(building_Name))
        val zoomLevel = 16.0f
        mMap.minZoomLevel
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, zoomLevel))
    }
}