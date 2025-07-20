import React, {useEffect, useState} from 'react';
import {useNavigate} from "react-router-dom";
import './Bucket.css';
import {useAuth} from "../security/AuthContext";

const Bucket = () => {
    const [products, setProducts] = useState();
    const username = localStorage.getItem("username");
    const PARENT_URL = `http://localhost:8080/buckets/`;
    const GET_BUCKET_URL = PARENT_URL + `get`;
    const CLEAR_BUCKET_URl = PARENT_URL + 'clear-bucket';
    const REMOVE_PRODUCT_URl = PARENT_URL + 'remove-product';
    const REMOVE_ALL_PRODUCT_THIS_TYPE_URl = PARENT_URL + 'remove-all-products-this-type';
    const CREATE_ORDER_URl = 'http://localhost:8080/order/create-order';
    const [isLoading, setIsLoading] = useState(true);
    const {isAuthenticated} = useAuth();
    const navigate = useNavigate();

    useEffect(() => {
        if (!isAuthenticated) {
            navigate("/login");
            return;
        }

        const fetchProducts = async () => {
            const response = await fetch(GET_BUCKET_URL, {
                credentials: "include"
            });
            const data = await response.json();
            setProducts(data);
            setIsLoading(false);
        };

        fetchProducts();
    }, [username, isAuthenticated, navigate]);

    const handleRemoveProduct = async (id) => {
        const response = await fetch(
            REMOVE_ALL_PRODUCT_THIS_TYPE_URl + `?productId=${encodeURIComponent(id)}`,
            {
                method: 'PATCH',
                credentials: "include"
            });
        logError(response)
        handleRefresh()
    };

    const logError = (response) => {
        if (!response.ok) {
            console.error('Ошибка при выполнении запроса:', response.statusText);
        }
    }

    const handleRefresh = () => {
        window.location.assign("http://localhost:3000/bucket");
    };

    const handleRemoveOne = async (id) => {
        const response = await fetch(REMOVE_PRODUCT_URl + `?productId=${encodeURIComponent(id)}`,
            {
                method: 'PATCH',
                credentials: "include"
            });
        logError(response)
        handleRefresh()
    };

    const handleClearBucket = async () => {
        const response = await fetch(CLEAR_BUCKET_URl,
            {
                method: 'PATCH',
                credentials: "include"
            });
        logError(response)
        setProducts([]);

    };

    const handleOrder = async () => {
        const response = await fetch(CREATE_ORDER_URl,
            {
                method: 'POST',
                credentials: "include"
            });
        logError(response)
        setProducts([])
    };

    const handleComeback = () => {
        navigate("/internet-shop")
    };
    return (
        <div className="bucket-container">
            <h1>Корзина</h1>
            <main className="products-list">
                {isLoading ? (
                    <p>Загрузка продуктов...</p>
                ) : (
                    products && Object.entries(products).map(([stringProduct, quantity]) => {
                        const productDetails = stringProduct.split(",");
                        const productId = productDetails[0].substring(22);
                        const productName = productDetails[1].substring(6);
                        const isAvailable = productDetails[2].substring(13);
                        const productPrice = parseInt(productDetails[3].substring(7));

                        return (
                            <div className="product-container" key={productId}>
                                <h2>{productName}</h2>
                                <p>Цена: {productPrice}₽</p>
                                <p>{isAvailable ? 'В наличии' : 'Нет в наличии'}</p>
                                <p>Количество в корзине: {quantity}</p>
                                <button className="remove-button" onClick={() => handleRemoveProduct(productId)}>
                                    Удалить из корзины
                                </button>
                                <button className="remove-one-button" onClick={() => handleRemoveOne(productId)}>
                                    Удалить одну штуку
                                </button>
                            </div>
                        );
                    })
                )}
            </main>
            <h2 className="total-sum">
                Итоговая сумма: {products ? Object.entries(products).reduce((total, [stringProduct, quantity]) => {
                const productPrice = parseFloat(stringProduct.split(",")[3].substring(7));
                return total + (productPrice * quantity);
            }, 0) : 0}₽
            </h2>
            <button className="clear-button" onClick={handleClearBucket}>Очистить корзину</button>
            <button className="order-button" onClick={handleOrder}>Заказать/Приход</button>
            <button className="comeback-button" onClick={handleComeback}>Назад</button>
        </div>
    );
};

export default Bucket;