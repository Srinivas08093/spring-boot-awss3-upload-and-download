package com.s3.sample.awss3uploadanddownload.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.s3.sample.awss3uploadanddownload.service.UploadAndDownloadService;

@RestController
public class UploadAndDownloadController {

	UploadAndDownloadService uploadAndDownloadService;

	@PostMapping("/uploadFile")
	public String uploadFile(@RequestPart(value = "file") MultipartFile file) {
		return uploadAndDownloadService.uploadFile(file);
	}

	@DeleteMapping("/deleteFile")
	public String deleteFile(@RequestPart(value = "url") String fileUrl) {
		return uploadAndDownloadService.deleteFileFromS3Bucket(fileUrl);
	}

}
