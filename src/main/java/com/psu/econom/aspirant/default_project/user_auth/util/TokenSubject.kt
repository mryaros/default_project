package com.psu.econom.aspirant.default_project.user_auth.util

class TokenSubject(
        var username: String = "",
        var roles: List<String> = emptyList(), //TODO роли нужны? Вынести в ERole
)