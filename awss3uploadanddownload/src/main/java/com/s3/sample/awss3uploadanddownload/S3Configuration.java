package com.s3.sample.awss3uploadanddownload;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class S3Configuration {

	@Value("${s3bucketAccessKey}")
	private String s3bucketAccessKey;

	@Value("${s3bucketSecreatKey}")
	private String s3bucketSecreatKey;

	@Bean
	public AmazonS3 s3ClientSourceBucket() {
		BasicAWSCredentials provider = new BasicAWSCredentials(s3bucketAccessKey, s3bucketSecreatKey);
		AWSStaticCredentialsProvider credentialsProvider = new AWSStaticCredentialsProvider(provider);

		return AmazonS3ClientBuilder.standard().withCredentials(credentialsProvider).withRegion(Regions.AP_SOUTH_1)
				.build();

	}
}
