package com.application.intercom.data.model.local.manager

data class ManagerAddNoticeBoardPostModel(
    var content: String,
    var date: String?=null,
    var images: List<String>,
    var isScheduled: Boolean,
    var title: String,
    var user: String
)