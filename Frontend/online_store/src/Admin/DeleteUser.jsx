import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

const DeleteUser = () => {
  const [userId, setUserId] = useState('');
  const navigate = useNavigate();

  const handleDelete = async () => {
    try {
      await axios.delete(`http://localhost:8080/users/delete/${userId}`);
      console.log('User deleted', userId);
      navigate('/items');
    } catch (error) {
      console.error('Error deleting user:', error);
    }
  };

  return (
    <div>
      <h2>Delete User</h2>
      <input 
        type="text" 
        value={userId} 
        onChange={(e) => setUserId(e.target.value)} 
        placeholder="Enter User ID" 
        required 
      />
      <button onClick={handleDelete}>Delete</button>
    </div>
  );
};

export default DeleteUser;
