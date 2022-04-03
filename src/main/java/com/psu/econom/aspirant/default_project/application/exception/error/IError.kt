package com.psu.econom.aspirant.default_project.application.exception.error

import org.springframework.http.HttpStatus

interface IError {
    val code: String
    val message: String
    val httpStatus: HttpStatus
}