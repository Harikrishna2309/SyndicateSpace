import React, { useState } from 'react';
import { BrowserRouter as Router, Route, Routes, Link } from 'react-router-dom';
import Login from './components/Login';
import UserCreation from './components/UserCreation';
import FileManagement from './components/FileManagement';

const App = () => {
    const [user, setUser] = useState(null);

    const handleLoginSuccess = (userData) => {
        setUser(userData);  
    };

    return (
        <Router>
            <div className="App">
                <h1>File Management Application</h1>

                {/* Navigation links for user management and file management */}
                <nav>
                    <ul>
                        <li><Link to="/">Home</Link></li>
                        <li><Link to="/create-user">Create User</Link></li>
                    </ul>
                </nav>

                {/* Conditional rendering based on user authentication */}
                {user ? (
                    <FileManagement user={user} />
                ) : (
                    <Login onLoginSuccess={handleLoginSuccess} />
                )}

                <Routes>
                    {/* Add routing for UserCreation component */}
                    <Route path="/create-user" element={<UserCreation />} />
                </Routes>
            </div>
        </Router>
    );
};

export default App;
