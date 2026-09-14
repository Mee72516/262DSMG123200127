package com.example.postres30dias.model

import androidx.annotation.DrawableRes

data class Elemento(
    val dia: Int,
    val titulo: String,
    @DrawableRes val imagenRes: Int,
    val descripcion: String
)