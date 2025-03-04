import React from "react";
import EmailInbox from "./EmailInbox";
import EmailForm from "./EmailForm";

const EmailPage = () => {
    return (
        <div>
            <EmailForm />
            <EmailInbox />
        </div>
    );
};

export default EmailPage;
