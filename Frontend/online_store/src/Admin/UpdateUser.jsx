import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

const UpdateUser = () => {
  const [userId, setUserId] = useState('');
  const [user, setUser] = useState({
    name: '',
    email: '',
    role: ''
  });

  const navigate = useNavigate();

  const handleChange = (e) => {
    const { name, value } = e.target;
    setUser((prevUser) => ({
      ...prevUser,
      [name]: value
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.post(`http://localhost:8080/users/update/${userId}`, user);
      console.log('User updated:', response.data);
      navigate('/items');
    } catch (error) {
      console.error('Error updating user:', error);
    }
  };

  return (
    <div>
      <h2>Update User</h2>
      <form onSubmit={handleSubmit}>
        <input 
          type="text" 
          value={userId} 
          onChange={(e) => setUserId(e.target.value)} 
          placeholder="Enter User ID" 
          required 
        />
        <input 
          type="text" 
          name="name" 
          value={user.name} 
          onChange={handleChange} 
          placeholder="Name" 
          required 
        />
         <input 
          type="text" 
          name="username" 
          value={user.username} 
          onChange={handleChange} 
          placeholder="Username" 
          required 
        />
        <input 
          type="email" 
          name="email" 
          value={user.email} 
          onChange={handleChange} 
          placeholder="Email" 
          required 
        />
        <input 
          type="password" 
          name="password" 
          value={user.password} 
          onChange={handleChange} 
          placeholder="Password" 
          required 
        />
        <input 
          type="text" 
          name="role" 
          value={user.role} 
          onChange={handleChange} 
          placeholder="Role" 
          required 
        />
        <button type="submit">Update</button>
      </form>
    </div>
  );
};

export default UpdateUser;
