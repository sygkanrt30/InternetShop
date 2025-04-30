import React, {useState} from 'react';
import './Register.css';
import { useNavigate } from 'react-router-dom';

const Register = () => {
    const [username, setUsername] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [message, setMessage] = useState('');
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();

        const role = "USER";
        const userData = {
            username,
            email,
            role,
            password,
            bucketOwner: {
                username: username
            }
        };

        try {
            const response = await fetch('http://localhost:8080/auth/signup', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(userData),
            });

            const data = await response.json();

            if (response.ok) {
                navigate("/internet-shop")
            } else {
                setMessage('Ошибка регистрации');
                console.log(data.message)
            }
        } catch (error) {
            console.log(error.message)
        }
    };

    const handleAuth = () =>{
        navigate("/login")
    };

    return (
        <div className="container">
            <h2>Регистрация</h2>
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
                    <label>Email:</label>
                    <input
                        type="email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
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
                <button type="submit" className="button-submit">Зарегистрироваться</button>
                <button type="button" className="button-enter" onClick={handleAuth}>Авторизоваться</button>

            </form>
            {message && <p className="message">{message}</p>}
        </div>
    );
};

export default Register;