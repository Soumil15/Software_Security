import React, { useState } from "react";
import "../styles/Login.css";
import { useNavigate } from "react-router-dom";
import axios from "axios";

const Login = ({ onLogin }) => {
    const navigate = useNavigate();
    const [loginData, setLoginData] = useState({
        username: "",
        password: "",
    });

    const [errors, setErrors] = useState({});
    const [serverError, setServerError] = useState("");

    // Handle input change
    const handleChange = (e) => {
        setLoginData({ ...loginData, [e.target.name]: e.target.value });
    };

    // Validate form data before submitting
    const validateForm = () => {
        let errors = {};
        const { username, password } = loginData;

        if (!username.trim()) {
            errors.username = "Username is required.";
        }
        if (!password.trim()) {
            errors.password = "Password is required.";
        }

        setErrors(errors);
        return Object.keys(errors).length === 0;
    };

    // Handle login submission
    const handleSubmit = async (e) => {
        e.preventDefault();
        setServerError(""); // Reset error

        if (!validateForm()) return;

        try {
            const response = await axios.post("http://localhost:8080/api/login", loginData, {
                headers: { "Content-Type": "application/json" },
                withCredentials: true
            });

            if (response.status === 200) {
                onLogin(); // ✅ Call `onLogin` to set authentication state
                navigate("/upload");
            }
        } catch (error) {
            console.error("Login failed:", error);
            setServerError("Invalid credentials. Please try again.");
        }
    };

    return (
        <div className="login-container">
            <form className="login-form" onSubmit={handleSubmit}>
                <h2>Login</h2>

                <label htmlFor="username">Username</label>
                <input
                    type="text"
                    id="username"
                    name="username" // ✅ Added `name` attribute
                    value={loginData.username}
                    onChange={handleChange}
                    placeholder="Enter your username"
                    required
                />
                {errors.username && <p className="error-text">{errors.username}</p>}

                <label htmlFor="password">Password</label>
                <input
                    type="password"
                    id="password"
                    name="password" // ✅ Added `name` attribute
                    value={loginData.password}
                    onChange={handleChange}
                    placeholder="Enter your password"
                    required
                />
                {errors.password && <p className="error-text">{errors.password}</p>}

                {/* Server Error Message */}
                {serverError && <p className="server-error">{serverError}</p>}

                <button type="submit" className="login-button">Login</button>

                <p className="login-text">
                    Don't have an account? <a href="#" className="signup-link" onClick={() => navigate("/signup")}>Sign Up</a>
                </p>
            </form>
        </div>
    );
};

export default Login;
