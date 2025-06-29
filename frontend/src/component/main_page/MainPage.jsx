import React, {useEffect, useState} from 'react';
import './MainPage.css';
import {useNavigate} from "react-router-dom";

const MainPage = () => {
    const [products, setProducts] = useState([]);
    const GET_PROD_URL = 'http://localhost:8080/products';
    const ADD_PRODUCT_URL='http://localhost:8080/buckets/add-products';
    const [isLoading, setIsLoading] = useState(true);
    const [successMessage, setSuccessMessage] = useState('');
    const navigate = useNavigate();

    useEffect(() => {
        const fetchProducts = async () => {
            const response = await fetch(GET_PROD_URL, {
                credentials: "include"
            });
            const data = await response.json();
            setProducts(data);
            setIsLoading(false);
        };
        fetchProducts();
    }, []);

    const handleLogout = () => {
        navigate("/login");
        console.log('User logged out');
    };

    const handleAddToBucket = async (product) => {
        const response = await fetch(ADD_PRODUCT_URL + `?productId=${encodeURIComponent(product.id)}`, {
            method: 'PATCH',
            credentials: "include"
        });
        if (response.ok) {
            const data = await response.json();
            console.log(data);
            setSuccessMessage(`Товар "${product.name}" успешно добавлен в корзину`);
            setTimeout(() => {
                setSuccessMessage('');
            }, 2000);
        } else {
            console.error('Ошибка при выполнении запроса:', response.statusText);
        }
        console.log(`Добавлено ${product.name} в корзину`);
    };

    const handleMoveToBucket = () => {
        navigate("/bucket");
    };

    return (
        <div className="main-page">
            <header className="header">
                <h1>Интернет магазин</h1>
                <div className="user-info">
                    <button onClick={handleLogout} className="exit-button">Выйти</button>
                </div>
            </header>

            {successMessage && (
                <div className="success-message">
                    {successMessage}
                </div>
            )}

            <main className="product-list">
                {isLoading ? (
                    <p>Загрузка продуктов...</p>
                ) : (
                    products.map(product => (
                        <div className="product-container" key={product.id}>
                            <h2>{product.name}</h2>
                            <p>Цена: {product.price}</p>
                            <p>{product.description}</p>
                            <p>{product.isAvailable ? 'В наличии' : 'Нет в наличии'} </p>
                            <button
                                className="add-to-bucket-button"
                                onClick={() => handleAddToBucket(product)}
                                disabled={!product.isAvailable}>
                                Добавить в корзину
                            </button>
                        </div>
                    ))
                )}
            </main>


            <footer className="footer">
                <button className="cart-button" onClick={handleMoveToBucket}>Корзина</button>
            </footer>
        </div>
    );
};

export default MainPage;