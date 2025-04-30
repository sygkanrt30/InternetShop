import Register from "./component/register/Register";
import Authorization from "./component/authorization/Authorization"
import {BrowserRouter as Router, Routes, Route} from 'react-router-dom';
import MainPage from "./component/main_page/MainPage";
import { AuthProvider } from './component/authorization/AuthContext';
import ProtectedRoute from './component/authorization/ProtectedRoute';


function App() {
    return (
        <AuthProvider>
            <Router>
                <Routes>
                    <Route path="/login" element={<Authorization />} />
                    <Route path="/signup" element={<Register />} />
                    <Route path="/internet-shop" element={<ProtectedRoute><MainPage /></ProtectedRoute>}/></Routes>
            </Router>
        </AuthProvider>
    );
}

export default App;
