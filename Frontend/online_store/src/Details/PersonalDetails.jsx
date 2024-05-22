import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './PersonalDetails.css';

const PersonalDetails = () => {
    const [user, setUser] = useState({
        username: '',
        email: '',
        name: '',
        password: '',
    });
    const [isEditable, setIsEditable] = useState(false);

    useEffect(() => {
        const fetchUserDetails = async () => {
            try {
                const response = await axios.get(`http://localhost:8080/users/user/${localStorage.getItem("id")}`);
                setUser(response.data);
            } catch (error) {
                console.error('Error fetching user details:', error);
            }
        };

        fetchUserDetails();
    }, []);

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setUser({ ...user, [name]: value });
    };

    const toggleEdit = () => {
        setIsEditable(!isEditable);
    };

    const saveChanges = async () => {
        try {
            await axios.post(`http://localhost:8080/users/update/${localStorage.getItem("id")}`, user);
            alert('User details updated successfully!');
            setIsEditable(false);
        } catch (error) {
            console.error('Error updating user details:', error);
            alert('Failed to update user details.');
        }
    };

    return (
        <div className="personal-details-container">
            <h1>Personal Details</h1>
            <form>
                <div className="form-group">
                    <label>Username:</label>
                    <input
                        type="text"
                        name="username"
                        value={user.username}
                        onChange={handleInputChange}
                        readOnly={!isEditable}
                    />
                </div>
                <div className="form-group">
                    <label>Email:</label>
                    <input
                        type="email"
                        name="email"
                        value={user.email}
                        onChange={handleInputChange}
                        readOnly={!isEditable}
                    />
                </div>
                <div className="form-group">
                    <label>Name:</label>
                    <input
                        type="text"
                        name="name"
                        value={user.name}
                        onChange={handleInputChange}
                        readOnly={!isEditable}
                    />
                </div>
                <div className="form-group">
                    <label>Password:</label>
                    <input
                        type="password"
                        name="password"
                        value={user.password}
                        onChange={handleInputChange}
                        readOnly={!isEditable}
                    />
                </div>
                <div className="button-group">
                    <button type="button" onClick={toggleEdit}>
                        {isEditable ? 'Cancel' : 'Edit'}
                    </button>
                    {isEditable && (
                        <button type="button" onClick={saveChanges} className="save-button">
                            Save
                        </button>
                    )}
                </div>
            </form>
        </div>
    );
};

export default PersonalDetails;
