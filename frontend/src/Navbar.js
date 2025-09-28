import { useTheme } from './ThemeContext';
import { Link } from 'react-router-dom';

export default function Navbar() {
  const { theme, toggleTheme } = useTheme();
  return (
    <nav style={{
      display: 'flex',
      gap: '2rem',
      padding: '1rem',
      background: theme === 'dark' ? '#222' : '#f5f5f5',
      borderBottom: '1px solid #953b3bff',
      alignItems: 'center',
      justifyContent: 'space-between'
    }}>
      <div style={{ display: 'flex', gap: '2rem' }}>
        <Link to="/" style={{ textDecoration: 'none', fontWeight: 'bold', color: theme === 'dark' ? '#f5f5f5' : '#333' }}>Home</Link>
        <Link to="/products" style={{ textDecoration: 'none', color: theme === 'dark' ? '#f5f5f5' : '#333' }}>Products</Link>
        <Link to="/cart" style={{ textDecoration: 'none', color: theme === 'dark' ? '#f5f5f5' : '#333' }}>Cart</Link>
        <Link to="/account" style={{ textDecoration: 'none', color: theme === 'dark' ? '#f5f5f5' : '#333' }}>Account</Link>
      </div>
      <button
        onClick={toggleTheme}
        style={{
          padding: '0.5rem 1rem',
          borderRadius: '4px',
          border: 'none',
          background: theme === 'dark' ? '#444' : '#ddd',
          color: theme === 'dark' ? '#f5f5f5' : '#222',
          cursor: 'pointer',
          fontWeight: 'bold'
        }}
      >
        {theme === 'dark' ? 'Light Mode' : 'Dark Mode'}
      </button>
    </nav>
  );
}
