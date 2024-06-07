package com.application.intercom.aws

import android.content.Context
import android.text.TextUtils
import android.util.Log
import com.application.intercom.BuildConfig
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.remote.manager.managerSide.UploadDocumenetRes
import com.application.intercom.helper.GPSService
import com.application.intercom.utils.EmpCustomLoader
import com.application.intercom.utils.SessionConstants
import com.catalyist.aws.AWSListner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import okhttp3.Callback
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Response
import java.io.File


class AWSUtils(
    private val context: Context?,
    private val filePath: String?,
    val listner: AWSListner?
) {
    private var image: File? = null
    /*private var transferUtility: TransferUtility? = null
    private var client: AmazonS3Client? = null*/

    init {
        /*TransferNetworkLossHandler.getInstance(context)
        *//*val credential =
            CognitoCachingCredentialsProvider(context?.applicationContext, IDENTITY_POOL_ID, REGION)*//*
        val credential = BasicAWSCredentials(BuildConfig.ACCESS_KEY, BuildConfig.SECRET_KEY)
        client = AmazonS3Client(credential, Region.getRegion(BuildConfig.REGION))
        val tuOptions = TransferUtilityOptions()
        tuOptions.transferThreadPoolSize = 10
        transferUtility =
            TransferUtility.builder().s3Client(client).context(context?.applicationContext)
                .transferUtilityOptions(tuOptions).build()*/
        startUploading()
    }

    private fun startUploading() {
        CoroutineScope(Dispatchers.Main).launch {
            coroutineScope {
                with(Dispatchers.IO) {
                    listner?.onAWSLoader(true)
                    if (TextUtils.isEmpty(filePath)) {
                        listner?.onAWSLoader(false)
                        listner?.onAWSError("No such file found!")
                    } else {
                        try {
                            image = File(filePath)
                            if (image != null) {
                                val filePart = MultipartBody.Part.createFormData(
                                    "fileName",
                                    image!!.name,
                                    image!!.asRequestBody("image/*".toMediaTypeOrNull())
                                )

                                BaseApplication.apiService.uploadAttachment(
                                    prefs.getString(
                                        SessionConstants.TOKEN, ""
                                    ), filePart
                                )
                                    ?.enqueue(object : retrofit2.Callback<UploadDocumenetRes?> {
                                        override fun onResponse(
                                            call: Call<UploadDocumenetRes?>,
                                            response: Response<UploadDocumenetRes?>
                                        ) {
                                            if (response.isSuccessful) {
                                                listner?.onAWSSuccess(response.body()?.data ?: "")

                                            } else listner?.onAWSError(response.message() ?: "")

                                        }

                                        override fun onFailure(
                                            call: Call<UploadDocumenetRes?>,
                                            t: Throwable
                                        ) {
                                            listner?.onAWSError(t.message ?: "")

                                        }

                                    })
                            }

                            /*val observer = transferUtility?.upload(
                                BuildConfig.BUCKET_NAME,
                                BuildConfig.FOLDER_PATH + image?.name,
                                image
                            )
                            observer?.setTransferListener(
                                AWSCallback(
                                    image,
                                    listner,
                                    folder_path = BuildConfig.FOLDER_PATH + image?.name
                                )
                            )*/

                            //  Log.e("TAG", "laxman: ${BuildConfig.IMAGE_URL + image?.name}")

                        } catch (e: Exception) {
                            listner?.onAWSLoader(false)
                            listner?.onAWSError(e.message ?: "")
                        }
                    }
                }
            }
        }
    }

}