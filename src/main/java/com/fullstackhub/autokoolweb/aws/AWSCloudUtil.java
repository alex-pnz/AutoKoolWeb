//package com.fullstackhub.autokoolweb.aws;
//
//import com.amazonaws.auth.AWSCredentials;
//import com.amazonaws.auth.AWSStaticCredentialsProvider;
//import com.amazonaws.auth.BasicAWSCredentials;
//import com.amazonaws.regions.Regions;
//import com.amazonaws.services.s3.AmazonS3;
//import com.amazonaws.services.s3.AmazonS3ClientBuilder;
//
//import java.io.File;
//
//public class AWSCloudUtil {
//
//    private AWSCredentials awsCredentials(String accessKey, String secretKey) {
//        return new BasicAWSCredentials(accessKey,secretKey);
//    }
//
//    private AmazonS3 awsS3ClientBuilder(String accessKey, String secretKey) {
//        return AmazonS3ClientBuilder.standard()
//                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials(accessKey, secretKey)))
//                .withRegion(Regions.DEFAULT_REGION).build();
//    }
//
//    public void uploadFileToS3(String filename, byte[] fileBytes, String accessKey, String secretKey, String bucket) {
//        AmazonS3 s3client = awsS3ClientBuilder(accessKey, secretKey);
//
//        File file = new File()
//    }
//}
