package com.example.fileManager.Helper;

import java.sql.*;

public class DBHelper {

    private static final String DB_URL = "";
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

    public boolean AddNewUser(String username, String password, String email){

        String sql = "INSERT INTO users (username, password, email, role) VALUES (?,?,?,?)";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String role = "Regular";

            pstmt.setString(1, username);
            pstmt.setString(2,password);
            pstmt.setString(3,email);
            pstmt.setString(4,role);

            return pstmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }


    public boolean CheckLogin(String username, String pwd){

        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";

        try(Connection connection = DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWORD)){

            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setString(1,username);
            ps.setString(2,pwd);

            ResultSet rs = ps.executeQuery();

            if(rs.next()){

                return true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return  false;

    }

    public String hashedPwd(String username){

        String sql = "SELECT password FROM users WHERE username = ?";

        try(Connection connection = DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWORD)){

            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1,username);

            ResultSet rs = pstmt.executeQuery();

            if(rs.next()){
                return rs.getString("password");
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return null;
    }

    public boolean UserExists(String username, String email){

        String sql = "SELECT * FROM users WHERE username = ? OR email = ?";

        try(Connection conn = DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWORD)){
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1,username);
            pstmt.setString(2,email);

            ResultSet rs = pstmt.executeQuery();

            if(rs.next()){
                return true;
            }
        }catch(SQLException throwables){
            throwables.printStackTrace();
        }
        return false;
    }

    public String CheckUserRole(String username){

        String sql = "SELECT role FROM users WHERE username = ?";

        try(Connection conn = DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWORD)){

            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1,username);

            ResultSet rs = pstmt.executeQuery();

            if(rs.next()){
                return rs.getString("role");
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return "unknown";
    }
}

