package jspstudy.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import jspstudy.dbconn.Dbconn;
import jspstudy.domain.BoardVo;
import jspstudy.domain.Criteria;
import jspstudy.domain.SearchCriteria;

//1��
public class BoardDao {
	// 5��
	Connection conn;
	// 6��
	PreparedStatement pstmt;

	// 2��
	public BoardDao() {
		// 3��
		Dbconn db = new Dbconn();
		// 4��
		this.conn = db.getConnection();

	}

	// 7-1 //9-2
	public int insertBoard(String subject, String content, String writer, String ip, int midx, String filename) {
		// 7-3
		int value = 0;

		// 8��
		String sql = "INSERT INTO a_board(originbidx,depth,level_, subject, content, writer, ip, midx, filename)"
				+ "VALUES (0,0,0,?,?,?,?,?,? )";

		// 9��
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, subject);
			pstmt.setString(2, content);
			pstmt.setString(3, writer);
			pstmt.setString(4, ip);
			pstmt.setInt(5, midx);
			pstmt.setString(6, filename);
			// 9-3
			value = pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally { 

			try {
				pstmt.close(); 
				conn.close(); 
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}

		return value;
	}

	public ArrayList<BoardVo> boardSelectAll(SearchCriteria scri) {
		ArrayList<BoardVo> alist = new ArrayList<BoardVo>();
		ResultSet rs = null;

		String str = "";
		if (scri.getSearchType().equals("subject")) {
			str = "and subject like ?";
		} else {
			str = "and writer like ?";
		}
//		String sql = "select * from a_board where delyn='N' order by originbidx desc, depth asc";
//		String sql = "SELECT * FROM (SELECT ROWNUM AS rnum, A.* FROM (SELECT * FROM a_board WHERE delyn='N' " + str
//				+ " ORDER BY ORIGINbidx desc, depth ASC) A)B WHERE rnum limit ?,?";
		
		// 현재 rownum으로 만든 것이 아니라 limit로 일단 해결.
		String sql = "SELECT * FROM a_board where delyn='N'"+str+" order by originbidx desc, depth asc limit ?,? ";
 
		try {

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "%" + scri.getKeyword() + "%");
			pstmt.setInt(2, (scri.getPage() - 1) * 15 + 1);
			pstmt.setInt(3, (scri.getPage() * 15));
			rs = pstmt.executeQuery();

			while (rs.next()) {
				BoardVo bv = new BoardVo();
				bv.setBidx(rs.getInt("bidx")); // 
				bv.setSubject(rs.getString("subject"));
				bv.setWriter(rs.getString("writer"));
				bv.setWriteday(rs.getString("writeday"));
				bv.setLevel_(rs.getInt("level_"));
				bv.setDepth(rs.getInt("depth"));
				bv.setOriginbidx(0);

				alist.add(bv); // 
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return alist;
	}

	public BoardVo boardSelectOne(int bidx) {
		BoardVo bv = null;
		ResultSet rs = null;

		String sql = "select * from a_board where bidx=?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bidx);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				bv = new BoardVo();
				bv.setBidx(rs.getInt("bidx"));
				bv.setOriginbidx(rs.getInt("originbidx"));
				bv.setDepth(rs.getInt("depth"));
				bv.setLevel_(rs.getInt("level_"));

				bv.setSubject(rs.getString("subject"));
				bv.setContent(rs.getString("content"));
				bv.setWriter(rs.getString("writer"));
				bv.setWriteday(rs.getString("writeday"));
				bv.setFilename(rs.getString("filename"));

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return bv;
	}

	public int modifyBoard(String subject, String content, String writer, int bidx) {

		int value = 0;


		String sql = "update a_board set subject=?, content=?, writer=?, writeday=sysdate where bidx=?";

	
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, subject);
			pstmt.setString(2, content);
			pstmt.setString(3, writer);
			pstmt.setInt(4, bidx);
			// 9-3
			value = pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally { 

			try {
				pstmt.close(); 
				conn.close(); 
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return value;
	}

	public int deleteBoard(int bidx) {
		// 7-3
		int value = 0;

		// 8��
		String sql = "update a_board set delyn='Y' where bidx=?";

		// 9��
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bidx);
			// 9-3
			value = pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally { // 10��

			try {// 11-3 11-1�� ������ �߸� ���콺 �÷��� try�� ���´�.
				pstmt.close(); // 11-1
				conn.close(); // 11-2
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}

		// 7-2 // 9-4 value���� return ��Ų��
		return value;
	}

	public int replyBoard(BoardVo bv) {
		// 7-3
		int value = 0;

		// 8��
		String sql1 = "update a_board set depth=depth+1 where originbidx=? and depth > ?";
		String sql2 = "insert into a_board(bidx, originbidx, depth, level_, subject, content, writer, ip, midx)"
				+ "VALUES (bidx_b_seq.nextval,?,?,?,?,?,?,?,? )";

		try {
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(sql1);
			pstmt.setInt(1, bv.getOriginbidx());
			pstmt.setInt(2, bv.getDepth());

			value = pstmt.executeUpdate();

			pstmt = conn.prepareStatement(sql2);
			pstmt.setInt(1, bv.getOriginbidx());
			pstmt.setInt(2, bv.getDepth() + 1);
			pstmt.setInt(3, bv.getLevel_() + 1);
			pstmt.setString(4, bv.getSubject());
			pstmt.setString(5, bv.getContent());
			pstmt.setString(6, bv.getWriter());
			pstmt.setString(7, bv.getIp());
			pstmt.setInt(8, bv.getMidx());

			value = pstmt.executeUpdate();

			conn.commit();

		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}

			e.printStackTrace();
		} finally { 

			try {
				pstmt.close(); 
				conn.close(); 
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}

		return value;
	}

	public int boardTotal(SearchCriteria scri) {
		int cnt = 0;
		ResultSet rs = null;
		String str = "";
		if (scri.getSearchType().equals("subject")) {
			str = "and subject like ?";
		} else {
			str = "and writer like ?";
		}

		String sql = "select count(*) as cnt from a_board where delyn='N'" + str + "";

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "%" + scri.getKeyword() + "%");

			rs = pstmt.executeQuery();

			if (rs.next()) {
				cnt = rs.getInt("cnt");

			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {

			try {
				rs.close();
				pstmt.close();
//				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return cnt;
	}

}
