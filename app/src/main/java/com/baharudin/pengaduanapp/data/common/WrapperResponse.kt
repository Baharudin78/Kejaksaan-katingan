package com.baharudin.pengaduanapp.data.common

import com.google.gson.annotations.SerializedName

data class WrappedResponse<T> (
    @SerializedName("message") var message : String,
    @SerializedName("statusCode") var status : Int,
    @SerializedName("data") var data : T? = null
)