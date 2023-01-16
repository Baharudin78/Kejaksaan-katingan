package com.baharudin.pengaduanapp.domain.usecase

import com.baharudin.pengaduanapp.data.common.WrappedResponse
import com.baharudin.pengaduanapp.data.dto.ReportDto
import com.baharudin.pengaduanapp.domain.common.BaseResult
import com.baharudin.pengaduanapp.domain.model.ReportModel
import com.baharudin.pengaduanapp.domain.repository.ReportRepository
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class ReportUseCase @Inject constructor(
    private val repository : ReportRepository
) {
    suspend fun invoke(param : HashMap<String, @JvmSuppressWildcards RequestBody>, partFile : MultipartBody.Part?): Flow<BaseResult<ReportModel, WrappedResponse<ReportDto>>> {
        return repository.postReport(param, partFile)
    }
}