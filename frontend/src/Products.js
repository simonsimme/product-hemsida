import React, { useEffect, useState } from 'react';
import { useTheme } from './ThemeContext';

const Products = () => {
    const [products, setProducts] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const { theme } = useTheme();

    useEffect(() => {
        fetch('http://localhost:8081/products')
            .then((res) => {
                if (!res.ok) throw new Error(`Failed to fetch products: ${res.status} ${res.statusText}`);
                const contentType = res.headers.get('content-type');
                if (contentType && contentType.includes('application/json')) {
                    return res.json();
                } else {
                    console.error('Unexpected Content-Type:', contentType);
                    throw new Error('Response is not JSON');
                }
            })
            .then((data) => {
                setProducts(Array.isArray(data) ? data : data.content || []);
                setLoading(false);
            })
            .catch((err) => {
                console.error('Error fetching products:', err);
                setError(err.message);
                setLoading(false);
            });
    }, []);

    if (loading) return <div>Loading products...</div>;
    if (error) return <div>Error: {error}</div>;

    const backgroundColor = theme === 'dark' ? '#121212' : '#ffffff';
    const textColor = theme === 'dark' ? '#ffffff' : '#181818';

    return (
        <div className="products-list" style={{ backgroundColor, color: textColor, padding: '20px' }}>
            <h2>Products</h2>
            
            <div style={{ 
                display: 'grid', 
                gridTemplateColumns: 'repeat(4, 1fr)', 
                gap: '20px' 
            }}>
                {products.map((product) => (
                    <div key={product.id} style={{ 
                        border: '1px solid #ccc', 
                        padding: '16px', 
                        backgroundColor: backgroundColor, 
                        borderRadius: '8px' 
                    }}>
                        <h3>{product.title}</h3>
                        <p>{product.description}</p>
                        {product.imageUrl && (
                            <img src={`http://localhost:8081${product.imageUrl}`} alt={product.title} style={{ width: '50%', height: 'auto', borderRadius: '4px' }} />
                        )}
                        
                        <p><strong>Price:</strong> ${product.price}</p>
                        <p><strong>Stock:</strong> {product.quantity}</p>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default Products;
