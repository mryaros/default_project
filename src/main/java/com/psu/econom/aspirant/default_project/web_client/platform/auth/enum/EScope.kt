package com.psu.econom.aspirant.default_project.web_client.platform.auth.enum

enum class EScope(val code: String) {
    ALL("ALL");

    companion object {
        fun getByCode(code: String?): EScope? {
            for (scope in values()) {
                if (scope.code == code) {
                    return scope
                }
            }
            if (code == null) {
                return ALL
            }
            return null
        }
    }
}