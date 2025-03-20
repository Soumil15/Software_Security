import React, { useState } from "react";
import axios from "axios";
import "../styles/EmailForm.css";

const EmailForm = () => {
    const [formData, setFormData] = useState({
        to: "",
        subject: "",
        body: "",
        file: null
    });

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    const handleFileChange = (e) => {
        setFormData({ ...formData, file: e.target.files[0] });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        const data = new FormData();
        data.append("to", formData.to);
        data.append("subject", formData.subject);
        data.append("body", formData.body);
        if (formData.file) {
            data.append("file", formData.file);
        }

        try {
            const response = await axios.post("http://localhost:8080/email/sendWithAttachment", data);
            alert(response.data);
        } catch (error) {
            console.error("Error sending email:", error);
            alert("Failed to send email.");
        }
    };

    return (
        <div className="email-form">
            <h2>📨 Send Email</h2>
            <form onSubmit={handleSubmit}>
                <input type="email" name="to" placeholder="Recipient Email" onChange={handleChange} required />
                <input type="text" name="subject" placeholder="Subject" onChange={handleChange} required />
                <textarea name="body" placeholder="Message" onChange={handleChange} required></textarea>
                <input type="file" onChange={handleFileChange} />
                <button type="submit">Send Email</button>
            </form>
        </div>
    );
};

export default EmailForm;
