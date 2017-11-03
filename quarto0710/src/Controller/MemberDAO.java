package Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Vector;

import Model.MemberVO;


public class MemberDAO {
	
	public MemberVO getMemberRegister(MemberVO mvo){
		StringBuffer sql = new StringBuffer();
		sql.append("insert into member");
		sql.append("(id, password, name, nickname)");
		sql.append(" values(?, ?, ?, ?)");
		
		Connection con = null;
		PreparedStatement pstmt = null;
		MemberVO mVo= mvo;
		
		try {
			con = DBUtil.getConnection();
		
		
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, mVo.getTxtId());
			pstmt.setString(2, mVo.getTxtPass());
			pstmt.setString(3, mVo.getName());
			pstmt.setString(4, mVo.getNick());
			int i = pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try{
				if(pstmt!=null){
					pstmt.close();
				}
				if(con!=null){
					con.close();
				}
			}catch(SQLException e){
			}
		}
		
		return mVo;
	}
	
	public List<MemberVO> getMemberList(){
		List<MemberVO> memberList = new Vector<MemberVO>();
		Connection con= null;
		try {
			con=DBUtil.getConnection();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select * from member");
			while(rs.next()){
				String id = rs.getString("id");
				String password = rs.getString("password");
				String name = rs.getString("name");
				String nick = rs.getString("nickname");
				int win = rs.getInt("win");
				int lose = rs.getInt("lose");
				int total = rs.getInt("toatal");
				
				memberList.add(new MemberVO(id, name, nick, password, win, lose, total));
			}
			if(con!=null){
				con.close();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return memberList;
	}
	
	public void updateMemberResult(MemberVO mvo){
		
//		Connection con = null;
		MemberVO mVo= mvo;
//		try {
//			con=DBUtil.getConnection();
//			Statement stmt = con.createStatement();
//			int count = stmt.executeUpdate("update member set win="+mVo.getWin()+", lose ="+mVo.getLose()+
//					", toatal = "+mVo.getTotalGame()+"where id='"+mVo.getTxtId()+"'");
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		PreparedStatement pstmt = null;
		StringBuffer sql = new StringBuffer();
		Connection con = null;
//		sql.append("update member set");
//		sql.append("win = ?, lose = ?, toatal = ? where id=?");
		System.out.println("실행했습니다. 1");
		sql.append("update member set win=?, lose=?, toatal=? where id=?");
		
		try {
			con = DBUtil.getConnection();
			System.out.println("실행했습니다. 2");
		
		
			pstmt = con.prepareStatement(sql.toString());
			
			pstmt.setInt(1, mVo.getWin());
			pstmt.setInt(2, mVo.getLose());
			pstmt.setInt(3, mVo.getTotalGame());
			pstmt.setString(4, mVo.getTxtId());
			System.out.println("실행했습니다. 3");
			int i = pstmt.executeUpdate();
			System.out.println("실행했습니다. i="+i);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try{
				if(pstmt!=null){
					pstmt.close();
				}
				if(con!=null){
					con.close();
				}
			}catch(SQLException e){
			}
		}
		
	}

}
