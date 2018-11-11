package com.shouwn.graduation.controller

import com.shouwn.graduation.service.FileStorageService
import org.springframework.beans.factory.annotation.Autowired

class FileController @Autowired constructor(
        private val fileStorageService: FileStorageService
)