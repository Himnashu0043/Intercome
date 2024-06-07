package com.application.intercom.manager.service_charge

import android.app.DatePickerDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.DatePicker
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.data.model.local.DateModelWithName
import com.application.intercom.databinding.NewServiceChargeItemBinding
import com.application.intercom.helper.setNewFormatDate
import com.google.gson.Gson
import java.util.*

class DateAndAmonutAdapter(
    val con: Context,
    var list: ArrayList<DateModelWithName>,
    var edit: String,
    val unCheck:()->Unit = {}
) :
    RecyclerView.Adapter<DateAndAmonutAdapter.MyViewHolder>() {

    var positioncheck:Int = -1

    class MyViewHolder(val mView: NewServiceChargeItemBinding) :
        RecyclerView.ViewHolder(mView.root)

    fun removeItem(list1: ArrayList<DateModelWithName>, position: Int) {
        this.list = list1
        this.positioncheck = position
        if (position==1){
            notifyDataSetChanged()
        }
        notifyItemRemoved(position)
    }


    fun setChecked(ischecked: Boolean, amount: String, value: String) {
        if (ischecked) {
            if (list.isNotEmpty()) {
                val firstItem = list.get(0)
                Log.d("CHECKING_ITEMS", "firstItem: " + Gson().toJson(firstItem))
                for (position in 1 until list.size) {
                    val a = list.get(position)
                    Log.d("CHECKING_ITEMS", "loop: $position" + Gson().toJson(a))
                    a.amount = amount
                    a.dueDate = value
                    notifyItemChanged(position)
                }
            }
        } else {
            if (list.isNotEmpty()) {
                val amount = list.first().amount
                val dueDate = list.first().dueDate
                /* if (amount.isNotEmpty() || dueDate.isNotEmpty()) {
                     for (position in 0 until list.size) {
                         val a = list.get(position)
                         a.amount = amount
                         a.dueDate = dueDate
                         notifyItemChanged(position)
                     }
                 }*/
            }
        }
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(
            NewServiceChargeItemBinding.inflate(
                LayoutInflater.from(con),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (position == 0 && list.size > 1) {
            holder.mView.checkedAllDate.visibility = View.VISIBLE
            holder.mView.checkedAllDate.isChecked = list[position].isChecked
            holder.mView.edtAmount2.doOnTextChanged { text, start, before, count ->
                unCheck.invoke()
                if (position == 0) {
                    holder.mView.checkedAllDate.isChecked = false
                    list[0].amount = holder.mView.edtAmount2.text.trim().toString()
                }
            }
        } else {
            holder.mView.checkedAllDate.visibility = View.GONE
            holder.mView.edtAmount2.doOnTextChanged { text, start, before, count ->
                try {
                    unCheck.invoke()
                    if (list[0].isChecked) {
                        list[0].isChecked = false
                        notifyItemChanged(0)
                    }
                } catch (e: Exception) {

                }
            }
        }

        if(positioncheck == 1){
            holder.mView.checkedAllDate.visibility = View.GONE
        }

        holder.mView.tvFlateName.text = list[position].name
        if (edit == "edit") {
            holder.mView.edtDueDate2.setText(setNewFormatDate(list[position].dueDate))
            holder.mView.edtAmount2.setText(list[position].amount)
        } else {
            holder.mView.edtDueDate2.setText(list[position].dueDate)
            holder.mView.edtAmount2.setText(list[position].amount)

        }

        holder.mView.checkedAllDate.setOnCheckedChangeListener { compoundButton, b ->
            val amount = holder.mView.edtAmount2.text.trim().toString()
            val dueDate = holder.mView.edtDueDate2.text.trim().toString()
            list[position].isChecked = b
            setChecked(b, amount, dueDate)
        }

        holder.mView.edtDueDate2.setOnClickListener {
            if (position == 0) {
                holder.mView.checkedAllDate.isChecked = false
            }
            unCheck.invoke()
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                con, { _: DatePicker, selectedYear: Int, selectedMonth: Int, dayOfMonth: Int ->
                    // Here you can handle the selected date
                    val selectedDate = "$selectedYear/${selectedMonth + 1}/$dayOfMonth"
                    holder.mView.edtDueDate2.setText(selectedDate)
                    list.get(position).dueDate = selectedDate

                },
                year,
                month,
                dayOfMonth
            )

            datePickerDialog.show()
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}