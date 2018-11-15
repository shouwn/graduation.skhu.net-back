package com.shouwn.graduation.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Service
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import javax.annotation.PostConstruct

@Service
class FileStorageService {

    @Value("\${file.dir}")
    private lateinit var dir: String

    private lateinit var fileStorageLocation: Path

    @PostConstruct
    fun init(){
        fileStorageLocation = Paths.get(dir)
                .toAbsolutePath().normalize()

        println(fileStorageLocation)
        Files.createDirectories(this.fileStorageLocation)
    }

    fun loadFileAsResource(fileName: String) =
            UrlResource(this.fileStorageLocation.resolve(fileName).normalize().toUri())
}