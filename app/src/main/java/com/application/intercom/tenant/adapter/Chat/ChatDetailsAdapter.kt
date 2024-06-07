package com.application.intercom.tenant.adapter.Chat

import android.content.Context
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.data.model.remote.userCreateRoom.UserMessageHistoryList
import com.application.intercom.databinding.ChatItemsDetailsBinding
import com.application.intercom.helper.*

import com.application.intercom.utils.CommonUtil
import com.application.intercom.utils.loadImagesWithGlideExt
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.temporal.Temporal
import java.util.Date


class ChatDetailsAdapter(
    val con: Context,
    var senderName: String,
    var profile: String,
    var onPress: ClickPic,
    var userId: String,
    var recivePic: String

) : RecyclerView.Adapter<ChatDetailsAdapter.MyViewHolder>() {

    private var mTodayDate: Date? = null
    private var mSimpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")

    init {
        try {
            val today: String = mSimpleDateFormat!!.format(Date().time)
            mTodayDate = mSimpleDateFormat!!.parse(today)
        } catch (e: java.text.ParseException) {
            e.printStackTrace()
        }
    }

    val list = ArrayList<UserMessageHistoryList.Data2>()

    class MyViewHolder(val mView: ChatItemsDetailsBinding) : RecyclerView.ViewHolder(mView.root)


    fun setMessageData(list2: ArrayList<UserMessageHistoryList.Data2>) {
        list.addAll(list2)
        notifyItemRangeChanged(0, list.size)


    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): MyViewHolder {
        return MyViewHolder(
            ChatItemsDetailsBinding.inflate(
                LayoutInflater.from(con), parent, false
            )
        )
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // holder.mView.textView26.text = senderName
        if (!list[position].msgType.isNullOrEmpty()) {
            val parts: Array<String> = list[position].createdAt.split("T").toTypedArray()
            if (showDate(position, parts[0])) {
                holder.mView.tvReciveDate.visibility = View.VISIBLE
                holder.mView.tvReciveDate.text = if (isTodayDate(list[position].createdAt))
                    "Today"
                else if (isYesterdaysDate(list[position].createdAt))
                    "Yesterday"
                else parseDateyyyymmddFormat(list[position].createdAt)
            } else {
                holder.mView.tvReciveDate.visibility = View.GONE
            }


            if (userId.equals(list[position].sender)) {
                if (list[position].msgType.equals("photo")) {
                    holder.mView.cardView17.visibility = View.VISIBLE
                    val url = Html.fromHtml(list[position].message)

                    holder.mView.uploadImg.loadImagesWithGlideExt(url.toString())
                    holder.mView.textView251.visibility = View.GONE
                    holder.mView.ivDownloadIcon.visibility = View.VISIBLE
                    holder.mView.ivDownloadIcon.setOnClickListener {
                        onPress.onImgDownload(url.toString())
                    }
                    holder.mView.cardView17.setOnClickListener {
                        onPress.onShowImg(url.toString())
                    }
                    holder.mView.cardVideos.visibility = View.GONE
                    holder.mView.textView241.visibility = View.VISIBLE
                    holder.mView.imageView12.visibility = View.VISIBLE



                    holder.mView.imgRecive.visibility = View.GONE
                    holder.mView.reciveVideos.visibility = View.GONE
                    holder.mView.ivReciveDownloadIcon.visibility = View.GONE
                    holder.mView.imageView11.visibility = View.GONE
                    holder.mView.textView25.visibility = View.GONE
                    holder.mView.textView24.visibility = View.GONE
                    holder.mView.textView23.visibility = View.GONE
                    holder.mView.reciveData.visibility = View.GONE

                    val time = getLocalTime(list[position].createdAt)
                    holder.mView.textView241.text = time

                    holder.mView.imageView12.loadImagesWithGlideExt(profile)


                } else if (list[position].msgType.equals("video")) {
                    holder.mView.cardVideos.visibility = View.VISIBLE
                    holder.mView.cardView17.visibility = View.INVISIBLE
                    val url = Html.fromHtml(list[position].message)


                    holder.mView.textView251.visibility = View.GONE
                    holder.mView.ivDownloadIcon.visibility = View.VISIBLE
                    holder.mView.ivDownloadIcon.setOnClickListener {
                        onPress.onImgDownload(url.toString())
                    }
                    holder.mView.cardVideos.setOnClickListener {
                        onPress.onVideoShow(url.toString())
                    }
                    println("----url $url")
                    //  val thum = CommonUtil.retrieveVideoFrameFromVideo(url.toString())
                    holder.mView.ivVideos.loadImagesWithGlideExt(url.toString())

                    holder.mView.textView241.visibility = View.VISIBLE
                    holder.mView.imageView12.visibility = View.VISIBLE


                    holder.mView.imgRecive.visibility = View.GONE
                    holder.mView.reciveVideos.visibility = View.GONE
                    holder.mView.ivReciveDownloadIcon.visibility = View.GONE
                    holder.mView.imageView11.visibility = View.GONE
                    holder.mView.textView25.visibility = View.GONE
                    holder.mView.textView24.visibility = View.GONE
                    holder.mView.textView23.visibility = View.GONE
                    holder.mView.reciveData.visibility = View.GONE

                    val time = getLocalTime(list[position].createdAt)
                    holder.mView.textView241.text = time
                    holder.mView.imageView12.loadImagesWithGlideExt(profile)
                } else {
                    holder.mView.textView251.visibility = View.VISIBLE
                    holder.mView.textView251.text = list[position].message
                    holder.mView.cardView17.visibility = View.GONE
                    holder.mView.ivDownloadIcon.visibility = View.GONE
                    holder.mView.cardVideos.visibility = View.GONE
                    holder.mView.textView241.visibility = View.VISIBLE
                    holder.mView.imageView12.visibility = View.VISIBLE




                    holder.mView.imgRecive.visibility = View.GONE
                    holder.mView.reciveVideos.visibility = View.GONE
                    holder.mView.ivReciveDownloadIcon.visibility = View.GONE
                    holder.mView.imageView11.visibility = View.GONE
                    holder.mView.textView25.visibility = View.GONE
                    holder.mView.textView24.visibility = View.GONE
                    holder.mView.textView23.visibility = View.GONE
                    holder.mView.reciveData.visibility = View.GONE
                    val time = getLocalTime(list[position].createdAt)
                    holder.mView.textView241.text = time
                    println("----time$time")
                    /* val date = calculateOnyTime(list[position].createdAt)
                     holder.mView.textView241.text = date*/
//                    if (list[position].createdAt.contains(" ")){
//                        val parts: Array<String> =
//                            list[position].createdAt.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
//                                .toTypedArray()
//                        holder.mView.textView241.text = timeWithCurrentTime(parts[0])
//                    }
                    holder.mView.imageView12.loadImagesWithGlideExt(profile)
                }
            } else {
                if (list[position].msgType.equals("photo")) {
                    holder.mView.reciveData.visibility = View.VISIBLE
                    holder.mView.imgRecive.visibility = View.VISIBLE
                    val url = Html.fromHtml(list[position].message)

                    holder.mView.reciveImg.loadImagesWithGlideExt(url.toString())
                    holder.mView.textView25.visibility = View.GONE
                    holder.mView.reciveVideos.visibility = View.GONE
                    holder.mView.ivReciveDownloadIcon.visibility = View.VISIBLE
                    val time = getLocalTime(list[position].createdAt)
                    holder.mView.textView24.text = time
                    holder.mView.ivReciveDownloadIcon.setOnClickListener {
                        onPress.onImgDownload(url.toString())
                    }
                    holder.mView.imgRecive.setOnClickListener {
                        onPress.onShowImg(url.toString())
                    }
                    holder.mView.imageView11.visibility = View.VISIBLE
                    holder.mView.textView24.visibility = View.VISIBLE
                    holder.mView.textView23.visibility = View.GONE
                    holder.mView.imageView11.loadImagesWithGlideExt(recivePic)






                    holder.mView.cardVideos.visibility = View.GONE
                    holder.mView.cardView17.visibility = View.GONE
                    holder.mView.constraintLayout9.visibility = View.GONE
                    holder.mView.imageView12.visibility = View.GONE
                    holder.mView.textView241.visibility = View.GONE
                    holder.mView.textView251.visibility = View.GONE
                    holder.mView.ivDownloadIcon.visibility = View.GONE
                } else if (list[position].msgType.equals("video")) {
                    holder.mView.reciveVideos.visibility = View.VISIBLE
                    holder.mView.reciveData.visibility = View.VISIBLE
                    holder.mView.imgRecive.visibility = View.INVISIBLE
                    val url = Html.fromHtml(list[position].message)

                    holder.mView.ivReciveVideos.loadImagesWithGlideExt(url.toString())
                    val time = getLocalTime(list[position].createdAt)
                    holder.mView.textView24.text = time
                    holder.mView.textView25.visibility = View.GONE
                    holder.mView.ivReciveDownloadIcon.visibility = View.VISIBLE
                    holder.mView.ivReciveDownloadIcon.setOnClickListener {
                        onPress.onImgDownload(url.toString())
                    }
                    holder.mView.reciveVideos.setOnClickListener {
                        onPress.onVideoShow(url.toString())
                    }


                    holder.mView.imageView11.visibility = View.VISIBLE
                    holder.mView.imageView11.loadImagesWithGlideExt(recivePic)
                    holder.mView.textView24.visibility = View.VISIBLE
                    holder.mView.textView23.visibility = View.GONE

                    holder.mView.cardVideos.visibility = View.GONE
                    holder.mView.cardView17.visibility = View.GONE
                    holder.mView.constraintLayout9.visibility = View.GONE
                    holder.mView.imageView12.visibility = View.GONE
                    holder.mView.textView241.visibility = View.GONE
                    holder.mView.textView251.visibility = View.GONE
                    holder.mView.ivDownloadIcon.visibility = View.GONE
                } else {
                    holder.mView.reciveData.visibility = View.VISIBLE
                    holder.mView.textView25.visibility = View.VISIBLE

                    holder.mView.textView25.text = list[position].message
                    holder.mView.imgRecive.visibility = View.GONE
                    holder.mView.ivReciveDownloadIcon.visibility = View.GONE
                    holder.mView.reciveVideos.visibility = View.GONE
                    val time = getLocalTime(list[position].createdAt)
                    holder.mView.textView24.text = time

                    holder.mView.imageView11.visibility = View.VISIBLE
                    holder.mView.imageView11.loadImagesWithGlideExt(recivePic)
                    holder.mView.textView24.visibility = View.VISIBLE
                    holder.mView.textView23.visibility = View.GONE

                    holder.mView.cardVideos.visibility = View.GONE
                    holder.mView.cardView17.visibility = View.GONE
                    holder.mView.constraintLayout9.visibility = View.GONE
                    holder.mView.imageView12.visibility = View.GONE
                    holder.mView.textView241.visibility = View.GONE
                    holder.mView.textView251.visibility = View.GONE
                    holder.mView.ivDownloadIcon.visibility = View.GONE
                }


            }

        }


    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface ClickPic {
        fun onImgDownload(img: String)
        fun onShowImg(img: String)
        fun onVideoShow(url: String)
    }

    private fun showDate(position: Int, date: String): Boolean {
        if (position == 0) {
            Log.d("showDate", " position $position || thisDate $date")
            return true
        } else {
            val previousTime: String = list[position - 1].createdAt
            val part2 = previousTime.split("T").toTypedArray()
            val thisDate: String = date
            val previousDate: String = part2[0]
            Log.d("showDate", " thisDate $thisDate || previousDate $previousDate")
            if (!thisDate.equals(previousDate, ignoreCase = true)) return true
        }
        return false
    }
}