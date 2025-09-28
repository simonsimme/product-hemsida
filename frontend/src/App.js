import './App.css';

import Navbar from './Navbar';
import Home from './Home';
import Products from './Products';
import Account from './Account';
import Cart from './Cart';
import { ThemeProvider } from './ThemeContext';
import ProductDetail from './ProductDetail';

import React, { useState } from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';

function App() {

  return (
    <ThemeProvider>
      <Router>
        <div className="App">
          <Navbar /> {}
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/products" element={<Products />} />
            <Route path="/product/:id" element={<ProductDetail />} />
            <Route path="/account" element={<Account />} />
            <Route path="/cart" element={<Cart />} />
          </Routes>
        </div>
      </Router>
    </ThemeProvider>
  );
}

export default App;
