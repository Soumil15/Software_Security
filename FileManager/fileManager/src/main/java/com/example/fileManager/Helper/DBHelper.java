package com.example.fileManager.Helper;

import java.sql.*;

public class DBHelper {

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/{your database name}";
    private static final String DB_USER = ""; // Add your db credentials
    private static final String DB_PASSWORD = "";

    public void saveKeyToDatabase(String fileName, String aesKey) {
        String sql = "INSERT INTO file_storage (file_name, aes_key) VALUES (?, ?) ON CONFLICT (file_name) DO UPDATE SET aes_key = EXCLUDED.aes_key";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, fileName);
            pstmt.setString(2, aesKey);
            pstmt.executeUpdate();
            System.out.println("AES key saved for file: " + fileName);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean checkIfFileExists(String fileName){
        String sql = "SELECT * FROM file_storage WHERE file_name = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1,fileName);
            ResultSet rs = pstmt.executeQuery();

            if(rs.next()){

                return true;
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return false;

    }

    public String retrieveKey(String fileName){

        String sql = "SELECT aes_key FROM file_storage WHERE file_name = ?";
        String aesKey = null;

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1,fileName);
            ResultSet rs = pstmt.executeQuery();

            if(rs.next()){
                aesKey = rs.getString("aes_key");
            }



        }catch (Exception e){
            e.printStackTrace();
        }

        return (aesKey != null) ? aesKey : "No file found";

    }
}

