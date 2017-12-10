/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Chat;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.swing.text.html.HTML;
import org.codehaus.jackson.map.ObjectMapper;

/**
 *
 * @author Dell
 */
public class ChatServer {
       public static final String ANSI_RED = "\u001B[31m";
    public static void main(String[] args) {
        // TODO code application logic here
        System.out.println("Hello. This is server");
        int port = 1511;
       
  
        try{
            ServerSocket serverSocket = new ServerSocket(port);
            while (true){
                Socket currentSocket = serverSocket.accept();
                System.out.println("connected");
                
                InputStream is = currentSocket.getInputStream();
                ObjectInputStream ois = new ObjectInputStream(is);
                String s = (String)ois.readObject();
                System.out.println("receive from client: " + s + ANSI_RED);
                ObjectMapper mapper = new ObjectMapper();
                MyMessage mr = mapper.readValue(s, MyMessage.class);
                
                MyMessage m = new MyMessage();
                m.sender = "server";
                m.receiver = mr.sender;
                m.type = mr.type;
                
                Connection con = null;
                Class.forName("org.sqlite.JDBC");
                con = DriverManager.getConnection("jdbc:sqlite:SQlite15I1.db");
                
                if (mr.type.equalsIgnoreCase("login")){
                    String sql = "SELECT id, username, password FROM TblUser WHERE (username = ?) AND (password = ?)";
                    PreparedStatement pstmt = con.prepareStatement(sql);
                    pstmt.setString(1, mr.sender);
                    pstmt.setString(2, mr.content);
                    ResultSet rs    = pstmt.executeQuery();
                    
                    if (rs.next()){
                        m.content = "ok";
                    }
                    // loop through the result set
                    else{
                        m.content = "fail";
                    }
                }else if(mr.type.equalsIgnoreCase("signup")){
                    String sql = "SELECT * FROM TblUser WHERE username = ?";
                    PreparedStatement pstmt = con.prepareStatement(sql);
                    pstmt.setString(1, mr.sender);
                    ResultSet rs    = pstmt.executeQuery();
                    if (rs.next()){
                        m.content = "fail";
                       
                    }else {
                        String sql1 = "INSERT INTO TblUser(username,password) VALUES(?,?)";
                        PreparedStatement ps = con.prepareStatement(sql1);
                        ps.setString(1, mr.sender);
                        ps.setString(2, mr.content);
                       int i = ps.executeUpdate();
                       if(i>0){
                           m.content = "ok";
                       }else {
                           m.content = "fail";
                       }
                        
                    }
                }
                else if(mr.type.equalsIgnoreCase("getusers")){
                    String sql = "SELECT username FROM TblUser";
                    PreparedStatement pstmt = con.prepareStatement(sql);
                    ResultSet rs    = pstmt.executeQuery(); //rs = result set
                    ArrayList<String> userList = new ArrayList<>();
                    while (rs.next()){
                        String username = rs.getString("username");
                        if (!username.equalsIgnoreCase(mr.sender)){
                            userList.add(username);
                        }
                    }
                    
                    m.content = mapper.writeValueAsString(userList);
                    System.out.println(m.content);
                }
                else if(mr.type.equalsIgnoreCase("chat")){
                    SimpleDateFormat sdf = new SimpleDateFormat("Y/M/d h:m:s");
                    Date date = new Date();
                    String sql = "INSERT INTO TblContent(sender,receiver,type,content,inComeTime,isDeliver) VALUES(?,?,?,?,?,?)";
                    PreparedStatement pstmt = con.prepareStatement(sql);
                    pstmt.setString(1,mr.sender);
                    pstmt.setString(2, mr.receiver);
                    pstmt.setString(3,mr.type);
                    pstmt.setString(4, mr.content);
                    pstmt.setString(5,sdf.format(date));
                    pstmt.setString(6, "false");
                    pstmt.executeUpdate();
                }
                else if(mr.type.equalsIgnoreCase("getmessages")){
                    Statement sttm =con.createStatement();                                                           // la username hien tai
                    String sql = "SELECT content FROM TblContent where (sender = '"+mr.content+"') AND (receiver = '"+mr.sender+"') AND (isDeliver = 'false')";
                    ResultSet rs = sttm.executeQuery(sql);
                    ArrayList<String> A = new ArrayList<>();
                    while (rs.next()) {
                        A.add(rs.getString("content"));
                    }
                    String sql2 = "UPDATE TblContent SET isDeliver = 'true' where (sender = '"+mr.content+"') AND (receiver = '"+mr.sender+"') AND (isDeliver = 'false')";
                    PreparedStatement pstmt = con.prepareStatement(sql2);
                    pstmt.executeUpdate();
                    m.content = mapper.writeValueAsString(A);
                }
                
                OutputStream os = currentSocket.getOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(os);
                String s1 = mapper.writeValueAsString(m);
                System.out.println("send to client: " + s1);
                oos.writeObject(s1);
                oos.flush();
                
                con.close();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        
    }
}
