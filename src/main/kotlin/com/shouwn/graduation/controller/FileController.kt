package com.shouwn.graduation.controller

import com.shouwn.graduation.service.FileStorageService
import org.apache.tika.metadata.HttpHeaders
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.io.IOException
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("api/files")
class FileController @Autowired constructor(
        private val fileStorageService: FileStorageService
){
    @GetMapping("/downloadFile/{fileName:.+}")
    fun downloadFile(@PathVariable fileName: String, request: HttpServletRequest): ResponseEntity<Resource>{
        val resource = fileStorageService.loadFileAsResource(fileName)

        val contentType =
            request.servletContext.getMimeType(resource.file.absolutePath) ?: "application/octet-stream"

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename\"${resource.filename}\"")
                .body(resource)
    }
}