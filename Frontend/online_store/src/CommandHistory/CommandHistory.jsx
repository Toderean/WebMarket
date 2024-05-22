import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './CommandHistory.css';

const CommandHistory = ({ userId }) => {
    const [items, setItems] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchItems = async () => {
            try {
                const response = await axios.get(`http://localhost:8080/cart/get-command-items/${localStorage.getItem("id")}`);
                setItems(response.data || []); 
                setLoading(false);
            } catch (err) {
                setError(err);
                setLoading(false);
            }
        };

        fetchItems();
    }, [userId]);

    if (loading) return <div>Loading...</div>;
    if (error) return <div>Error: {error.message}</div>;

    return (
        <div className="command-history-container"> 
            <h1>Command History</h1>
            {items.length === 0 ? (
                <p>No items found.</p>
            ) : (
                <ul>
                    {items.map((item, index) => (
                        <li key={index}>
                            <div>
                                <img src={item.poster} alt={item.name} width="50" height="50" />
                                <p>{item.name}</p>
                                <p>Quantity: {item.quantity}</p>
                                <p>Price: ${item.price.toFixed(2)}</p>
                            </div>
                        </li>
                    ))}
                </ul>
            )}
            <div className="total-price"> 
                <h2>Total Price: ${items.reduce((total, item) => total + (item.price * item.quantity), 0).toFixed(2)}</h2>
            </div>
        </div>
    );
};

export default CommandHistory;
