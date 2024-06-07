package com.application.intercom.manager.gatekeeper

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.R
import com.application.intercom.data.model.remote.manager.managerSide.ManagerGateKepperListRes
import com.application.intercom.databinding.ItemGatekeeperListingBinding
import com.application.intercom.helper.calculateTimeDifference
import com.application.intercom.helper.currentTime
import com.application.intercom.helper.differenceOfTime
import com.application.intercom.helper.getParseBetweenTimeStatus
import com.application.intercom.utils.loadImagesWithGlideExt


class ManagerGateKeeperListingAdapter(
    private val context: Context,
    val list: ArrayList<ManagerGateKepperListRes.Data.Result>,
    val onPress: CLick
) : RecyclerView.Adapter<ManagerGateKeeperListingAdapter.MyViewHolder>() {
    class MyViewHolder(val mView: ItemGatekeeperListingBinding) :
        RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemGatekeeperListingBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = list[position]
        holder.mView.ivEdit.setOnClickListener {
            onPress.onEditClick(position)
//        context.startActivity(Intent(context, ManagerParkingToLetParkingActivity::class.java))
        }
        holder.mView.ivProfile.loadImagesWithGlideExt(data.photo)
        holder.mView.tvName.text = data.fullName
        holder.mView.tvMobileNo.text = data.mobileNumber
        println("=========${data}")
        val test = getParseBetweenTimeStatus(data.shiftStartTime, data.shiftEndTime)
        println("=========test$test")
        if (test) {
            holder.mView.cardView8.setCardBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.green
                )
            )
            holder.mView.tvduty.text = context.getString(R.string.on_duty)
        } else {
            holder.mView.cardView8.setCardBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.red
                )
            )
            holder.mView.tvduty.text = context.getString(R.string.off_duty)
        }
        holder.mView.tvGateType.text =
            "Shifting Time - ${list[position].shiftStartTime} - ${list[position].shiftEndTime}"
    }

    override fun getItemCount(): Int {
        return list.size

    }

    interface CLick {
        fun onEditClick(position: Int)
    }
}