import React, { createContext, useContext, useState, useEffect } from 'react';

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [loggedIn, setLoggedIn] = useState(false);
  const [email, setEmail] = useState('');

  useEffect(() => {
    const savedEmail = localStorage.getItem('loggedInEmail');
    if (savedEmail) {
      setEmail(savedEmail);
      setLoggedIn(true);
    }
  }, []);

  const login = (email) => {
    setEmail(email);
    setLoggedIn(true);
    localStorage.setItem('loggedInEmail', email);
  };

  const logout = () => {
    setEmail('');
    setLoggedIn(false);
    localStorage.removeItem('loggedInEmail');
  };

  return (
    <AuthContext.Provider value={{ loggedIn, email, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);
