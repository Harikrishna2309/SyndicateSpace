import React, { useEffect, useState } from 'react';
import axios from 'axios';

const FileList = ({ username, role }) => {
  const [files, setFiles] = useState([]);

  useEffect(() => {
    axios.get(`http://localhost:8081/files?username=${username}&role=${role}`)
      .then(response => {
        setFiles(response.data);
      })
      .catch(error => {
        console.error('Error fetching files:', error);
      });
  }, [username, role]);

  const deleteFile = (fileId) => {
    axios.delete(`http://localhost:8081/files/delete/${fileId}?username=${username}&role=${role}`)
      .then(() => {
        alert('File deleted successfully');
        setFiles(files.filter(file => file.id !== fileId));
      })
      .catch(error => {
        alert('Failed to delete file');
        console.error(error);
      });
  };

  return (
    <div>
      <h3>File List</h3>
      <ul>
        {files.map(file => (
          <li key={file.id}>
            {file.name} 
            <button onClick={() => deleteFile(file.id)}>Delete</button>
          </li>
        ))}
      </ul>
    </div>
  );
};

export default FileList;
