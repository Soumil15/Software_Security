import React from "react";
import { BrowserRouter as Router, Routes, Route, Link } from "react-router-dom";
import FileUpload from "./components/FileUpload";
import EmailPage from "./components/EmailPage";

function App() {
  return (
    <Router>
      <div>
        <nav className="navbar">
          <Link to="/" className="nav-btn">📂 Upload/Download</Link>
          <Link to="/email" className="nav-btn">📩 Email</Link>
        </nav>

        <Routes>
          <Route path="/" element={<FileUpload />} />
          <Route path="/email" element={<EmailPage />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
