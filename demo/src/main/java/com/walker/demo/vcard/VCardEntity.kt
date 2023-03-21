package com.walker.demo.vcard

data class VCardEntity(
    val errCode:String,
    val errMsg:String,
    val name: String,
    val title: String,
    val phone: String,
    val tell: String,
    val email: String,
    val address: String,
    val organization: String
)
