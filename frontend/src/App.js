
import './App.css';

import Navbar from './Navbar';
import Home from './Home';
import Products from './Products';
import Account from './Account';
import Cart from './Cart';
import { ThemeProvider } from './ThemeContext';

import React, { useState } from 'react';

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
      <div className="App">
        <Navbar onNavigate={setPage} />
        {content}
      </div>
    </ThemeProvider>
  );
}

export default App;
