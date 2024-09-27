package com.example.filesharingapp.syndicate.controller;

import com.example.filesharingapp.syndicate.service.FileService;

import org.springframework.beans.factory.annotation.Value;

import com.amazonaws.services.s3.AmazonS3;
import com.example.filesharingapp.syndicate.model.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/files")
public class FileController {

    @Autowired
    private FileService fileService;
    
    @Autowired
    private AmazonS3 amazonS3;  // Injecting the AmazonS3 client

    @Value("${aws.s3.bucketName}")
    private String bucketName;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("username") String username,
            @RequestParam("role") String role) {
        try {
            String fileName = file.getOriginalFilename();
            // Upload to S3 and get the file URL
            amazonS3.putObject(bucketName, fileName, file.getInputStream(), null);
            String fileUrl = amazonS3.getUrl(bucketName, fileName).toString();

            // Save file metadata to MongoDB
            fileService.uploadFile(fileName, fileUrl, username, role);
            return new ResponseEntity<>("File uploaded successfully", HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Failed to upload file", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/files")
    public ResponseEntity<List<File>> listFiles(@RequestParam("username") String username, @RequestParam("role") String role) {
        if (role.equalsIgnoreCase("Admin")) {
            // Admin can view all files
            return ResponseEntity.ok(fileService.listAllFiles());
        } else {
            // Others can only view their own files
            return ResponseEntity.ok(fileService.listFilesByUsername(username));
        }
    }
    
    @PutMapping("/update/{fileId}")
    public ResponseEntity<String> updateFile(
            @PathVariable String fileId,
            @RequestParam("file") MultipartFile file,  // New file to be uploaded
            @RequestParam("username") String username,
            @RequestParam("role") String role) {
        
        Optional<File> fileOpt = fileService.getFileById(fileId);
        if (fileOpt.isPresent()) {
            File fileRecord = fileOpt.get();

            // Check if user has permission to update
            if (fileRecord.getOwnerUsername().equals(username) || role.equalsIgnoreCase("Admin")) {
                try {
                    // Upload the new file to S3
                    String newFileName = file.getOriginalFilename();
                    amazonS3.putObject(bucketName, newFileName, file.getInputStream(), null);
                    String newFileUrl = amazonS3.getUrl(bucketName, newFileName).toString();

                    // Update the file metadata in MongoDB
                    fileRecord.setFileName(newFileName);
                    fileRecord.setFileUrl(newFileUrl);

                    fileService.updateFile(fileId, fileRecord);

                    return new ResponseEntity<>("File updated successfully", HttpStatus.OK);
                } catch (IOException e) {
                    return new ResponseEntity<>("Failed to upload new file", HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } else {
                return new ResponseEntity<>("You don't have permission to update this file", HttpStatus.FORBIDDEN);
            }
        }
        return new ResponseEntity<>("File not found", HttpStatus.NOT_FOUND);
    }

    
    @DeleteMapping("/delete/{fileId}")
    public ResponseEntity<String> deleteFile(
            @PathVariable String fileId,
            @RequestParam("username") String username,
            @RequestParam("role") String role) {
        Optional<File> fileOpt = fileService.getFileById(fileId);
        if (fileOpt.isPresent()) {
            File file = fileOpt.get();

            // Check if user has permission to delete
            if (file.getOwnerUsername().equals(username) || role.equalsIgnoreCase("Admin")) {
                fileService.deleteFile(fileId);
                return new ResponseEntity<>("File deleted successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("You don't have permission to delete this file", HttpStatus.FORBIDDEN);
            }
        }
        return new ResponseEntity<>("File not found", HttpStatus.NOT_FOUND);
    }
    
 }
