package com.service.applehip.web.api

import com.service.applehip.service.image.ImageService
import com.service.applehip.web.dto.image.ImageSaveRequest
import io.leangen.graphql.annotations.GraphQLMutation
import io.leangen.graphql.annotations.GraphQLQuery
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi
import org.springframework.stereotype.Controller

@GraphQLApi
@Controller
class ImageApiController(
        private var imageService: ImageService
) {

    @GraphQLMutation(name = "saveImage", description = "이미지를 저장하기 위함. 추후 삭제")
    fun saveImage(request: ImageSaveRequest) = this.imageService.saveImage(request = request)
    
    @GraphQLQuery(name = "getImageUrl", description = "이미지를 호출하기 위함. 이미지 Id를 이용하여 호출")
    fun getImageUrl(imageId: Long?): String = imageId?.let {
        this.imageService.getImageUrl(it)
    }?:"" 
}