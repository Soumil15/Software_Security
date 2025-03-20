import React, { useState } from "react";
import { useDropzone } from "react-dropzone";
import axios from "axios";
import { FaFilePdf, FaFileExcel, FaFileImage, FaFileWord, FaFileAlt, FaTimes, FaCheck, FaCloudUploadAlt, FaDownload, FaEnvelope } from "react-icons/fa";
import "../styles/FileUpload.css";
import { Navigate, useNavigate } from "react-router-dom";

const getFileIcon = (fileName) => {
    const extension = fileName.split(".").pop().toLowerCase();
    switch (extension) {
        case "pdf": return <FaFilePdf className="file-icon pdf" />;
        case "xls": case "xlsx": return <FaFileExcel className="file-icon excel" />;
        case "jpg": case "jpeg": case "png": return <FaFileImage className="file-icon image" />;
        case "doc": case "docx": return <FaFileWord className="file-icon word" />;
        default: return <FaFileAlt className="file-icon default" />;
    }
};

const FileUpload = () => {
    const [files, setFiles] = useState([]);
    const [uploadStatus, setUploadStatus] = useState({});
    const [activeTab, setActiveTab] = useState("upload");
    const [searchQuery, setSearchQuery] = useState("");
    const [downloadStatus, setDownloadStatus] = useState("");

    const onDrop = (acceptedFiles) => {
        setFiles([...files, ...acceptedFiles]);
    };

    const handleUpload = async (file) => {
        const formData = new FormData();
        formData.append("file", file, file.name);

        setUploadStatus((prev) => ({ ...prev, [file.name]: "uploading" }));

        try {
            await axios.post("http://localhost:8080/api/upload", formData, {
                headers: { "Content-Type": "multipart/form-data" },
            });
            setUploadStatus((prev) => ({ ...prev, [file.name]: "success" }));
        } catch (error) {
            setUploadStatus((prev) => ({ ...prev, [file.name]: "error" }));
        }
    };

    const handleDownload = async () => {
        if (!searchQuery.trim()) {
            setDownloadStatus("not_found");
            return;
        }

        setDownloadStatus("downloading");

        try {
            const response = await axios.get(`http://localhost:8080/api/downloads`, {
                params: { filename: searchQuery.trim() },
                responseType: "blob",
                withCredentials: true
            });

            const url = window.URL.createObjectURL(new Blob([response.data]));
            const link = document.createElement("a");
            link.href = url;
            link.setAttribute("download", searchQuery);
            document.body.appendChild(link);
            link.click();
            document.body.removeChild(link);

            setDownloadStatus("success");
        } catch (error) {
            if (error.response) {
                if (error.response.status === 403) {
                    alert("Access Denied! You don't have permission to download this file.");
                } else if (error.response.status === 404) {
                    alert(`File not found: ${searchQuery}`);
                } else {
                    alert(`Download failed: ${error.response.data || "Unknown error"}`);
                }
            } else {
                alert("Network error or server is down.");
            }

            console.error("Download error:", error);
            setDownloadStatus("not_found");
        }
    };



    const { getRootProps, getInputProps } = useDropzone({ onDrop });
    const navigate = useNavigate();

    return (
        <div className="app-container dark-theme">
            <nav className="navbar">
                <button className={`nav-btn ${activeTab === "upload" ? "active" : ""}`} onClick={() => setActiveTab("upload")}>
                    <FaCloudUploadAlt /> Upload
                </button>
                <button className={`nav-btn ${activeTab === "download" ? "active" : ""}`} onClick={() => setActiveTab("download")}>
                    <FaDownload /> Download
                </button>

                <button className="nav-btn" onClick={() => navigate("/email")}>
                    <FaEnvelope /> Email
                </button>
            </nav>

            {activeTab === "upload" && (
                <div className="upload-container">
                    <h3 className="upload-title">Upload files</h3>
                    <p className="upload-subtitle">Select and upload the files of your choice</p>
                    <div {...getRootProps()} className="dropzone">
                        <input {...getInputProps()} />
                        <div className="upload-box">
                            <span className="cloud-icon">☁️</span>
                            <p>Choose a file or drag & drop it here</p>
                            <p className="file-info">JPEG, PNG, PDF, and MP4 formats, up to 50MB</p>
                            <button className="browse-btn">Browse File</button>
                        </div>
                    </div>
                    <div className="file-list">
                        {files.map((file) => (
                            <div key={file.name} className="file-item">
                                {getFileIcon(file.name)}
                                <span className="file-name">{file.name}</span>
                                <button className="upload-btn" onClick={() => handleUpload(file)}>Upload</button>
                                {uploadStatus[file.name] === "uploading" && <span className="status uploading">Uploading...</span>}
                                {uploadStatus[file.name] === "success" && <FaCheck className="status success" />}
                                {uploadStatus[file.name] === "error" && <FaTimes className="status error" />}
                            </div>
                        ))}
                    </div>
                </div>
            )}
            {activeTab === "download" && (
                <div className="download-container">
                    <h3 className="download-title">Download Files</h3>
                    <p className="download-subtitle">Enter the filename to download from the server</p>

                    <div className="input-group">
                        <input
                            type="text"
                            className="search-input"
                            placeholder="Enter file name (e.g., document.pdf)"
                            value={searchQuery}
                            onChange={(e) => setSearchQuery(e.target.value)}
                        />
                        <button className="download-btn" onClick={handleDownload}>
                            Download
                        </button>
                    </div>

                    {downloadStatus === "not_found" && <p className="status error">File not found</p>}
                    {downloadStatus === "downloading" && <p className="status downloading">Downloading...</p>}
                    {downloadStatus === "success" && <p className="status success">Download complete</p>}
                </div>

            )}

        </div>
    );
};

export default FileUpload;