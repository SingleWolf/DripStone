package com.walker.demo.vcard

interface IVCardParser {
    fun onParse(data:String):String

    fun isValid(data:String):Boolean
}