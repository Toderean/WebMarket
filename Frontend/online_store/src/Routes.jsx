// Routes.jsx
import React from 'react';
import { BrowserRouter as Router, Route } from 'react-router-dom';

import Register from './Register/Register';
import SignIn from './SignIn/SignIn';

const Routes = () => {
  return (
    <Router>
        <Routes>
        <Route path="/register" component={Register} />
        <Route path="/signin" component={SignIn} />
         {/* Add more routes if needed */}
         </Routes>
    </Router>
  );
};

export default Routes;
