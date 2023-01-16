package com.baharudin.pengaduanapp.data.respository

import com.baharudin.pengaduanapp.data.common.WrappedResponse
import com.baharudin.pengaduanapp.data.dto.ReportDto
import com.baharudin.pengaduanapp.data.remote.ReportApi
import com.baharudin.pengaduanapp.domain.common.BaseResult
import com.baharudin.pengaduanapp.domain.model.ReportModel
import com.baharudin.pengaduanapp.domain.repository.ReportRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class ReportRepositoryImpl @Inject constructor(
    private val reportApi: ReportApi
) : ReportRepository {
    override suspend fun postReport(
        param: HashMap<String, RequestBody>,
        partFile: MultipartBody.Part?
    ): Flow<BaseResult<ReportModel, WrappedResponse<ReportDto>>> {
        return flow {
            val response = reportApi.postReport(param, partFile!!)
            if (response.isSuccessful) {
                val body = response.body()!!
                val report = ReportModel(
                    body.data?._id.orEmpty(),
                    body.data?.address.orEmpty(),
                    body.data?.category.orEmpty(),
                    body.data?.createdAt.orEmpty(),
                    body.data?.deed.orEmpty(),
                    body.data?.incident_photo.orEmpty(),
                    body.data?.name.orEmpty(),
                    body.data?.phone.orEmpty(),
                    body.data?.reported.orEmpty(),
                    body.data?.work_unit.orEmpty(),
                )
                emit(BaseResult.Success(report))
            }else{
                val type = object : TypeToken<WrappedResponse<ReportDto>>(){}.type
                val err = Gson().fromJson<WrappedResponse<ReportDto>>(response.errorBody()!!.charStream(), type)!!
                err.status = response.code()
                emit(BaseResult.Error(err))
            }
        }
    }
}