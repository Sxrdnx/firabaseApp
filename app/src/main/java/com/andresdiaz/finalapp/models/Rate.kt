package com.andresdiaz.finalapp.models

import java.util.*

data class Rate(
    val text: String= "",
    val rate: Float=0f,
    val createdAt: Date= Date(),
    val profileImgURL: String =""
)