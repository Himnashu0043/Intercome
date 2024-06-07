package com.application.intercom.owner.activity.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.intercom.baseActivity.BaseActivity
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.remote.owner.owner_communityChat.OwnerCommunityChatList
import com.application.intercom.data.model.remote.userCreateRoom.UserPropertyChatList
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.UserHomeRepository
import com.application.intercom.databinding.ActivityOwnerCommunityChatBinding
import com.application.intercom.owner.adapter.chat.OwnerPropertyChatAdapter
import com.application.intercom.user.home.UserHomeFactory
import com.application.intercom.user.home.UserHomeViewModel
import com.application.intercom.utils.*

class OwnerCommunityChatActivity : BaseActivity<ActivityOwnerCommunityChatBinding>() {
    override fun getLayout(): ActivityOwnerCommunityChatBinding {
        return ActivityOwnerCommunityChatBinding.inflate(layoutInflater)
    }

    private lateinit var viewModel: UserHomeViewModel
    private var list = ArrayList<OwnerCommunityChatList.Data8>()
    private var mAdapter: OwnerCommunityChatAdapter? = null
    private var key: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        key = intent.getStringExtra("key").toString()
        println("---key$key")
        initView()
        lstnr()

    }

    private fun initView() {
        binding.ownerChat.tvTittle.text = "Chat"
        initialize()
        observer()
    }

    private fun lstnr() {
        binding.ownerChat.ivBack.setOnClickListener {
            finish()
        }

    }


    private fun initialize() {
        val repo = UserHomeRepository(BaseApplication.apiService)
        viewModel =
            ViewModelProvider(this, UserHomeFactory(repo))[UserHomeViewModel::class.java]
    }

    private fun userRoomList() {
        val token = prefs.getString(
            SessionConstants.TOKEN,
            ""
        )
        viewModel.ownerCommunityChatRoomList(token, "post")

    }

    private fun observer() {
        viewModel.ownerCommunityRoomListLiveData.observe(this, Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.response_code == AppConstants.STATUS_SUCCESS) {
                            list.clear()
                            list.addAll(it.Data)
                            binding.rcyOwnerCommunityChat.layoutManager =
                                LinearLayoutManager(this)
                            mAdapter = OwnerCommunityChatAdapter(this, list, key)
                            binding.rcyOwnerCommunityChat.adapter = mAdapter
                        } else if (it.response_code == AppConstants.STATUS_404) {
                            this.longToast("Data Not Found!!")
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

    override fun onResume() {
        super.onResume()
        userRoomList()
    }
}