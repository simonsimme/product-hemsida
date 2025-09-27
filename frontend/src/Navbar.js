
import { useTheme } from './ThemeContext';

export default function Navbar({ onNavigate }) {
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
        <button onClick={() => onNavigate('home')} style={{ background: 'none', border: 'none', fontWeight: 'bold', cursor: 'pointer', color: theme === 'dark' ? '#f5f5f5' : '#333' }}>Home</button>
        <button onClick={() => onNavigate('products')} style={{ background: 'none', border: 'none', cursor: 'pointer', color: theme === 'dark' ? '#f5f5f5' : '#333' }}>Products</button>
        <button onClick={() => onNavigate('cart')} style={{ background: 'none', border: 'none', cursor: 'pointer', color: theme === 'dark' ? '#f5f5f5' : '#333' }}>Cart</button>
        <button onClick={() => onNavigate('account')} style={{ background: 'none', border: 'none', cursor: 'pointer', color: theme === 'dark' ? '#f5f5f5' : '#333' }}>Account</button>
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
