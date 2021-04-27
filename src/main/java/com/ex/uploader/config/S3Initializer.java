package com.ex.uploader.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


/**
 * Class to initialize s3 client for use in application
 */
public class S3Initializer {
    private BasicAWSCredentials creds; //credentials
    private String region; //region of server

    /*
    Load resources from properties file and initialize
     */
    public S3Initializer(){
        try(InputStream input = getClass().getResourceAsStream("/s3.properties")){
            Properties prop = new Properties();
            prop.load(input);
            creds = new BasicAWSCredentials(prop.getProperty("access-key-id"),prop.getProperty("secret"));
            region = prop.getProperty("region");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Return an s3 Client
     * @return Amazon S3 Client
     */
    public AmazonS3 getS3Client(){
        return AmazonS3ClientBuilder.standard().withRegion(region).withCredentials(new AWSStaticCredentialsProvider(creds)).build();
    }




}
