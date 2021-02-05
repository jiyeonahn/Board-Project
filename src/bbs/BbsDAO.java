package bbs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class BbsDAO {
	
	private Connection conn;//데이터베이스에 접근하게 해주는 하나의 객체
	private ResultSet rs;//정보를 담을 수 있는 객체
	
	public BbsDAO() {//mysql에 접속을 하게 해줌,자동으로 데이터베이스 커넥션이 일어남
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
	
	public String getDate() {//현재 서버 시간 가져오기
		String SQL="select now()";//현재 시간을 가져오는 mysql문장
		try {
			PreparedStatement pstmt=conn.prepareStatement(SQL);//sql문장을 실행 준비 단계로
			rs=pstmt.executeQuery();//실행결과 가져오기
			if(rs.next()) {
				return rs.getString(1);//현재 날짜 반환
			}
			
		} catch(Exception e) {
			e.printStackTrace();//오류 발생
		}
		return "";//데이터베이스 오류
	}
	
	public int getNext() {
		String SQL="SELECT bbsID from BBS order by bbsID DESC";//마지막 게시물 반환
		try {
			PreparedStatement pstmt=conn.prepareStatement(SQL);
			rs=pstmt.executeQuery();
			if(rs.next()) {
				return rs.getInt(1) + 1;
			}
			return 1; // 첫 번째 게시물인 경우
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return -1;//데이터베이스 오류
	}
	
	public int write(String bbsTitle, String userID, String bbsContent, int bbsCount, int LikeCount) {
		String SQL="INSERT INTO BBS VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		try {
			PreparedStatement pstmt=conn.prepareStatement(SQL);
			pstmt.setInt(1, getNext());//게시글 번호
			pstmt.setString(2, bbsTitle);//제목
			pstmt.setString(3, userID);//아이디
			pstmt.setString(4, getDate());//날짜
			pstmt.setString(5, bbsContent);//내용
			pstmt.setInt(6, 1);//삭제된 경우가 아니기 때문에 1을 넣어줌
			pstmt.setInt(7, bbsCount);
			pstmt.setInt(8, LikeCount);
			return pstmt.executeUpdate();			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return -1;//데이터베이스 오류
	}
	
	//데이터베이스에서 글의 목록을 가져오는 소스코드 작성
	public ArrayList<Bbs> getList(int pageNumber){//특정한 리스트를 받아서 반환
		String SQL="SELECT * from BBS where bbsID < ? AND bbsAvailable = 1 order by bbsID desc limit 10";//마지막 게시물 반환, 삭제가 되지 않은 글만 가져온다.
		ArrayList<Bbs> list = new ArrayList<Bbs>();
		try {
			PreparedStatement pstmt=conn.prepareStatement(SQL);
			pstmt.setInt(1, getNext()-(pageNumber-1)*10);//물음표에 들어갈 내용
			rs=pstmt.executeQuery();
			while(rs.next()) {
				Bbs bbs = new Bbs();
				bbs.setBbsID(rs.getInt(1));
				bbs.setBbsTitle(rs.getString(2));
				bbs.setUserID(rs.getString(3));
				bbs.setBbsDate(rs.getString(4));
				bbs.setBbsContent(rs.getString(5));
				bbs.setBbsAvailable(rs.getInt(6));
				bbs.setBbsCount(rs.getInt(7));
				bbs.setLikeCount(rs.getInt(8));
				list.add(bbs);//list에 해당 인스턴스를 담는다.
			}			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return list;//ㄱㅔ시글 리스트 반환
	}
	
	public int getCount() {
		String SQL = "select count(*) from bbs";
		try {
			PreparedStatement pstmt=conn.prepareStatement(SQL);
			rs=pstmt.executeQuery();
			if(rs.next()) {
				return rs.getInt(1);
			}			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	public boolean nextPage(int pageNumber) {//페이지 처리를 위한 함수
		String SQL="SELECT * from BBS where bbsID < ? AND bbsAvailable =1";
		try {
			PreparedStatement pstmt=conn.prepareStatement(SQL);
			pstmt.setInt(1, getNext()-(pageNumber-1)*10);
			rs=pstmt.executeQuery();
			if(rs.next()) {
				return true;//다음 페이지로 넘어갈 수 있음
			}			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public Bbs getBbs(int bbsID) {//하나의 글 내용을 불러오는 함수
		String SQL="SELECT * from BBS where bbsID = ?";
		try {
			PreparedStatement pstmt=conn.prepareStatement(SQL);
			pstmt.setInt(1, bbsID);//물음표
			rs=pstmt.executeQuery();//select
			if(rs.next()) {//결과가 있다면
				Bbs bbs = new Bbs();
				bbs.setBbsID(rs.getInt(1));//첫 번째 결과 값
				bbs.setBbsTitle(rs.getString(2));
				bbs.setUserID(rs.getString(3));
				bbs.setBbsDate(rs.getString(4));
				bbs.setBbsContent(rs.getString(5));
				bbs.setBbsAvailable(rs.getInt(6));
				int bbsCount=rs.getInt(7);
				bbs.setBbsCount(bbsCount);
				bbsCount++;
				countUpdate(bbsCount,bbsID);
	
				bbs.setLikeCount(rs.getInt(8));
				//like(bbsID);
				return bbs;//6개의 항목을 bbs인스턴스에 넣어 반환한다.
			}			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public int countUpdate(int bbsCount, int bbsID) {
		String SQL = "update bbs set bbsCount = ? where bbsID = ?";
		try {
			PreparedStatement pstmt=conn.prepareStatement(SQL);
			pstmt.setInt(1, bbsCount);//물음표의 순서
			pstmt.setInt(2, bbsID);
			return pstmt.executeUpdate();//insert,delete,update			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return -1;//데이터베이스 오류
	}
	
	
	public int update(int bbsID, String bbsTitle,String bbsContent ) {
		String SQL="update BBS set bbsTitle = ?, bbsContent = ? where bbsID = ?";//특정한 아이디에 해당하는 제목과 내용을 바꿔준다. 
		try {
			PreparedStatement pstmt=conn.prepareStatement(SQL);
			pstmt.setString(1, bbsTitle);//물음표의 순서
			pstmt.setString(2, bbsContent);
			pstmt.setInt(3, bbsID);
			return pstmt.executeUpdate();//insert,delete,update			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return -1;//데이터베이스 오류
	}
	
	public int delete(int bbsID) {
		String SQL = "update BBS set bbsAvailable = 0 where bbsID = ?";
		try {
			PreparedStatement pstmt=conn.prepareStatement(SQL);
			pstmt.setInt(1, bbsID);
			return pstmt.executeUpdate();			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return -1;//데이터베이스 오류
	}
	
	public int like(int bbsID) {
		String SQL = "update bbs set likeCount = likeCount + 1 where bbsID = ?";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, bbsID);
			return pstmt.executeUpdate();
		} catch(Exception e) {
			e.printStackTrace();
		} 
		return -1;
	}
	
	public ArrayList<Bbs> getSearch(String searchField, String searchText){//특정한 리스트를 받아서 반환
	      ArrayList<Bbs> list = new ArrayList<Bbs>();
	      String SQL ="select * from bbs WHERE "+searchField.trim();
	      try {
	            if(searchText != null && !searchText.equals("") ){//이거 빼면 안 나온다ㅜ 왜지?
	                SQL +=" LIKE '%"+searchText.trim()+"%' order by bbsID desc limit 10";
	            }
	            PreparedStatement pstmt=conn.prepareStatement(SQL);
				rs=pstmt.executeQuery();//select
	         while(rs.next()) {
	            Bbs bbs = new Bbs();
	            bbs.setBbsID(rs.getInt(1));
	            bbs.setBbsTitle(rs.getString(2));
	            bbs.setUserID(rs.getString(3));
	            bbs.setBbsDate(rs.getString(4));
	            bbs.setBbsContent(rs.getString(5));
	            bbs.setBbsAvailable(rs.getInt(6));
	            bbs.setBbsCount(rs.getInt(7));
	            bbs.setLikeCount(rs.getInt(8));
	            list.add(bbs);//list에 해당 인스턴스를 담는다.
	         }         
	      } catch(Exception e) {
	         e.printStackTrace();
	      }
	      return list;//ㄱㅔ시글 리스트 반환
	   }
}