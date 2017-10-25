/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Chat;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

/**
 *
 * @author vtahu
 */
public class UpdateTableUser {
    public static void main(String[] args) {
        Connection con = null;

  
        try{
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:SQlite15i1.db");
            
            String sql = "UPDATE TblUser SET password = ? "
                + "WHERE id = ?";
            
            PreparedStatement pstmt = con.prepareStatement(sql);
            
            pstmt.setString(1, "2345");
            pstmt.setInt(2, 2);
            
            pstmt.executeUpdate();
            System.out.println("Sua Database Thanh Cong");
        }catch(Exception e){
            e.printStackTrace();
        }
       
    }
}
