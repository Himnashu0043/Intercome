package com.application.intercom.gatekepper.activity.newFlow.ownerTenantRegularEntryHistoryDetails

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CalendarView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.ViewModel.ownerViewModel.ownerside.OwnerSideViewModel
import com.application.intercom.data.model.factory.ownerfactory.OwnerSideFactory
import com.application.intercom.data.model.remote.gateKeeper.gateKeeperHome.RegularEntryHistoryDetailsList
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.ownerRepo.ownerSide.OwnerSideRepo
import com.application.intercom.databinding.ActivityOwnerTenantRegularEntryHistoryDetailsBinding
import com.application.intercom.databinding.Example1CalendarDayBinding
import com.application.intercom.gatekepper.gatekeeperAdapter.regularEntryHistoryDetails.OwnerTenantRegularEntryParentHistoryAdapter
import com.application.intercom.helper.GPSService
import com.application.intercom.helper.displayText
import com.application.intercom.helper.setTextColorRes
import com.application.intercom.owner.adapter.ShowImgAdapter
import com.application.intercom.utils.*
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.ViewContainer
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth


class OwnerTenantRegularEntryHistoryDetailsActivity : AppCompatActivity() {
    lateinit var binding: ActivityOwnerTenantRegularEntryHistoryDetailsBinding
    private lateinit var viewModel: OwnerSideViewModel
    private var adptr: OwnerTenantRegularEntryParentHistoryAdapter? = null
    private var list = ArrayList<RegularEntryHistoryDetailsList.Data.Result>()
    private var visitorId: String = ""
    private var from: String = ""
    private var showphotoAdapter: ShowImgAdapter? = null
    private var photo_upload_list = ArrayList<String>()
    private var mobileNumber: String = ""
    private var currentStatus: String = ""
    private var getDate: String = ""
    private val monthCalendarView: com.kizitonwose.calendar.view.CalendarView get() = binding.exOneCalendar
    private val selectedDates = mutableSetOf<LocalDate>()

    @RequiresApi(Build.VERSION_CODES.O)
    private val today = LocalDate.now().toString()
    private lateinit var currentMonth: YearMonth
    private lateinit var startMonth: YearMonth
    private lateinit var endMonth: YearMonth
    private lateinit var daysOfWeek: List<DayOfWeek>


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOwnerTenantRegularEntryHistoryDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        visitorId = intent.getStringExtra("visitorId").toString()
        from = intent.getStringExtra("from").toString()
        println("-----regular$from")
        println("-----today$today")

        ////calender View
        daysOfWeek = daysOfWeek()
        binding.legendLayout.root.children
            .map { it as TextView }
            .forEachIndexed { index, textView ->
                textView.text = daysOfWeek[index].displayText()
                textView.setTextColorRes(R.color.black)
            }

        currentMonth = YearMonth.now()
        startMonth = currentMonth.minusMonths(100)
        endMonth = currentMonth.plusMonths(100)

        initView()
        listener()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initView() {
        initialize()
        observer()
        binding.toolbar.tvTittle.text = getString(R.string.visitor_details)
        if (from.equals("tenant")) {
            ownerTenantRegularEntryHistoryDetailsTenant()
        } else {
            ownerTenantRegularEntryHistoryDetailsOwner()
        }
    }

    private fun initialize() {
        val repo = OwnerSideRepo(BaseApplication.apiService)
        viewModel =
            ViewModelProvider(this, OwnerSideFactory(repo))[OwnerSideViewModel::class.java]
    }

    private fun ownerTenantRegularEntryHistoryDetailsTenant() {
        val token = prefs.getString(
            SessionConstants.TOKEN, GPSService.mLastLocation?.latitude.toString()
        )
        viewModel.ownerTenantRegularEntryHistoryDetailsTenant(token, visitorId)
    }

