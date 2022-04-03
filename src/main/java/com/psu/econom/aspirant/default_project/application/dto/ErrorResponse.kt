package com.psu.econom.aspirant.default_project.application.dto

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.annotations.ApiModelProperty

class ErrorResponse(
        @JsonProperty
        @ApiModelProperty(value = "error", example = "invalid_request")
        var error: String,
        @JsonProperty
        @ApiModelProperty(value = "error description", example = "Code is not found")
        var errorDescription: String,
)