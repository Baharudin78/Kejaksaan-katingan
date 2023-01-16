package com.baharudin.pengaduanapp.data.remote

import com.baharudin.pengaduanapp.data.common.WrappedResponse
import com.baharudin.pengaduanapp.data.dto.ReportDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap

interface ReportApi {

    @Multipart
    @POST("api/input-pengaduan")
    suspend fun postReport(
        @PartMap param: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part foto : MultipartBody.Part,
    ) : Response<WrappedResponse<ReportDto>>
}