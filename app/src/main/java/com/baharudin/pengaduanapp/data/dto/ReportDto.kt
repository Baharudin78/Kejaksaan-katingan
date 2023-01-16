package com.baharudin.pengaduanapp.data.dto

import com.baharudin.pengaduanapp.domain.model.ReportModel

data class ReportDto(
    val _id: String,
    val address: String,
    val category: String,
    val createdAt: String,
    val deed: String,
    val incident_photo: String,
    val name: String,
    val phone: String,
    val reported: String,
    val work_unit: String
)

fun ReportDto.toReportModel() : ReportModel {
    return ReportModel(
        _id, address, category, createdAt, deed, incident_photo, name, phone, reported, work_unit
    )
}
