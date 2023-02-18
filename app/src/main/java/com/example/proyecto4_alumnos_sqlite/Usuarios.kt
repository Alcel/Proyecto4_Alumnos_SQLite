package com.example.proyecto4_alumnos_sqlite

import android.graphics.Bitmap

data class Usuarios(
    var id:Int?,
    var nombre:String,
    var asig:String,
    var email:String,
    var imagen: ByteArray
):java.io.Serializable
