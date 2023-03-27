package com.walker.demo.vcard

import com.walker.core.log.LogHelper
import com.walker.core.util.GsonUtils
import ezvcard.Ezvcard
import ezvcard.VCard
import ezvcard.parameter.AddressType
import ezvcard.parameter.EmailType
import ezvcard.parameter.TelephoneType

class VCardParseImpl : IVCardParser {

    companion object {
        const val TAG = "VCardParseImpl"
        const val PROPERTY_NAME_ID = "ID"
    }

    override fun onParse(data: String): String {
        var result = mutableMapOf<String, String>()
        result[VCardConst.ERR_CODE] = VCardConst.CODE_FAIL
        result[VCardConst.ERR_MSG] = "failed"

        try {
            val goalData = rectifyData(data)
            Ezvcard.parse(goalData).first()?.also {
                LogHelper.get().i(TAG, "$it", true)
                result = mapInfo(it)
            }
        } catch (e: Throwable) {
            LogHelper.get().i(TAG, e.message, true)
            result[VCardConst.ERR_MSG] = e.toString()
        }
        return GsonUtils.toJson(result)
    }

    override fun isValid(data: String) =
        data.contains(VCardConst.FLAG_BEGIN) && data.contains(VCardConst.FLAG_END)

    fun rectifyData(data: String) =
        data.replaceBefore(VCardConst.FLAG_BEGIN, "").replaceAfterLast(VCardConst.FLAG_END, "")

    private fun mapInfo(vcard: VCard): MutableMap<String, String> {
        val result = mutableMapOf<String, String>();

        vcard.getExtendedProperty(PROPERTY_NAME_ID)?.also {
            result[VCardConst.ID] = it.value
        }

        vcard.structuredName?.also {
            var lastName = ""
            it.family?.apply {
                lastName = this
            }

            lastName = if (it.given != null) {
                "${lastName};${it.given}"
            } else {
                "${lastName};"
            }

            it.additionalNames?.forEach { v ->
                if (v != null) {
                    lastName = "${lastName};${v}"
                }
            }
            result[VCardConst.LAST_NAME] = lastName
        }

        vcard.formattedName?.also {
            result[VCardConst.USER_NAME] = it.value
        }

        if (vcard.titles.isNotEmpty()) {
            result[VCardConst.TITLE] = vcard.titles[0].value
        }

        vcard.telephoneNumbers?.apply {
            this.forEachIndexed { index, telephone ->
                if (telephone.parameters.type.toLowerCase() == TelephoneType.WORK.value) {
                    result[VCardConst.TELL] = telephone.text
                } else if (telephone.parameters.type.toLowerCase() == TelephoneType.CELL.value) {
                    result[VCardConst.PHONE] = telephone.text
                } else if (telephone.parameters.type.toLowerCase() == TelephoneType.HOME.value) {
                    result[VCardConst.CALL] = telephone.text
                } else if (telephone.parameters.type.toLowerCase() == TelephoneType.FAX.value) {
                    result[VCardConst.FAX] = telephone.text
                }
            }
        }

        vcard.addresses?.apply {
            this.forEachIndexed { index, address ->
                if (address.parameters.type.toLowerCase() == AddressType.WORK.value) {
                    result[VCardConst.ADDRESS] = address.poBox
                }
            }
        }

        vcard.emails?.apply {
            this.forEachIndexed { index, email ->
                if (email.parameters.type.toLowerCase() == EmailType.INTERNET.value) {
                    result[VCardConst.EMAIL] = email.value
                }
            }
        }

        vcard.organization?.apply {
            if (this.values.isNotEmpty()) {
                result[VCardConst.ORGANIZATION] = this.values[0]
            }
        }
        if (result.isEmpty()) {
            result[VCardConst.ERR_CODE] = VCardConst.CODE_FAIL
            result[VCardConst.ERR_MSG] = "content is empty"
        } else {
            result[VCardConst.ERR_CODE] = VCardConst.CODE_SUCCESS
            result[VCardConst.ERR_MSG] = "success"
        }
        return result
    }

}