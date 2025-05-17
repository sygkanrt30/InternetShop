import React, { createContext, useContext, useState, useEffect } from 'react';

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
    const [isAuthenticated, setIsAuthenticated] = useState(false);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const username = localStorage.getItem('username');
        if (username) {
            setIsAuthenticated(true);
        }
        setLoading(false); // Завершение загрузки
    }, []);

    const login = (username) => {
        localStorage.setItem('username', username);
        setIsAuthenticated(true);
    };

    const logout = () => {
        localStorage.removeItem('username');
        setIsAuthenticated(false);
    };

    return (
        <AuthContext.Provider value={{ isAuthenticated, loading, login, logout }}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => useContext(AuthContext);