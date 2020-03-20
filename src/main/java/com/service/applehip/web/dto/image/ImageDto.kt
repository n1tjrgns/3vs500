package com.service.applehip.web.dto.image

import com.service.applehip.domain.image.Image

// Request
class ImageSaveRequest(
        var userNo : Long?,
        var url : String = ""
) {
    fun toImageEntity(imageNo: Long, awsKey: String = "") = Image(
            url = this.url,
            regUserNo = userNo,
            awsKey = awsKey,
            imageNo = imageNo
    )
}

// Response
class ImageSaveResponse(
        var state: String = "fail",
        var detail: String = "unknown error",
        var imageNo: Long = -1L
)