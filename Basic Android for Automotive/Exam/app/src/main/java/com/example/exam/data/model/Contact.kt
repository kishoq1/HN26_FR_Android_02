package com.example.exam.data.model


import java.io.Serializable

data class Contact(
    var id: Int = -1,
    var name: String = "",
    var phone: String = "",
    var email: String = "",
    var avatar: ByteArray? = null
) : Serializable