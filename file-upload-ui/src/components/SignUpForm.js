import React, { useState } from "react";
import "../styles/SignUp.css";
import { useNavigate } from "react-router-dom";
import axios from "axios";

const SignUp = () => {
    const navigate = useNavigate();

    const [formData, setFormData] = useState({
        username: "",
        email: "",
        password: "",
        confirmPwd: ""
    });

    // State for errors
    const [errors, setErrors] = useState({});
    const [serverError, setServerError] = useState("");

    // Handle form input change
    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    // Validate form data before submitting
    const validateForm = () => {
        let errors = {};
        const { username, email, password, confirmPwd } = formData;

        if (!username.trim()) {
            errors.username = "Username is required.";
        }

        if (!email.trim()) {
            errors.email = "Email is required.";
        } else if (!/\S+@\S+\.\S+/.test(email)) {
            errors.email = "Email format is invalid.";
        }

        if (!password) {
            errors.password = "Password is required.";
        } else if (password.length < 6) {
            errors.password = "Password must be at least 6 characters.";
        }

        if (!confirmPwd) {
            errors.confirmPwd = "Confirm Password is required.";
        } else if (password !== confirmPwd) {
            errors.confirmPwd = "Passwords do not match.";
        }

        setErrors(errors);
        return Object.keys(errors).length === 0; // Returns true if no errors
    };

    // Handle form submission
    const handleSubmit = async (e) => {
        e.preventDefault();
        setServerError(""); // Reset server error message


        if (!validateForm()) return; // Stop submission if validation fails

        const { confirmPwd, ...filteredFormData } = formData;

        try {
            const response = await axios.post("http://localhost:8080/api/signup", filteredFormData, {
                headers: { "Content-Type": "application/json" },
            });

            alert("Signup successful! Redirecting...");
            navigate("/"); // Navigate to home only if signup is successful
        } catch (error) {
            console.error("Signup failed:", error);
            setServerError("Failed to create an account. Please try again.");
        }
    };

    return (
        <div className="signup-container">
            <form className="signup-form" onSubmit={handleSubmit}>
                <h2>Sign Up</h2>


                <label htmlFor="username">Username</label>
                <input
                    type="text"
                    name="username"
                    id="username"
                    value={formData.username}
                    onChange={handleChange}
                    placeholder="Enter your username"
                    required
                />
                {errors.username && <p className="error-text">{errors.username}</p>}


                <label htmlFor="email">Email address</label>
                <input
                    type="email"
                    name="email"
                    id="email"
                    value={formData.email}
                    onChange={handleChange}
                    placeholder="Enter your email"
                    required
                />
                {errors.email && <p className="error-text">{errors.email}</p>}


                <label htmlFor="password">Password</label>
                <input
                    type="password"
                    name="password"
                    id="password"
                    value={formData.password}
                    onChange={handleChange}
                    placeholder="Enter your password"
                    required
                />
                {errors.password && <p className="error-text">{errors.password}</p>}


                <label htmlFor="confirmPwd">Confirm Password</label>
                <input
                    type="password"
                    name="confirmPwd"
                    id="confirmPwd"
                    value={formData.confirmPwd}
                    onChange={handleChange}
                    placeholder="Confirm your password"
                    required
                />
                {errors.confirmPwd && <p className="error-text">{errors.confirmPwd}</p>}

                {/* Server Error Message */}
                {serverError && <p className="server-error">{serverError}</p>}


                <button type="submit" className="signup-button">Create Account</button>

                <p className="terms">
                    By Signing up, you agree to <span className="highlight">Terms of Use</span> and <span className="highlight">Privacy Policy</span>
                </p>

                <p className="login-text">
                    Already have an account? <a href="#" className="login-link" onClick={() => navigate("/")}>Login</a>
                </p>
            </form>
        </div>
    );
};

export default SignUp;
