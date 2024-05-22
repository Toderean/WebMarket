import React, { useState, useEffect } from 'react';
import { useParams } from "react-router-dom";
import axios from 'axios';
import './ItemPage.css'; // Import the CSS file

const ItemPage = () => {
  const { id } = useParams();
  const [item, setItem] = useState(null);
  const [quantity, setQuantity] = useState(1);

  useEffect(() => {
    axios
      .get(`http://localhost:8080/items/item/id=${id}`) 
      .then((response) => {
        setItem(response.data);
      })
      .catch((error) => {
        console.error("Error fetching item:", error);
      });
  }, [id]);

  const handleAddToCart = async (sessionId) => {
    try {
      await axios.post(`http://localhost:8080/cart/add-to-cart/${id}/${quantity}/${localStorage.getItem("username")}`, {
        id,
        quantity
      });
      alert('Item added to cart successfully!');
    } catch (error) {
      console.error('Error adding item to cart:', error);
    }
  };

  const handleQuantityChange = (e) => {
    setQuantity(parseInt(e.target.value));
  };

  if (!item) {
    return <div className="loading">Loading...</div>;
  }

  return (
    <div className="item-page-container">
      <h2 className="item-name">{item.name}</h2>
      <div className="item-image-container">
        <img 
          src={item.poster} 
          alt={item.name} 
          className="item-image"
        />
      </div>
      <div className="item-details">
        <p className="item-price">Price: ${item.price}</p>
        <p className="item-quantity">Quantity: {item.quantity}</p>
        <label htmlFor="quantity" className="quantity-label">Quantity:</label>
        <input 
          id="quantity" 
          type="number" 
          min="1" 
          value={quantity} 
          onChange={handleQuantityChange} 
          className="quantity-input"
        />
        <button onClick={handleAddToCart} className="add-to-cart-button">Add to Cart</button>
      </div>
    </div>
  );
};

export default ItemPage;
