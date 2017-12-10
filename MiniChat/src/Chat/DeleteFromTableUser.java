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
public class DeleteFromTableUser {
    public static void main(String[] args) {
        Connection con = null;

  
        try{
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:SQlite15i1.db");
            
            String sql = "DELETE FROM TblUser WHERE id = ?";
            
            PreparedStatement pstmt = con.prepareStatement(sql);
            
            pstmt.setInt(1, 4);
            
            pstmt.executeUpdate();
            System.out.println("Xoa Database Thanh Cong");
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
}
