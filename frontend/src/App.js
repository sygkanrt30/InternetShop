import Register from "./component/register/Register";
import Authorization from "./component/authorization/Authorization"
import {BrowserRouter as Router, Navigate, Route, Routes} from 'react-router-dom';
import MainPage from "./component/main_page/MainPage";
import {AuthProvider} from './component/security/AuthContext';
import ProtectedRoute from './component/security/ProtectedRoute';
import Bucket from "./component/bucket/Bucket"


function App() {
    return (
        <AuthProvider>
            <Router>
                <Routes>
                    <Route path="/" element={<Navigate to="/login" replace />} />
                    <Route path="/login" element={<Authorization />} />
                    <Route path="/signup" element={<Register />} />
                    <Route path="/internet-shop" element={<ProtectedRoute><MainPage /></ProtectedRoute>}/>
                    <Route path="/bucket" element={<ProtectedRoute><Bucket /></ProtectedRoute>}/>
                </Routes>
            </Router>
        </AuthProvider>
    );
}

export default App;
