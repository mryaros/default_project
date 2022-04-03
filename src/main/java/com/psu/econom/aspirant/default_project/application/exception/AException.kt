package com.psu.econom.aspirant.default_project.application.exception

import com.psu.econom.aspirant.default_project.application.exception.error.IError

abstract class AException(open val error: IError) : Exception()