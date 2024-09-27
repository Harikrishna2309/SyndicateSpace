package com.example.filesharingapp.syndicate.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

	@Document(collection = "files")
	public class File {

	    @Id
	    private String id;
	    private String fileName;       // Name of the file
	    private String fileUrl;        // URL where the file is stored (e.g., S3 URL)
	    private String ownerUsername;  // Username of the file owner (the uploader)
	    private String role;           // Role of the user who uploaded the file (Designer, Manager, Admin)

	    // Default constructor
	    public File() {
	    }

	    // Constructor to initialize fields
	    public File(String fileName, String fileUrl, String ownerUsername, String role) {
	        this.fileName = fileName;
	        this.fileUrl = fileUrl;
	        this.ownerUsername = ownerUsername;
	        this.role = role;
	        
	        System.out.println("File model initialized");
	    }

	    // Getters and Setters
	    public String getId() {
	        return id;
	    }

	    public void setId(String id) {
	        this.id = id;
	    }

	    public String getFileName() {
	        return fileName;
	    }

	    public void setFileName(String fileName) {
	        this.fileName = fileName;
	    }

	    public String getFileUrl() {
	        return fileUrl;
	    }

	    public void setFileUrl(String fileUrl) {
	        this.fileUrl = fileUrl;
	    }

	    public String getOwnerUsername() {
	        return ownerUsername;
	    }

	    public void setOwnerUsername(String ownerUsername) {
	        this.ownerUsername = ownerUsername;
	    }

	    public String getRole() {
	        return role;
	    }

	    public void setRole(String role) {
	        this.role = role;
	    }
}
