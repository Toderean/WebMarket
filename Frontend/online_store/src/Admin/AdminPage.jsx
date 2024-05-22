import React from 'react';
import { useNavigate } from 'react-router-dom';

const AdminPage = () => {
    const navigate = useNavigate();

    return (
      <div>
        <h1>Admin Page</h1>
        <button onClick={() => navigate('/admin-page/create-item')}>Create Item</button>
        <button onClick={() => navigate('/admin-page/update-item')}>Update Item</button>
        <button onClick={() => navigate('/admin-page/delete-item')}>Delete Item</button>
        <button onClick={() => navigate('/admin-page/update-user')}>Update User</button>
        <button onClick={() => navigate('/admin-page/delete-user')}>Delete User</button>
      </div>
    );
};

export default AdminPage;