package com.example.filesharingapp.syndicate.service;

import com.example.filesharingapp.syndicate.model.File;
import com.example.filesharingapp.syndicate.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FileService {

    @Autowired
    private FileRepository fileRepository;

    public File uploadFile(String fileName, String fileUrl, String ownerUsername, String role) {
        File file = new File(fileName, fileUrl, ownerUsername, role);
        return fileRepository.save(file);  // Save file metadata in MongoDB
    }
    
    public List<File> listAllFiles() {
        return fileRepository.findAll();
    }

    public List<File> listFilesByUsername(String username) {
        return fileRepository.findByOwnerUsername(username);
    }
    
    public File updateFile(String fileId, File newFileDetails) {
        Optional<File> fileOpt = fileRepository.findById(fileId);
        if (fileOpt.isPresent()) {
            File file = fileOpt.get();
            file.setFileName(newFileDetails.getFileName());
            return fileRepository.save(file);
        }
        return null;
    }
    
    public Optional<File> getFileById(String fileId) {
        return fileRepository.findById(fileId);
    }
    
    public void deleteFile(String fileId) {
        fileRepository.deleteById(fileId);
    }
    
}
