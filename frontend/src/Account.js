import React, { useState, useEffect } from 'react';
import { useTheme } from './ThemeContext';
import { useAuth } from './AuthContext';

export default function Account() {
  const { theme } = useTheme();
  const { loggedIn, email, login, logout, validateToken } = useAuth();
  const [mode, setMode] = useState('login');
  const [password, setPassword] = useState('');
  const [message, setMessage] = useState('');
  const [loading, setLoading] = useState(false);
  const [inputEmail, setInputEmail] = useState(email);

  useEffect(() => {
    if (loggedIn && validateToken) {
      //  token validation (every 5 minutes)

      const interval = setInterval(() => {
        validateToken();
      }, 5 * 60 * 1000); // 5 minutes

      return () => clearInterval(interval);
    }
  }, [loggedIn, validateToken]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setMessage('');
    const endpoint = mode === 'login' ? '/api/auth/login' : '/api/auth/register';
    try {
      const res = await fetch(`http://localhost:8082${endpoint}` , {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email: inputEmail, password })
      });
      if (!res.ok) throw new Error('Failed to ' + mode);
      const data = await res.json();
      setMessage(mode === 'login' ? 'Login successful!' : 'Registration successful!');
      if (mode === 'login') {
        login(inputEmail); 
        localStorage.setItem('authToken', data.accessToken);
        localStorage.setItem('userId', data.userId);
        console.log('Stored token:', data);
      }
    } catch (err) {
      setMessage('Error: ' + err.message);
    }
    setLoading(false);
  };

  if (loggedIn) {
    return (
      <div style={{ padding: '2rem', maxWidth: '400px', margin: '0 auto', color: theme === 'dark' ? '#fff' : '#181818' }}>
        <h2>Welcome, {email}!</h2>
        <p>You are now logged in.</p>
        <button onClick={logout} style={{ marginTop: '1rem' }}>Logout</button>
      </div>
    );
  }

  return (
    <div style={{ padding: '2rem', maxWidth: '400px', margin: '0 auto', color: theme === 'dark' ? '#fff' : '#181818' }}>
      <h2>{mode === 'login' ? 'Login' : 'Register'}</h2>
      <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
        <input
          type="email"
          placeholder="Email"
          value={inputEmail} 
          onChange={e => setInputEmail(e.target.value)} 
          required
        />
        <input
          type="password"
          placeholder="Password"
          value={password}
          onChange={e => setPassword(e.target.value)}
          required
        />
        <button type="submit" disabled={loading}>
          {loading ? 'Loading...' : mode === 'login' ? 'Login' : 'Register'}
        </button>
      </form>
      <button onClick={() => setMode(mode === 'login' ? 'register' : 'login')} style={{ marginTop: '1rem' }}>
        {mode === 'login' ? 'Switch to Register' : 'Switch to Login'}
      </button>
      {message && <p>{message}</p>}
    </div>
  );
}
