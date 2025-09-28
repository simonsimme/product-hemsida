import { useTheme } from './ThemeContext';
import { useState, useEffect } from 'react';
import { useAuth } from './AuthContext';

export default function Cart() {
  const { theme } = useTheme();
  const { loggedIn } = useAuth();
  const [orders, setOrders] = useState([]);

  useEffect(() => {
    if (loggedIn) {
      const token = localStorage.getItem('authToken');
      const userId = localStorage.getItem('userId'); 
      console.log('Fetching orders with token:', token);
      fetch(`http://localhost:8082/orders/user/${userId}`, {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      })
        .then((res) => {
          if (!res.ok) throw new Error('Failed to fetch orders');
          return res.json();
        })
        .then((data) => setOrders(data))
        .catch((err) => console.error('Error fetching orders:', err));
    }
  }, [loggedIn]);

  if (!loggedIn) {
    return (
      <div style={{ padding: '2rem', maxWidth: '700px', margin: '0 auto', color: theme === 'dark' ? '#fff' : '#181818' }}>
        <h2>Your Cart</h2>
        <p>Please log in on the <a href="/account">Account</a> page to view your orders.</p>
      </div>
    );
  }

  return (
    <div style={{ padding: '2rem', maxWidth: '700px', margin: '0 auto', color: theme === 'dark' ? '#fff' : '#181818' }}>
      <h2>Your Orders</h2>
      {orders.length === 0 ? (
        <p>No orders found.</p>
      ) : (
        <ul>
          {orders.map((order) => (
            <li key={order.id} style={{ marginBottom: '1rem' }}>
              <h3>Order #{order.id}</h3>
              <ul>
                {(order.items || []).map((item) => (
                  <li key={item.id}>
                    <img src={`http://localhost:8082${item.product.imageUrl}`} alt={item.productName} style={{ width: '50px', height: '50px', marginRight: '10px' }} />
                    {console.log(`http://localhost:8082${item.product.imageUrl}`)}
                    {console.log(order.items)}
                    Product ID: {item.id} - Price: ${item.price} - Quantity: {item.quantity}
                  </li>
                ))}
              </ul>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}
