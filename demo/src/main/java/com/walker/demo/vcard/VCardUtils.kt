package com.walker.demo.vcard

import com.walker.core.util.GsonUtils

object VCardUtils : IVCardParser by VCardParseImpl() {

    inline fun <reified T> parseData(data: String): T {
        val paresData = onParse(data)
        return GsonUtils.fromLocalJson(paresData, T::class.java)
    }
}