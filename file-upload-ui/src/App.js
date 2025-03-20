import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import { useState } from "react";
import Login from "./components/Login";
import SignUp from "./components/SignUpForm";
import FileUpload from "./components/FileUpload";
import EmailPage from "./components/EmailPage";

function App() {
  const [authenticated, setIsAuthenticated] = useState(false);

  // Function to handle login (passed as a prop to Login)
  const handleLogin = () => {
    setIsAuthenticated(true);
  };

  return (
    <Router>
      <Routes>
        <Route path="/" element={authenticated ? <Navigate to="/upload" /> : <Login onLogin={handleLogin} />} />


        <Route path="/upload" element={authenticated ? <FileUpload /> : <Navigate to="/" />} />
        <Route path="/email" element={authenticated ? <EmailPage /> : <Navigate to="/" />} />

        <Route path="/signup" element={<SignUp />} />
      </Routes>
    </Router>
  );
}

export default App;
