import React, { useState } from 'react';
import axios from 'axios';
import './CreateUser.css'; // Import the CSS file

const CreateUser = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [roleNames, setRoleNames] = useState(['']);

  const handleSubmit = async (e) => {
    e.preventDefault();

    const userData = {
      username,
      password,
      roleNames: roleNames.filter(role => role.trim() !== '') 
    };

    try {
      const response = await axios.post('http://localhost:8081/api/users/create', userData, {
        headers: {
          'Content-Type': 'application/json',
        },
      });
      alert(response.data);
    } catch (error) {
      if (error.response) {
        alert(`Error: ${error.response.data}`);
      } else {
        alert('An error occurred while creating the user.');
      }
    }
  };

  const handleRoleChange = (index, value) => {
    const updatedRoles = [...roleNames];
    updatedRoles[index] = value;
    setRoleNames(updatedRoles);
  };

  const addRoleInput = () => {
    setRoleNames([...roleNames, '']);
  };

  return (
    <div className="create-user-container">
      <h2>Create User</h2>
      <form onSubmit={handleSubmit}>
        <div>
          <label>Username:</label>
          <input
            type="text"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            required
          />
        </div>
        <div>
          <label>Password:</label>
          <input
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
        </div>
        <div className="role-inputs">
          <label>Roles:</label>
          {roleNames.map((role, index) => (
            <input
              key={index}
              type="text"
              value={role}
              onChange={(e) => handleRoleChange(index, e.target.value)}
              placeholder={`Role`}
            />
          ))}
          {/* <button type="button" onClick={addRoleInput}>Add Role</button> */}
        </div>
        <button type="submit">Create User</button>
      </form>
    </div>
  );
};

export default CreateUser;
