package com.application.intercom.user.service

import com.application.intercom.db.entity.ServicesCategoryTable
import java.text.FieldPosition

interface ServicesCategoryListener {

    fun servicesCategoryClick(isRecentItem : ServicesCategoryTable,position:Int)
}