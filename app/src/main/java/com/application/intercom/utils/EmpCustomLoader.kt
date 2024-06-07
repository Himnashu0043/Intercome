package com.application.intercom.utils

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.Uri
import android.provider.ContactsContract
import android.view.Window
import android.view.WindowManager
import androidx.annotation.NonNull
import com.application.intercom.R


class EmpCustomLoader(@NonNull context: Context, theme:Int) :Dialog(context) {
    companion object {
        var loader: EmpCustomLoader? = null

        fun showLoader(activity: Activity?) {
            if (loader == null) loader = show(activity, true)
            try {
                loader?.setCancelable(false)
                loader?.show()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun show(context: Context?, cancelable: Boolean): EmpCustomLoader? {
            val dialog = EmpCustomLoader(context!!, android.R.style.Theme_Black)
            dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
            dialog.window!!.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
            )
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setContentView(R.layout.dialog_loading_box)
            dialog.setCancelable(cancelable)
            dialog.show()
            return dialog
        }

        fun hideLoader() {
            try {
                if (loader != null && loader?.isShowing()!!) {
                    loader?.dismiss()
                    loader = null
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }



        fun firstLetterWord(str: String): String? {
            var result = ""

            // Traverse the string.
            var v = true
            for (i in 0 until str.length) {
                // If it is space, set v as true.
                if (str[i] == ' ') {
                    v = true
                } else if (str[i] != ' ' && v == true) {
                    result += str[i]
                    v = false
                }
            }
            return result
        }

        fun openBrowser(context: Context, link: String) {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
            context.startActivity(browserIntent)
        }

        fun addContact(context: Context,phoneNumber:String){
            val intent = Intent(Intent.ACTION_INSERT)
            intent.type = ContactsContract.RawContacts.CONTENT_TYPE
            intent.putExtra(ContactsContract.Intents.Insert.PHONE, phoneNumber)
            context?.startActivity(intent)
        }

        fun dialContact(context: Context,phoneNumber: String){
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${phoneNumber}")
            context?.startActivity(intent)
        }

        //check internet or not
        fun  isNetworkAvailbale(context: Context):Boolean{
            val conManager = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
            val internetInfo =conManager.activeNetworkInfo
            return internetInfo!=null && internetInfo.isConnected
        }

    }
}