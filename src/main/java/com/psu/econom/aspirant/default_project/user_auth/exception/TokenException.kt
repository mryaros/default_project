package com.psu.econom.aspirant.default_project.user_auth.exception

import com.psu.econom.aspirant.default_project.application.exception.AException
import com.psu.econom.aspirant.default_project.application.exception.error.IError

//TODO этот класс нужен?
/** Класс ошибок, которые могут кидатся при проверке токенов */
open class TokenException(override val error: IError) : AException(error)