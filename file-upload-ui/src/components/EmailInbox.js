import React, { useState, useEffect } from "react";
import axios from "axios";
import "./EmailInbox.css";

const EmailInbox = () => {
    const [emails, setEmails] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");

    const fetchEmails = async () => {
        setLoading(true);
        setError("");

        try {
            const response = await axios.get("http://localhost:8080/email/fetchInboxToday");
            setEmails(response.data);
        } catch (error) {
            console.error("Error fetching emails:", error);
            setError("Failed to fetch emails. Please try again.");
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchEmails(); // Auto-fetch emails when the page loads
    }, []);

    return (
        <div className="email-container">
            <h2>📩 Today's Emails</h2>
            <button onClick={fetchEmails} className="refresh-btn">🔄 Refresh</button>

            {loading && <p>Loading emails...</p>}
            {error && <p className="error">{error}</p>}

            <ul className="email-list">
                {emails.length > 0 ? (
                    emails.map((email, index) => (
                        <li key={index} className="email-item">
                            <p><strong>📧 Subject:</strong> {email.subject}</p>
                            <p><strong>👤 From:</strong> {email.sender}</p>
                            <p><strong>📅 Date:</strong> {new Date(email.date).toLocaleString()}</p>
                            <p><strong>📄 Content:</strong> {email.content}</p>
                        </li>
                    ))
                ) : (
                    <p>No new emails today.</p>
                )}
            </ul>
        </div>
    );
};

export default EmailInbox;
