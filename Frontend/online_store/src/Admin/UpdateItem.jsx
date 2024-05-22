import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

const UpdateItem = () => {
  const [itemId, setItemId] = useState('');
  const [item, setItem] = useState({
    name: '',
    quantity: '',
    category: '',
    price: '',
    poster: ''
  });

  const navigate = useNavigate();

  const handleChange = (e) => {
    const { name, value } = e.target;
    setItem((prevItem) => ({
      ...prevItem,
      [name]: value
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.post(`http://localhost:8080/items/act/id=${itemId}`, item);
      console.log('Item updated:', response.data);
      navigate('/items');
    } catch (error) {
      console.error('Error updating item:', error);
    }
  };

  return (
    <div>
      <h2>Update Item</h2>
      <form onSubmit={handleSubmit}>
        <input 
          type="text" 
          value={itemId} 
          onChange={(e) => setItemId(e.target.value)} 
          placeholder="Enter Item ID" 
          required 
        />
        <input 
          type="text" 
          name="name" 
          value={item.name} 
          onChange={handleChange} 
          placeholder="Name" 
          required 
        />
        <input 
          type="number" 
          name="quantity" 
          value={item.quantity} 
          onChange={handleChange} 
          placeholder="Quantity" 
          required 
        />
        <input 
          type="text" 
          name="category" 
          value={item.category} 
          onChange={handleChange} 
          placeholder="Category" 
          required 
        />
        <input 
          type="number" 
          name="price" 
          value={item.price} 
          onChange={handleChange} 
          placeholder="Price" 
          required 
        />
        <input 
          type="text" 
          name="poster" 
          value={item.poster} 
          onChange={handleChange} 
          placeholder="Poster URL" 
          required 
        />
        <button type="submit">Update</button>
      </form>
    </div>
  );
};

export default UpdateItem;