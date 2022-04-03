package com.psu.econom.aspirant.default_project.application.exception

import com.psu.econom.aspirant.default_project.application.exception.error.IError

/** Класс ошибок, которые могут кидатся при выполнении контроллеров */
open class ControllerException(override val error: IError) : AException(error)