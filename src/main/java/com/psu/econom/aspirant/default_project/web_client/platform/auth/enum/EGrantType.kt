package com.psu.econom.aspirant.default_project.web_client.platform.auth.enum

enum class EGrantType(val code: String) {
    AUTHORIZATION_CODE("authorization_code"),
    PASSWORD("password"),
    REFRESH_TOKEN("refresh_token"),
    CLIENT_CREDENTIALS("client_credentials");

    companion object {
        fun getByCode(code: String): EGrantType? {
            for (grantType in values()) {
                if (grantType.code == code) {
                    return grantType
                }
            }
            return null
        }
    }
}