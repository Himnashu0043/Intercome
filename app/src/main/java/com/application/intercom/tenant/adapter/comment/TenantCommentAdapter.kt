package com.application.intercom.tenant.adapter.comment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.remote.owner.getComment.OwnerGetCommentList
import com.application.intercom.databinding.GalleryPickerBinding
import com.application.intercom.databinding.TenantCommentItemsBinding
import com.application.intercom.helper.GPSService
import com.application.intercom.utils.SessionConstants
import com.application.intercom.utils.loadImagesWithGlideExt


class TenantCommentAdapter(
    val con: Context,
    val list: ArrayList<OwnerGetCommentList.Data>,
    var postStatus: Boolean,
    var onPress: ListClick
) :
    RecyclerView.Adapter<TenantCommentAdapter.MyViewHolder>() {
    class MyViewHolder(val mView: TenantCommentItemsBinding) : RecyclerView.ViewHolder(mView.root)

    /*  var userId =
          prefs.getString(SessionConstants.USERID, GPSService.mLastLocation?.latitude.toString())*/

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(
            TenantCommentItemsBinding.inflate(
                LayoutInflater.from(con),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        println("========postStatus$postStatus")
//        println("========UserId$userId")
        println("======list$list")
        val data = list[position]
        if (!data.comment.isEmpty()) {
            holder.mView.textView68.text = list[position].fromComment!!.fullName
            holder.mView.textView69.text = list[position].comment
            holder.mView.imageView36.loadImagesWithGlideExt(list[position].fromComment!!.profilePic)
        }

        if (postStatus) {
            holder.mView.imageView321.visibility = View.VISIBLE
            if (prefs.getString(
                    SessionConstants.USERID,
                    GPSService.mLastLocation?.latitude.toString()
                ) == list[position].fromComment!!._id
            ) {
                // Toast.makeText(con, "Edit", Toast.LENGTH_SHORT).show()
                holder.mView.imageView321.setOnClickListener {
                    holder.mView.listPop.visibility = View.VISIBLE
                }
                holder.mView.tvEdit.setOnClickListener {
                    onPress.commentEdit(position,list[position]._id,list[position].comment)
                    holder.mView.listPop.visibility = View.GONE

                }
                holder.mView.tvInActive.setOnClickListener {
                    onPress.commentDelete(position,list[position]._id)
                    holder.mView.listPop.visibility = View.GONE
                }
            } else {
                holder.mView.imageView321.setOnClickListener {
                    holder.mView.listPop.visibility = View.VISIBLE
                    holder.mView.tvEdit.visibility = View.GONE
                }
                holder.mView.tvInActive.setOnClickListener {
                    onPress.commentDelete(position,list[position]._id)
                    holder.mView.listPop.visibility = View.GONE
                }
            }
        } else {
            if (prefs.getString(
                    SessionConstants.USERID,
                    GPSService.mLastLocation?.latitude.toString()
                ) == list[position].fromComment!!._id
            ) {
                holder.mView.imageView321.visibility = View.VISIBLE
                holder.mView.imageView321.setOnClickListener {
                    holder.mView.listPop.visibility = View.VISIBLE
                }
                holder.mView.tvEdit.setOnClickListener {
                    onPress.commentEdit(position,list[position]._id,list[position].comment)
                    holder.mView.listPop.visibility = View.GONE
                }
                holder.mView.tvInActive.setOnClickListener {
                    onPress.commentDelete(position,list[position]._id)
                    holder.mView.listPop.visibility = View.GONE
                }
            } else {
                holder.mView.imageView321.visibility = View.INVISIBLE
            }
        }

        /*if (!data.fromComment!!.fullName.isNullOrEmpty()) {
            holder.mView.textView68.text = list[position].fromComment!!.fullName
        } else {
            holder.mView.textView68.text = "--"
        }
        if (!data.comment.isNullOrEmpty()) {
            holder.mView.textView69.text = list[position].comment
        } else {
            holder.mView.textView69.text = "--"
        }
        if (!data.fromComment!!.profilePic.isNullOrEmpty()){
            holder.mView.imageView36.loadImagesWithGlideExt(list[position].fromComment!!.profilePic)
        }*/


    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface ListClick {
        fun commentEdit(position: Int,commentId:String,comment:String)
        fun commentDelete(position: Int,commentId:String)
    }
}