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
 * @author Dell
 */
public class InsertTableUser {
    public static void main(String[] args) {
        Connection con = null;
  
        try{
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:SQlite15I1.db");

            
            String sql = "INSERT INTO TblUser(username,password) VALUES(?,?)";
            PreparedStatement pstmt = con.prepareStatement(sql);
            
            pstmt.setString(1,"pham");
            pstmt.setString(2, "4567");
            
            pstmt.executeUpdate();
            System.out.println("Them Database Thanh Cong");
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
}
