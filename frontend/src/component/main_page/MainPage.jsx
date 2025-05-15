import React, {useEffect, useState} from 'react';
import './MainPage.css';
import {useNavigate} from "react-router-dom";

const MainPage = () => {
    const [products, setProducts] = useState([]);
    const [userName] = useState(localStorage.getItem("username"));
    const [isLoading, setIsLoading] = useState(true);
    const [successMessage, setSuccessMessage] = useState('');
    const navigate = useNavigate();

    useEffect(() => {
        const fetchProducts = async () => {
            const response = await fetch('http://localhost:8080/products');
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
        const name = localStorage.getItem("username");
        const response = await fetch(`http://localhost:8080/buckets/add-products?username=${encodeURIComponent(name)}&productId=${encodeURIComponent(product.id)}`, {
            method: 'PATCH'
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
                <h1>Добро пожаловать в мой курсовой проект</h1>
                <div className="user-info">
                    <span>nickname: {userName}</span>
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
                            <p>{product.available ? 'В наличии' : 'Нет в наличии'} </p>
                            <button
                                className="add-to-bucket-button"
                                onClick={() => handleAddToBucket(product)}
                                disabled={!product.available}>
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