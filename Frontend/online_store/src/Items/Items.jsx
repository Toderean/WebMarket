import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import './Items.css';

const ItemList = () => {
  const [items, setItems] = useState([]);
  const navigate = useNavigate();
  const [userType, setUserType] = useState(Number(localStorage.getItem('userType')) || 0);
  const username = localStorage.getItem('username') || 'User';

  useEffect(() => {
    const fetchItems = async () => {
      try {
        const response = await axios.get('http://localhost:8080/items/all');
        setItems(response.data);
      } catch (error) {
        console.error('Error fetching items:', error);
      }
    };

    fetchItems();
  }, []);

  const logout = async () => {
    try {
      localStorage.clear();
      navigate('/');
    } catch (error) {
      console.error('Error logging out:', error);
    }
  };

  const handleItemClick = (id) => {
    navigate(`/item/${id}`);
  };

  return (
    <div className="container">
      <header className="header">
        <h1>Welcome, {username}!</h1>
      </header>
      <h2>Items List</h2>
      <div className="buttons">
        <button onClick={logout}>Logout</button>
        <button onClick={() => navigate('/cart')}>Cart</button>
        <button onClick={() => navigate('/command-history')}>Command History</button>
        <button onClick={() => navigate('/personal-details')}>Personal Details</button>
        {userType === 1 && <button onClick={() => navigate('/admin')}>Admin Page</button>}
      </div>
      <ul>
        {items.map(item => (
          <li key={item.id}>
            <button onClick={() => handleItemClick(item.id)}>
              <img 
                src={item.poster} 
                alt={item.name} 
                style={{
                  width: 80,
                  height: 80,
                }} 
              />
            </button>
            <br />
            <strong>Name:</strong> {item.name}<br />
            <strong>Quantity:</strong> {item.quantity}<br />
            <strong>Category:</strong> {item.category}<br />
            <strong>Price:</strong> {item.price}<br />
          </li>
        ))}
      </ul>
    </div>
  );
};

export default ItemList;
