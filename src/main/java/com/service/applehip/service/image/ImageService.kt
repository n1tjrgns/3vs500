package com.service.applehip.service.image

import com.service.applehip.domain.image.ImageRepository
import com.service.applehip.domain.seq.TableSeqRepository
import com.service.applehip.util.TableName
import com.service.applehip.web.dto.image.ImageSaveRequest
import com.service.applehip.web.dto.image.ImageSaveResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.lang.Exception

@Service
open class ImageService(
        private var imageRepository: ImageRepository,
        private var tableSeqRepository: TableSeqRepository
) {
    @Transactional(rollbackFor = [Exception::class])
    open fun saveImage(request : ImageSaveRequest): ImageSaveResponse {
        val userNo = request.userNo?:0
        if(userNo == 0L) {
            return ImageSaveResponse(detail = "찾을수 없는 사용자입니다.")
        }

        val tableSeqOption = tableSeqRepository.findById(TableName.IMAGE.tableName)
        if(!tableSeqOption.isPresent) {
            return ImageSaveResponse(detail = "테이블 순번이 존재하지 않습니다.")
        }
        val seq = tableSeqOption.get().let {
            val tempSeq = it.getNextSeq()
            this.tableSeqRepository.save(it)
            tempSeq
        }

        val result = this.imageRepository.save(request.toImageEntity(imageNo = seq))
        return result.let {image ->
            image.imageNo?.let { imageNo
                -> ImageSaveResponse(state = "success", detail = "성공", imageNo = imageNo)
            }?: ImageSaveResponse(detail = "저장에 실패하였습니다.")
        }
    }

    open fun getImageUrl(imageId: Long) = this.imageRepository.findById(imageId).let {
        if(it.isPresent) {
            it.get().url
        } else {
            ""
        }
    }
}