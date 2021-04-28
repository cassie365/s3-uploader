package com.ex.uploader;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.ex.uploader.config.S3Initializer;

import java.io.File;
import java.util.Scanner;

/*
Accepts file path from user and uploads file to s3 bucket
 */
public class Uploader {
    private static String bucketName = "cc-file-repo";
    private static String folderName = "folder1";
    private static String uploadFolderName = "upload";


    public static void main(String[] args) {
        S3Initializer initializer = new S3Initializer();
        AmazonS3 client = initializer.getS3Client();
        Scanner s = new Scanner(System.in);

        boolean quit = false;

        System.out.println("WELCOME TO UPLOADER");
        System.out.println("------------------------------------------");
        System.out.println("For help enter 'help'");
        while(!quit){
            System.out.print("\n: ");
            String cmd = s.nextLine();

            switch(cmd){
                case "upload":
                    upload(s,client);
                    break;
                case "help":
                    help();
                    break;
                case "exit":
                    System.out.println("Exiting...");
                    quit = true;
                    break;
                case "view":
                    getFiles(client);
                    break;
                default:
                    System.out.println("Unrecognized commands "+cmd);
            }
        }
    }

    private static void upload(Scanner s, AmazonS3 client){
        System.out.print("Enter the file to upload with extension: ");
        String file = s.nextLine();

        String fileNameinS3 = file;
        String fileNameinLocalPC = uploadFolderName+"/"+file;

        PutObjectRequest request = new PutObjectRequest(bucketName,folderName+"/"+fileNameinS3,new File(fileNameinLocalPC));
        client.putObject(request);
        System.out.println("\nComplete!");
    }

    private static void getFiles(AmazonS3 client){
        System.out.println("Files in folder:");
        System.out.println("NAME    SIZE");
        ListObjectsRequest request = new ListObjectsRequest().withBucketName(bucketName).withPrefix(folderName);
        ObjectListing objects = client.listObjects(request);
        if(objects.getObjectSummaries().isEmpty())
            System.out.println("Empty...");
        else{
            for(S3ObjectSummary s : objects.getObjectSummaries()){
                System.out.println(s.getKey()+" "+s.getSize());
            }
        }
    }

    private static void help(){
        System.out.println("Place file to be uploaded in directory 'upload'\n");
        System.out.println("COMMANDS: \n exit - exit application" +
                "\n help - view commands and tips" +
                "\n upload - upload a file" +
                "\n view - view all files in folder");
    }
}
