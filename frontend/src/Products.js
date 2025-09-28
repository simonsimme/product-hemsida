import React, { useEffect, useState } from 'react';
import { useTheme } from './ThemeContext';
import { Link } from 'react-router-dom';

const Products = () => {
    const [products, setProducts] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const { theme } = useTheme();

    useEffect(() => {
        fetch('http://localhost:8082/products')
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
                    <Link to={`/product/${product.id}`} key={product.id} style={{ textDecoration: 'none', color: textColor }}>
                        <div style={{ 
                            border: '1px solid #ccc', 
                            padding: '16px', 
                            backgroundColor: backgroundColor, 
                            borderRadius: '8px',
                            height: '600px',
                            display: 'flex',
                            flexDirection: 'column' 
                        }}>
                            <h3>{product.title}</h3>
                            <p>{product.description}</p>
                            <div style={{ flex: '1', display: 'flex', justifyContent: 'center', alignItems: 'center', marginBottom: '10px' }}>
                                {product.imageUrl && (
                                    <img src={`http://localhost:8082${product.imageUrl}`} alt={product.title} style={{ maxWidth: '100%', maxHeight: '150px', objectFit: 'contain', borderRadius: '4px' }} />
                                )}
                            </div>
                            
                            <p><strong>Price:</strong> ${product.price}</p>
                            <p><strong>Stock:</strong> {product.quantity}</p>
                        </div>
                    </Link>
                ))}
            </div>
        </div>
    );
};

export default Products;
