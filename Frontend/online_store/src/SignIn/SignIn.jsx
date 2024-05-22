import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import './SignIn.css'; 

const SignIn = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const queryParams = new URLSearchParams({
        username,
        password,
      }).toString();
      
      const response = await axios.post(`http://localhost:8080/users/login?${queryParams}`);
      console.log(response.data);
      localStorage.setItem("username",username);
      localStorage.setItem("id", response.data.id);
      localStorage.setItem("userType", response.data.userType);
      navigate('/items'); 
      
    } catch (err) {
      setError('Invalid username or password');
    }
  };

  return (
    <div className="container">
      <h2>Sign In</h2>
      {error && <div className="error">{error}</div>}
      <form onSubmit={handleSubmit}>
        <div>
          <label>Username:</label>
          <input
            type="text"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
          />
        </div>
        <div>
          <label>Password:</label>
          <input
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />
        </div>
        <button type="submit">Sign In</button>
        <div className="login-link">
          <p>Not registered? <a href="/register">Just Sign In</a></p>
        </div>
      </form>
    </div>
  );
};

export default SignIn;
