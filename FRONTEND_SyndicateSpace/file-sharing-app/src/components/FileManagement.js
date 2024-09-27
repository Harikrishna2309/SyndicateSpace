import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './FileManagement.css';

const FileManagement = ({ user }) => {
    const [files, setFiles] = useState([]);
    const [error, setError] = useState('');
    const [uploadMessage, setUploadMessage] = useState('');
    const [selectedFile, setSelectedFile] = useState(null);
    const [fileToUpdate, setFileToUpdate] = useState(null); 

    const fetchFiles = async () => {
        try {
            const response = await axios.get(`http://localhost:8081/files/files?username=${user.username}&role=${user.roles[0]}`);
            setFiles(response.data);
        } catch (err) {
            setError('Error fetching files');
            console.error(err);
        }
    };

    useEffect(() => {
        fetchFiles();
    }, [user]);

    const handleFileUpload = async (e) => {
        e.preventDefault();

        if (!selectedFile) {
            setUploadMessage('Please select a file to upload.');
            return;
        }

        const formData = new FormData();
        formData.append('file', selectedFile);
        formData.append('username', user.username);
        formData.append('role', user.roles[0]);

        try {
            await axios.post('http://localhost:8081/files/upload', formData);
            setUploadMessage('File uploaded successfully!');
            fetchFiles(); 
        } catch (err) {
            setUploadMessage('Error uploading file');
            console.error(err);
        }
    };

    const handleUpdateFile = async (fileId, newFile) => {
        if (!newFile) {
            setError('Please select a new file to update.');
            return;
        }

        try {
            const formData = new FormData();
            formData.append('file', newFile);
            formData.append('username', user.username);
            formData.append('role', user.roles[0]);

            await axios.put(`http://localhost:8081/files/update/${fileId}`, formData);
            fetchFiles(); 
        } catch (err) {
            setError('Error updating file');
            console.error(err);
        }
    };

    // Handle File Deletion
    const handleDeleteFile = async (fileId) => {
        try {
            await axios.delete(`http://localhost:8081/files/delete/${fileId}?username=${user.username}&role=${user.roles[0]}`);
            fetchFiles(); 
        } catch (err) {
            setError('Error deleting file');
            console.error(err);
        }
    };

    return (
        <div className="file-management-container">
            <h2>Welcome, {user.username}</h2>
            <h3>Your Files:</h3>

            <form onSubmit={handleFileUpload}>
                <input 
                    type="file" 
                    onChange={(e) => setSelectedFile(e.target.files[0])} 
                    required 
                />
                <button type="submit">Upload File</button>
            </form>
            {uploadMessage && <p className="upload-message">{uploadMessage}</p>}
            {error && <p className="error-message">{error}</p>}
            <ul>
                {files.map(file => (
                    <li key={file.id}>
                        <p>File Name: {file.fileName}</p>
                        <input 
                            type="file" 
                            onChange={(e) => handleUpdateFile(file.id, e.target.files[0])} 
                            placeholder="Choose a new file for update" 
                        />
                        <button onClick={() => handleDeleteFile(file.id)}>Delete</button>
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default FileManagement;
