# Secure File Transfer & Email Communication

This repository contains a **full-stack** web application that allows users to:

1. **Upload and download files** securely with AES-256 encryption  
2. **Send and receive emails** using secure protocols (SMTP/IMAP over SSL/TLS)  
3. Enforce **Role-Based Access Control (RBAC)** for file downloads (admin vs. regular user)  

The project is divided into two main folders:

- **`FileManager/fileManager`** – Spring Boot–based **backend** code  
- **`file-upload-ui`** – React–based **frontend** code  

---

## Table of Contents

- [Overview](#overview)
  - [Key Features](#key-features)
  - [Architecture](#architecture)
- [Tech Stack](#tech-stack)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
  - [Backend Setup](#backend-setup)
  - [Frontend Setup](#frontend-setup)
- [Usage](#usage)
  - [1. Account Creation and Login](#1-account-creation-and-login)
  - [2. File Upload](#2-file-upload)
  - [3. File Download](#3-file-download)
  - [4. Sending Emails](#4-sending-emails)
  - [5. Receiving Emails](#5-receiving-emails)
- [Security Highlights](#security-highlights)
- [Project Structure](#project-structure)
- [Contributors & Roles](#contributors--roles)
- [License](#license)

---

## Overview

This application demonstrates how to build a secure system for **file storage** and **email communication**, leveraging the following security mechanisms:

- **AES-256 Encryption** for files at rest  
- **SSL/TLS** for secure transport (HTTPS, IMAP, SMTP)  
- **Role-Based Access Control** ensuring that only administrators can download all files, while regular users are restricted to their own or permitted files  
- **Secure password hashing** for user credentials  

### Key Features

- **Login/Signup** with hashed passwords and validation  
- **Secure File Transfer** with file encryption (AES-256) before storing on disk  
- **Download Permissions** governed by user role (admin or regular)  
- **Email Sending** (SMTP with SSL/TLS) with optional attachments  
- **Email Receiving** (IMAP with SSL/TLS), filtering incoming emails to current-day only  
- **Neat UI** in React with simple forms for uploading, downloading, sending, and receiving emails  

## Prerequisites

1. **Java 17+** installed for building/running the Spring Boot application  
2. **Node.js 14+** installed for building/running the React application  
3. **PostgreSQL** instance (local or remote)  
   - Make sure you have a database created and credentials ready  
4. **Gmail account** (or another SMTP/IMAP-supported service)  
   - Enable “Less secure apps” or “App Passwords” if needed  
   - Update the application properties with your email and password or an app-specific password  


