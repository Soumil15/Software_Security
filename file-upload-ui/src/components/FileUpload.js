import React, { useState } from "react";
import { useDropzone } from "react-dropzone";
import axios from "axios";
import { FaFilePdf, FaFileExcel, FaFileImage, FaFileWord, FaFileAlt, FaTimes, FaCheck, FaCloudUploadAlt, FaDownload } from "react-icons/fa";
import "./FileUpload.css";

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

    const { getRootProps, getInputProps } = useDropzone({ onDrop });

    return (
        <div className="app-container dark-theme">
            <nav className="navbar">
                <button className={`nav-btn ${activeTab === "upload" ? "active" : ""}`} onClick={() => setActiveTab("upload")}>
                    <FaCloudUploadAlt /> Upload
                </button>
                <button className={`nav-btn ${activeTab === "download" ? "active" : ""}`} onClick={() => setActiveTab("download")}>
                    <FaDownload /> Download
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
                    <p className="download-subtitle">Select files to download from the server</p>

                    <ul>
                        <li>Hello.txt</li>
                    </ul>
                    {/* Download logic to be implemented soon*/}
                </div>
            )}
        </div>
    );
};

export default FileUpload;