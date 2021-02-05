package likey;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LikeyDAO {
   private Connection conn;//데이터베이스에 접근하게 해주는 하나의 객체
   private ResultSet rs;//정보를 담을 수 있는 객체
   
   public LikeyDAO() {//mysql에 접속을 하게 해줌,자동으로 데이터베이스 커넥션이 일어남
      try {//예외처리
         String dbURL = "jdbc:mysql://localhost:3306/BBS?serverTimezone=UTC";
         String dbID="root";
         String dbPassword="1248";
         Class.forName("com.mysql.jdbc.Driver");//mysql드라이버를 찾는다.
         //드라이버는 mysql에 접속할 수 있도록 매개체 역할을 하는 하나의 라이브러리
         conn=DriverManager.getConnection(dbURL,dbID,dbPassword);
      }catch(Exception e) {
         e.printStackTrace();
      }
   }
   
   public int like(String userID, int bbsID) {
      String SQL = "insert into likey values (?, ?)";
      try {
         PreparedStatement pstmt = conn.prepareStatement(SQL);
         pstmt.setString(1, userID);
         pstmt.setInt(2, bbsID);
         return pstmt.executeUpdate();
      } catch(Exception e) {
         e.printStackTrace();
      }
   return -1;//추천 중복 오류
 }

}