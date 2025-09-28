import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { useTheme } from './ThemeContext';
import { useAuth } from './AuthContext';

const ProductDetail = () => {
    const { id } = useParams();
    const [product, setProduct] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const { theme } = useTheme();
    const { email } = useAuth(); 

    const userId = localStorage.getItem('userId'); 

    useEffect(() => {
        fetch(`http://localhost:8082/products/${id}`)
            .then((res) => {
                if (!res.ok) throw new Error(`Failed to fetch product: ${res.status} ${res.statusText}`);
                return res.json();
            })
            .then((data) => {
                setProduct(data);
                setLoading(false);
            })
            .catch((err) => {
                console.error('Error fetching product:', err);
                setError(err.message);
                setLoading(false);
            });
    }, [id]);

    const handleAddToCart = () => {
        const token = localStorage.getItem('authToken'); 
        console.log('Token:', token);
        fetch(`http://localhost:8082/order-items/create-or-update/${userId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`,
            },
            body: JSON.stringify({
                productId: product.id,
                quantity: 1, 
            }),
        })
            .then((res) => {
                if (!res.ok) throw new Error('Failed to add item to cart');
                return res.json();
            })
            .then(() => {
                alert('Item added to cart!');
            })
            .catch((err) => {
                console.error('Error adding item to cart:', err);
                alert('Failed to add item to cart.' + err.message);
            });
    };

    if (loading) return <div>Loading product...</div>;
    if (error) return <div>Error: {error}</div>;
    const backgroundColor = theme === 'dark' ? '#12121273' : '#ffffff';
    const textColor = theme === 'dark' ? '#ffffff' : '#181818';

    return (
        <div style={{ padding: '20px', backgroundColor,}}>
            <h2 style={{ color: textColor }}>{product.title}</h2>
            <p style={{ color: textColor }}>{product.description}</p>
            {product.imageUrl && (
                <img src={`http://localhost:8082${product.imageUrl}`} alt={product.title} style={{ width: '10%', height: 'auto', borderRadius: '4px' }} />
            )}
            <p style={{ color: textColor }}><strong>Price:</strong> ${product.price}</p>
            <p style={{ color: textColor }}><strong>Stock:</strong> {product.quantity}</p>
            <button onClick={handleAddToCart} style={{ backgroundColor: 'green', color: 'white' }}>Add to Cart</button>
        </div>
    );
};

export default ProductDetail;
