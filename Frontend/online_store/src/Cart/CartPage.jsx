import React, { useState, useEffect } from 'react';
import axios from 'axios';

const CartPage = () => {
  const [cartItems, setCartItems] = useState([]);

  useEffect(() => {
    const fetchCartItems = async () => {
      try {
        const response = await axios.get(`http://localhost:8080/cart/${localStorage.getItem("username")}`);
        setCartItems(Object.entries(response.data).map(([item, quantity]) => ({ ...item, quantity })));
      } catch (error) {
        console.error('Error fetching cart items:', error);
      }
    };

    fetchCartItems();
  }, []);

  const handleQuantityChange = (itemId, quantity) => {
    setCartItems(prevItems =>
      prevItems.map(item =>
        item.id === itemId ? { ...item, quantity } : item
      )
    );
  };

  const handleDeleteItem = async (itemId) => {
    try {
      await axios.delete(`http://localhost:8080/cart/delete-from-cart/${itemId}/${localStorage.getItem("username")}`);
      setCartItems(prevItems => prevItems.filter(item => item.id !== itemId));
    } catch (error) {
      console.error('Error deleting item from cart:', error);
    }
  };

  const handleSubmitCommand = async () => {
    try {
      await axios.post(`http://localhost:8080/cart/submit-command/${localStorage.getItem("username")}`);
      setCartItems([]);
    } catch (error) {
      console.error('Error submitting command:', error);
    }
  };

  return (
    <div>
      <h2>Cart</h2>
      <ul>
        {cartItems.map(item => {
          console.log(cartItems)
          return(
          <li key={item.id}>
            <img 
              src={item.poster} 
              alt={item.name} 
              style={{
                width: 80,
                height: 80,
              }} 
            />
            <div>
              <strong>Name:</strong> {item.name}<br />
              <label htmlFor={`quantity-${item.id}`}>Quantity:</label>
              <input 
                id={`quantity-${item.id}`} 
                type="number" 
                value={item.quantity} 
                onChange={(e) => handleQuantityChange(item.id, parseInt(e.target.value))}
              />
              <button onClick={() => handleDeleteItem(item.id)}>Delete</button>
            </div>
          </li>)
})}
      </ul>
      <button onClick={handleSubmitCommand}>Submit Command</button>
    </div>
  );
};

export default CartPage;
