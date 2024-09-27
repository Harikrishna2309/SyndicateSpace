package com.example.filesharingapp.syndicate.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

	@Document(collection = "files")
	public class File {

	    @Id
	    private String id;
	    private String fileName;       
	    private String fileUrl;        
	    private String ownerUsername;  
	    private String role;           

	    // Default constructor
	    public File() {
	    }

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
