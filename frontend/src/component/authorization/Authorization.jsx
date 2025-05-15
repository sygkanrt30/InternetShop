import React, {useState} from 'react';
import './Authorization.css';
import {useNavigate} from 'react-router-dom';
import {useAuth} from '../security/AuthContext';

const Authorization = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [message, setMessage] = useState('');
    const navigate = useNavigate();
    const {login} = useAuth();

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            const response = await fetch('http://localhost:8080/auth/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({username, password}),
            });

            const data = await response.json();

            if (response.ok) {
                localStorage.setItem("username", username)
                login()
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