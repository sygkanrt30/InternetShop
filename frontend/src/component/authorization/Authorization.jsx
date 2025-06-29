import React, {useState} from 'react';
import './Authorization.css';
import {useNavigate} from 'react-router-dom';
import {useAuth} from '../security/AuthContext';

const Authorization = () => {
    const [username, setUsername] = useState('');
    const LOGIN_URL = 'http://localhost:8080/auth/login';
    const [password, setPassword] = useState('');
    const [message, setMessage] = useState('');
    const navigate = useNavigate();
    const {login} = useAuth();

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            const response = await fetch(LOGIN_URL+ `?username=${encodeURIComponent(username)}&password=${encodeURIComponent(password)}`, {
                method: 'POST',
                credentials: 'include'
            });

            const data = await response.json();

            if (response.ok) {
                login(username)
                navigate("/internet-shop")
            } else {
                setMessage('Ошибка регистрации');
                console.log(data.message)
            }
        } catch (error) {
            setMessage(error.message);
            console.log(error.message);
        }
    };

    const handleRegister = () => {
        navigate("/signup")
    };

    return (
        <div className="container">
            <h2>Войти</h2>
            <form onSubmit={handleSubmit}>
                <div>
                    <label>Имя пользователя:</label>
                    <input
                        type="text"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        required
                    />
                </div>
                <div>
                    <label>Пароль:</label>
                    <input
                        type="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                    />
                </div>
                <button type="submit" className="button-submit">Войти</button>
                <button type="button" className="button-register" onClick={handleRegister}>Зарегистрироваться</button>
            </form>
            {message && <p className="message">{message}</p>}
        </div>
    );
};

export default Authorization;