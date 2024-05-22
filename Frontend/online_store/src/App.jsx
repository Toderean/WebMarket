import React from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import SignIn from './SignIn/SignIn';
import Register from './Register/Register';
import ItemList from './Items/Items';
import CartPage from './Cart/CartPage';
import ItemPage from './ItemPage/ItemPage';
import CommandHistory from './CommandHistory/CommandHistory';
import AdminPage from './Admin/AdminPage';
import CreateItem from './Admin/CreateItem';
import UpdateItem from './Admin/UpdateItem';
import UpdateUser from './Admin/UpdateUser';
import DeleteItem from './Admin/DeleteItem';
import DeleteUser from './Admin/DeleteUser';
import PersonalDetails from './Details/PersonalDetails';

function App() {
  const isAuthenticated = !!localStorage.getItem('id');

  const handleLoginSuccess = () => {
    return <Navigate to="/items" />;
  };

  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<SignIn onLogInSuccess={handleLoginSuccess}/>} />
        <Route path="/register" element={<Register/>} />
        <Route path="/items" element={<ItemList />} />
        <Route path="/cart" element={<CartPage />} />
        <Route path="/item/:id" element={<ItemPage />} />
        <Route path='/command-history' element={<CommandHistory/>}/>
        <Route path= "/admin" element={<AdminPage/>}/>
        <Route path= "/admin-page/create-item" element={<CreateItem/>}/>
        <Route path= "/admin-page/update-item" element={<UpdateItem/>}/>
        <Route path= "/admin-page/update-user" element={<UpdateUser/>}/>
        <Route path= "/admin-page/delete-item" element={<DeleteItem/>}/>
        <Route path= "/admin-page/delete-user" element={<DeleteUser/>}/>
        <Route path= "/personal-details" element={isAuthenticated ? <PersonalDetails/> : <Navigate to="/" />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
