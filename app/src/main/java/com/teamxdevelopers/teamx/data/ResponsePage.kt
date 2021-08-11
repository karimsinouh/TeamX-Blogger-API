package com.teamxdevelopers.teamx.data

data class ResponsePage<T>(
    val kind:String,
    val nextPageToken:String?=null,
    val items:List<T>?= emptyList(),
)
