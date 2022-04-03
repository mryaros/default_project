package com.psu.econom.aspirant.default_project.user_auth.exception

import com.psu.econom.aspirant.default_project.application.exception.AException
import com.psu.econom.aspirant.default_project.application.exception.error.IError


open class UserException(override val error: IError) : AException(error)