import React, { createContext, useContext, useState, useEffect } from 'react';

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [loggedIn, setLoggedIn] = useState(false);
  const [email, setEmail] = useState('');

  const login = (email) => {
    setEmail(email);
    setLoggedIn(true);
    localStorage.setItem('loggedInEmail', email);
  };

  const logout = () => {
    setEmail('');
    setLoggedIn(false);
    localStorage.removeItem('loggedInEmail');
    localStorage.removeItem('authToken');
    localStorage.removeItem('userId');
  };

  const validateToken = async () => {
    const token = localStorage.getItem('authToken');
    if (!token) {
      logout();
      return false;
    }

    try {
      const response = await fetch('http://localhost:8082/api/auth/validate', {
        method: 'GET',
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        }
      });

      if (!response.ok) {
        logout();
        return false;
      }

      return true;
    } catch (error) {
      console.error('Token validation failed:', error);
      logout();
      return false;
    }
  };

  useEffect(() => {
    const checkAuth = async () => {
      const savedEmail = localStorage.getItem('loggedInEmail');
      const token = localStorage.getItem('authToken');
      
      if (savedEmail && token) {
        setEmail(savedEmail);
        setLoggedIn(true);
        // Only validate token on startup if you want strict validation
        // For now, trust the stored token until periodic validation runs
      }
    };
    
    checkAuth();
  }, []);

  return (
    <AuthContext.Provider value={{ loggedIn, email, login, logout, validateToken }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);
