package com.baharudin.pengaduanapp.domain.repository

import com.baharudin.pengaduanapp.data.common.WrappedResponse
import com.baharudin.pengaduanapp.data.dto.ReportDto
import com.baharudin.pengaduanapp.domain.common.BaseResult
import com.baharudin.pengaduanapp.domain.model.ReportModel
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface ReportRepository {
    suspend fun postReport(
        param : HashMap<String, @JvmSuppressWildcards RequestBody>,
        partFile: MultipartBody.Part?,
    ) : Flow<BaseResult<ReportModel, WrappedResponse<ReportDto>>>
}