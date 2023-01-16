package com.baharudin.pengaduanapp.domain.model

data class ReportModel(
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
