import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

const DeleteItem = () => {
  const [itemId, setItemId] = useState('');
  const navigate = useNavigate();

  const handleDelete = async () => {
    try {
      await axios.delete(`http://localhost:8080/items/admin/deleteItems=${itemId}`);
      console.log('Item deleted');
      navigate('/items');
    } catch (error) {
      console.error('Error deleting item:', error);
    }
  };

  return (
    <div>
      <h2>Delete Item</h2>
      <input 
        type="text" 
        value={itemId} 
        onChange={(e) => setItemId(e.target.value)} 
        placeholder="Enter Item ID" 
        required 
      />
      <button onClick={handleDelete}>Delete</button>
    </div>
  );
};

export default DeleteItem;
