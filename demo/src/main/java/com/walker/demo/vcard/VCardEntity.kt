package com.walker.demo.vcard

data class VCardEntity(
    val errCode: String="",
    val errMsg: String="",
    val id: String = "",
    val userName: String = "",
    val lastName: String = "",
    val title: String = "",
    val phone: String = "",
    val tell: String = "",
    val call: String = "",
    val fax: String = "",
    val email: String = "",
    val address: String = "",
    val organization: String = ""
) {
    fun isOk(): Boolean {
        return errCode == VCardConst.CODE_SUCCESS
    }
}