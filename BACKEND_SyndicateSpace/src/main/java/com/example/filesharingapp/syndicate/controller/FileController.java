package com.example.filesharingapp.syndicate.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.example.filesharingapp.syndicate.model.File;
import com.example.filesharingapp.syndicate.service.FileService;

@RestController
@RequestMapping("/files")
public class FileController {

    @Autowired
    private FileService fileService;
    
    @Autowired
    private AmazonS3 amazonS3;  

    @Value("${aws.s3.bucketName}")
    private String bucketName;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("username") String username,
            @RequestParam("role") String role) {
        try {
            String fileName = file.getOriginalFilename();
            
            amazonS3.putObject(bucketName, fileName, file.getInputStream(), null);
            String fileUrl = amazonS3.getUrl(bucketName, fileName).toString();

            fileService.uploadFile(fileName, fileUrl, username, role);
            return new ResponseEntity<>("File uploaded successfully", HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Failed to upload file", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/files")
    public ResponseEntity<List<File>> listFiles(@RequestParam("username") String username, @RequestParam("role") String role) {
        if (role.equalsIgnoreCase("Admin")) {
            return ResponseEntity.ok(fileService.listAllFiles());
        } else {
            return ResponseEntity.ok(fileService.listFilesByUsername(username));
        }
    }
    
    @PutMapping("/update/{fileId}")
    public ResponseEntity<String> updateFile(
            @PathVariable String fileId,
            @RequestParam("file") MultipartFile file,  
            @RequestParam("username") String username,
            @RequestParam("role") String role) {
        
        Optional<File> fileOpt = fileService.getFileById(fileId);
        if (fileOpt.isPresent()) {
            File fileRecord = fileOpt.get();

            if (fileRecord.getOwnerUsername().equals(username) || role.equalsIgnoreCase("Admin")) {
                try {
                    String newFileName = file.getOriginalFilename();
                    amazonS3.putObject(bucketName, newFileName, file.getInputStream(), null);
                    String newFileUrl = amazonS3.getUrl(bucketName, newFileName).toString();

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
