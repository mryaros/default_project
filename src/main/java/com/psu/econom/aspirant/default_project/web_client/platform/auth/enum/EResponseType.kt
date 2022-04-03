package com.psu.econom.aspirant.default_project.web_client.platform.auth.enum

enum class EResponseType(val code: String) {
    CODE("code"),
    TOKEN("token");

    companion object {
        fun getByCode(code: String?): EResponseType? {
            for (responseType in values()) {
                if (responseType.code == code) {
                    return responseType
                }
            }
            return null
        }
    }
}