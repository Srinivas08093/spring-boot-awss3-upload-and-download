package com.s3.sample.awss3uploadanddownload.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;

@Service
public class UploadAndDownloadService {

	private static final Logger logger = LoggerFactory.getLogger(UploadAndDownloadService.class);
	@Autowired
	private AmazonS3 amazonS3;
/*
	@Value("${aws.endpointUrl}")
	private String endpointUrl;*/
	@Value("${aws.bucketName}")
	private String bucketName;

	public String uploadFile(MultipartFile multipartFile) {
		String fileUrl = "";
		try {
			File file = convertMultiPartToFile(multipartFile);
			String fileName = generateFileName(multipartFile);
			fileUrl =  bucketName + "/" + fileName;
			uploadFileTos3bucket(fileName, file);
			file.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileUrl;
	}

	private File convertMultiPartToFile(MultipartFile file) throws IOException {
		File convFile = new File(file.getOriginalFilename());
		FileOutputStream fos = new FileOutputStream(convFile);
		fos.write(file.getBytes());
		fos.close();
		return convFile;
	}

	private String generateFileName(MultipartFile multiPart) {
		return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
	}

	private void uploadFileTos3bucket(String fileName, File file) {

		try {
			System.out.println("UPloading image");
			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentType("image/jpeg");

			PutObjectRequest request = new PutObjectRequest(bucketName, fileName, file);
			request.setMetadata(metadata);
			PutObjectResult putObjectResult = amazonS3.putObject(request);
			
			putObjectResult.getContentMd5();
			
		} catch (AmazonServiceException ase) {

			logger.debug("Caught an AmazonServiceException, which" + " means your request made it "
					+ "to Amazon S3, but was rejected with an error response" + " for some reason.");
			logger.debug("Error Message:    " + ase.getMessage());
			logger.debug("HTTP Status Code: " + ase.getStatusCode());
			logger.debug("AWS Error Code:   " + ase.getErrorCode());
			logger.debug("Error Type:       " + ase.getErrorType());
			logger.debug("Request ID:       " + ase.getRequestId());
		
		} catch (AmazonClientException ace) {
			
			logger.debug("Caught an AmazonClientException, which means" + " the client encountered "
					+ "an internal error while trying to " + "communicate with S3, "
					+ "such as not being able to access the network.");

			logger.debug("Error Message: " + ace.getMessage());
		
		} catch (Exception ex) {
			
			logger.debug("Error Message: " + ex.getMessage());
		}
/*
		amazonS3.putObject(
				new PutObjectRequest(bucketName, fileName, file).withCannedAcl(CannedAccessControlList.PublicRead));*/
	}

	public String deleteFileFromS3Bucket(String fileUrl) {
		String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
		amazonS3.deleteObject(new DeleteObjectRequest(bucketName, fileName));
		return "Successfully deleted";
	}

}