    private fun ownerTenantRegularEntryHistoryDetailsOwner() {
        val token = prefs.getString(
            SessionConstants.TOKEN, GPSService.mLastLocation?.latitude.toString()
        )
        viewModel.ownerTenantRegularEntryHistoryDetailsOwner(token, visitorId)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun observer() {
        viewModel.ownerTenantRegularEntryHistoryDetailsTenantLiveData.observe(
            this,
            androidx.lifecycle.Observer {
                when (it) {
                    is EmpResource.Loading -> {
                        EmpCustomLoader.showLoader(this)
                    }

                    is EmpResource.Success -> {
                        EmpCustomLoader.hideLoader()
                        it.value.let {
                            if (it.status == AppConstants.STATUS_SUCCESS) {
                                currentStatus = it.data.result.get(0).currentStatus
//                                getDate = it.data.result.get(0).history.get(0)._id ?: ""
//                                println("-----currentStatus$currentStatus")
//                                println("-----getDate$getDate")
                                if (!it.data.result.get(0).history.isNullOrEmpty()){
                                    getDate = it.data.result.get(0).history.get(0)._id ?: ""
                                    println("-----currentStatus$currentStatus")
                                    println("-----getDate$getDate")
                                }
                                mobileNumber = it.data.result.get(0).mobileNumber
                                binding.textView166.text = it.data.result.get(0).visitorName
                                binding.textView167.text =
                                    it.data.result.get(0).flatInfo.get(0).name
                                binding.textView168.text =
                                    "${it.data.result.get(0).visitCategoryName} | ${
                                        it.data.result.get(0).visitorType
                                    } Entry"
                                binding.textView170.text = it.data.result.get(0).mobileNumber
                                binding.tvAddress.text = it.data.result.get(0).address
                                binding.tvNote.text = it.data.result.get(0).note
                                binding.imageView91.loadImagesWithGlideExt(it.data.result.get(0).photo)
                                for (img in it.data.result) {
                                    photo_upload_list.addAll(img.document)
                                }

                                binding.rcyPhoto.layoutManager =
                                    LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
                                showphotoAdapter = ShowImgAdapter(this, photo_upload_list)
                                binding.rcyPhoto.adapter = showphotoAdapter
                                showphotoAdapter!!.notifyDataSetChanged()

                                list.clear()
                                list.addAll(it.data.result)
                                binding.rcyparentHistory.layoutManager = LinearLayoutManager(this)
                                adptr =
                                    OwnerTenantRegularEntryParentHistoryAdapter(this, list, from)
                                binding.rcyparentHistory.adapter = adptr
                                adptr!!.notifyDataSetChanged()
                                setupMonthCalendar(startMonth, endMonth, currentMonth, daysOfWeek)
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

        viewModel.ownerTenantRegularEntryHistoryDetailsOwnerLiveData.observe(
            this,
            androidx.lifecycle.Observer {
                when (it) {
                    is EmpResource.Loading -> {
                        EmpCustomLoader.showLoader(this)
                    }

                    is EmpResource.Success -> {
                        EmpCustomLoader.hideLoader()
                        it.value.let {
                            if (it.status == AppConstants.STATUS_SUCCESS) {
                                currentStatus = it.data.result.get(0).currentStatus
                                if (!it.data.result.get(0).history.isNullOrEmpty()){
                                    getDate = it.data.result.get(0).history.get(0)._id ?: ""
                                    println("-----currentStatus$currentStatus")
                                    println("-----getDate$getDate")
                                }

                                mobileNumber = it.data.result.get(0).mobileNumber
                                binding.textView166.text = it.data.result.get(0).visitorName
                                binding.textView167.text =
                                    it.data.result.get(0).flatInfo.get(0).name
                                binding.textView168.text =
                                    "${it.data.result.get(0).visitCategoryName} | ${
                                        it.data.result.get(0).visitorType
                                    } Entry"
                                binding.textView170.text = it.data.result.get(0).mobileNumber
                                binding.tvAddress.text = it.data.result.get(0).address
                                binding.tvNote.text = it.data.result.get(0).note
                                binding.imageView91.loadImagesWithGlideExt(it.data.result.get(0).photo)
                                for (img in it.data.result) {
                                    photo_upload_list.addAll(img.document)
                                }

                                binding.rcyPhoto.layoutManager =
                                    LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
                                showphotoAdapter = ShowImgAdapter(this, photo_upload_list)
                                binding.rcyPhoto.adapter = showphotoAdapter
                                showphotoAdapter!!.notifyDataSetChanged()

                                list.clear()
                                list.addAll(it.data.result)
                                binding.rcyparentHistory.layoutManager = LinearLayoutManager(this)
                                adptr =
                                    OwnerTenantRegularEntryParentHistoryAdapter(this, list, from)
                                binding.rcyparentHistory.adapter = adptr
                                adptr!!.notifyDataSetChanged()
                                setupMonthCalendar(startMonth, endMonth, currentMonth, daysOfWeek)
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun listener() {
        binding.ivcalender.setOnClickListener {
            binding.ivDetails.visibility = View.VISIBLE
            binding.rcyparentHistory.visibility = View.GONE
            binding.ivcalender.visibility = View.VISIBLE
            binding.tvCalender.visibility = View.VISIBLE
            binding.exOneCalendar.visibility = View.VISIBLE
            binding.legendLayout.ll.visibility = View.VISIBLE
            setupMonthCalendar(startMonth, endMonth, currentMonth, daysOfWeek)
        }
        binding.ivDetails.setOnClickListener {
            binding.ivcalender.visibility = View.VISIBLE
            binding.ivDetails.visibility = View.GONE
            binding.rcyparentHistory.visibility = View.VISIBLE
            binding.tvCalender.visibility = View.GONE
            binding.exOneCalendar.visibility = View.GONE
            binding.legendLayout.ll.visibility = View.GONE
        }

        binding.imageView99.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${mobileNumber}")
            startActivity(intent)
        }
        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupMonthCalendar(
        startMonth: YearMonth,
        endMonth: YearMonth,
        currentMonth: YearMonth,
        daysOfWeek: List<DayOfWeek>,
    ) {
        class DayViewContainer(view: View) : ViewContainer(view) {
            // Will be set when this container is bound. See the dayBinder.
            lateinit var day: CalendarDay
            val textView = Example1CalendarDayBinding.bind(view).exOneDayText

            init {
                view.setOnClickListener {
                    if (day.position == DayPosition.MonthDate) {
                        dateClicked(date = day.date)
                    }
                }
                view.isEnabled = false
                /*if (currentStatus == "Out") {
                    Log.d("checkingDates", "$today == $getDate")
                    if (today.equals(getDate)) {
                        textView.setTextColorRes(R.color.black)
                        textView.setBackgroundResource(R.drawable.example_1_selected_bg)
                    } else {
                        textView.setTextColorRes(R.color.white)
                        textView.setBackgroundResource(R.drawable.common_btn_bg)
                    }

                } else {

//                        textView.setTextColorRes(R.color.white)
//                    textView.setBackgroundResource(R.drawable.common_btn_bg)
                }*/
            }
        }
        monthCalendarView.dayBinder = object : MonthDayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)
            override fun bind(container: DayViewContainer, data: CalendarDay) {
                container.day = data
                bindDate(data.date, container.textView, data.position == DayPosition.MonthDate)
            }
        }
        monthCalendarView.monthScrollListener = { updateTitle() }
        monthCalendarView.setup(startMonth, endMonth, daysOfWeek.first())
        monthCalendarView.scrollToMonth(currentMonth)
    }

    private fun dateClicked(date: LocalDate) {
        if (selectedDates.contains(date)) {
            selectedDates.remove(date)
        } else {
            selectedDates.add(date)
        }
        // Refresh both calendar views..
        monthCalendarView.notifyDateChanged(date)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    private fun updateTitle() {

        val month = monthCalendarView.findFirstVisibleMonth()?.yearMonth ?: return
        binding.exOneYearText.text = month.year.toString()
        binding.exOneMonthText.text = month.month.displayText(short = false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun bindDate(date: LocalDate, textView: TextView, isSelectable: Boolean) {
        textView.text = date.dayOfMonth.toString()
        Log.d("checkingDates", "date : $date || getDate : $getDate ")
//        if (!isSelectable) {
        if (!list.get(0).history.isNullOrEmpty()) {
            list.get(0).history?.forEach {
                if (date.toString().equals(it._id)) {
                    if (currentStatus == "Out") {
                        textView.setTextColorRes(R.color.white)
                        textView.setBackgroundResource(R.drawable.example_organge_bg)
                    } else {
                        textView.setTextColorRes(R.color.white)
                        textView.setBackgroundResource(R.drawable.example_1_selected_bg)

                    }
                }

            }
        }
//            when {
//                /* selectedDates.contains(date) -> {
//                     textView.setTextColorRes(R.color.black)
//                     textView.setBackgroundResource(R.drawable.example_1_selected_bg)
//                 }*/
//                list.get(0).history.forEach {
//
//                }
//                date.toString().equals(getDate) -> {
//                    if (currentStatus == "Out") {
//                        textView.setTextColorRes(R.color.white)
//                        textView.setBackgroundResource(R.drawable.example_1_selected_bg)
//                    } else {
//                        textView.setTextColorRes(R.color.white)
//                        textView.setBackgroundResource(R.drawable.example_organge_bg)
//                    }
////                    textView.setTextColorRes(R.color.black)
////                    textView.setBackgroundResource(R.drawable.example_1_today_bg)
//                }
//
//                else -> {
//                    textView.setTextColorRes(R.color.black)
//                    textView.background = null
//                    /*textView.setTextColorRes(R.color.white)
//                    textView.setBackgroundResource(R.drawable.example_organge_bg)*/
//                }
//
//            }
//        } else {
//            textView.setTextColorRes(R.color.example_1_white_light)
//            textView.background = null
//        }
    }
}