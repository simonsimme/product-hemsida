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
  const [page, setPage] = useState('home');

  let content;
  switch (page) {
    case 'home':
      content = <Home />;
      break;
    case 'products':
      content = <Products />;
      break;
    case 'account':
      content = <Account />;
      break;
    case 'cart':
      content = <Cart />;
      break;
    default:
      content = <Home />;
  }

  return (
    <ThemeProvider>
      <Router>
        <div className="App">
          <Navbar onNavigate={setPage} />
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
