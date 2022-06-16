package jspstudy.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import jspstudy.domain.BoardVo;
import jspstudy.domain.Criteria;
import jspstudy.domain.PageMaker;
import jspstudy.domain.SearchCriteria;
import jspstudy.service.BoardDao;

@WebServlet("/BoardController")
public class BoardController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// ↓DB로 전송할때 한글 깨지지 않도록 하는 구문 ↓↓
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");

		//가상경로 추출
		String uri = request.getRequestURI();
		String pj= request.getContextPath();
		String command= uri.substring(pj.length());
		
		String uploadPath = "D:\\openapi(A)\\dev\\jspstudy\\src\\main\\webapp\\";
		String saveFolder = "images";
		String saveFullPath = uploadPath+saveFolder;

		if (command.equals("/board/boardWrite.do")){

			System.out.println("게시판글작성 들어옴");
			RequestDispatcher rd = request.getRequestDispatcher("/board/boardWrite.jsp");
			rd.forward(request, response);

		}else if (command.equals("/board/boardWriteAction.do")) {
			
			int sizeLimit = 1024*1024*15;
			
			MultipartRequest multi = null;
			multi = new MultipartRequest(request, saveFullPath, sizeLimit, "UTF-8", new DefaultFileRenamePolicy());
			
			
			String subject = multi.getParameter("subject");
			String content = multi.getParameter("content");
			String writer = multi.getParameter("writer");
			
			
			//열거자에 저장될 파일을 담는 객체를 생성한다.
			Enumeration files =  multi.getFileNames();
			//담긴 파일 객체의 파일 이름을 얻는다.
			String file = (String)files.nextElement();
			//저장되는 파일 이름
			String fileName = multi.getFilesystemName(file);
			//원래 파일이름
			String originFileName = multi.getOriginalFileName(file);
			
			String ip = InetAddress.getLocalHost().getHostAddress();
			
			
			HttpSession session = request.getSession();
			int midx = (int)session.getAttribute("midx");
			
			BoardDao bd = new BoardDao();
			int value = bd.insertBoard(subject, content, writer, ip, midx, fileName);

			if (value == 1) {
				response.sendRedirect(request.getContextPath()+"/index.jsp");
			} else {
				response.sendRedirect(request.getContextPath()+"/board/boardWrite.do");
			}
		}else if(command.equals("/board/boardList.do")) {
			System.out.println("리스트 들어왔음");
			
			String page = request.getParameter("page");
			if(page == null) {
				page= "1";
			}
			int pagex = Integer.parseInt(page);
			
			String keyword =request.getParameter("keyword");
			if(keyword == null) {keyword="";}
			String searchType = request.getParameter("searchType");
			if(searchType==null) {searchType="subject";}
			
			SearchCriteria scri = new SearchCriteria();
			scri.setPage(pagex);
			scri.setKeyword(keyword);
			scri.setSearchType(searchType);
			
			// 처리하는 부분 

			BoardDao bd = new BoardDao();
			int cnt = bd.boardTotal(scri);
			System.out.println("cnt"+cnt);
			
			
			PageMaker pm = new PageMaker();
			pm.setScri(scri);
			pm.setTotalCount(cnt);
			
			
			ArrayList<BoardVo> alist = bd.boardSelectAll(scri);
			request.setAttribute("alist", alist);  			// 데이터(자원) 공유
			request.setAttribute("pm", pm);
			


			//이동하는 부분
			RequestDispatcher rd = request.getRequestDispatcher("/board/boardList.jsp");
			rd.forward(request, response);

		} else if (command.equals("/board/boardContent.do")) {
			System.out.println("내용보기 들어왔음");
			// 1. parameter가 넘어옴
			String bidx = request.getParameter("bidx");
			int bidx_ = Integer.parseInt(bidx);

			// 2. 처리함
			BoardDao bd = new BoardDao();
			BoardVo bv = bd.boardSelectOne(bidx_);

			request.setAttribute("bv", bv);   // 내부적으로 자원 공유

			// 3. 이동함
			RequestDispatcher rd = request.getRequestDispatcher("/board/boardContent.jsp");
			rd.forward(request, response);
			
		} else if (command.equals("/board/boardModify.do")){
			System.out.println("게시판 글수정 들어옴");
		
			// 1. parameter가 넘어옴
						String bidx = request.getParameter("bidx");
						int bidx_ = Integer.parseInt(bidx);

						// 2. 처리함
						BoardDao bd = new BoardDao();
						BoardVo bv = bd.boardSelectOne(bidx_);

						request.setAttribute("bv", bv);   // 내부적으로 자원 공유

						// 3. 이동함
						RequestDispatcher rd = request.getRequestDispatcher("/board/boardModify.jsp");
						rd.forward(request, response);
						
		}else if (command.equals("/board/boardModifyAction.do")) {
			
			System.out.println("게시글 수정 됨");
			
			String bidx = request.getParameter("bidx");
			int bidx_ = Integer.parseInt(bidx);
			
			
			String subject = request.getParameter("subject");
			String content = request.getParameter("content");
			String writer = request.getParameter("writer");
			
			BoardDao bd = new BoardDao();
			int value = bd.modifyBoard(subject, content, writer, bidx_);
			
			if (value == 1) {
				response.sendRedirect(request.getContextPath()+"/board/boardContent.do?bidx="+bidx);
			} else {
				response.sendRedirect(request.getContextPath()+"/board/boardModify.do");
			}
			
		} else if (command.equals("/board/boardDelete.do")) {
			System.out.println("게시글 삭제 들어옴");
			
			String bidx = request.getParameter("bidx");
			int bidx_ = Integer.parseInt(bidx);
			BoardDao bd = new BoardDao();
			BoardVo bv = bd.boardSelectOne(bidx_);

			request.setAttribute("bv", bv);  
			
			RequestDispatcher rd = request.getRequestDispatcher("/board/boardDelete.jsp");
			rd.forward(request, response);
			
		} else if (command.equals("/board/boardDeleteAction.do")) {
			
				System.out.println("게시글 삭제 됨");
				
				
				
				String bidx = request.getParameter("bidx");
				int bidx_ = Integer.parseInt(bidx);				
				BoardDao bd = new BoardDao();
				int value = bd.deleteBoard(bidx_);
			
				if (value == 1) {
					response.sendRedirect(request.getContextPath()+"/index.jsp");
				} else {
					response.sendRedirect(request.getContextPath()+"/board/boardDelete.do");
				}
			
		}else if (command.equals("/board/boardReply.do")) {
			System.out.println("답변 페이지 들어옴");
			
			//넘겨야할 목록들 ↓
			String bidx = request.getParameter("bidx");
			String originbidx = request.getParameter("originbidx");
			String depth = request.getParameter("depth");
			String level_ = request.getParameter("level_");
//		System.out.println(bidx);
//		System.out.println(originbidx);
//		System.out.println(depth);
//		System.out.println(level_);
			
			BoardVo bv = new BoardVo();
			bv.setBidx(Integer.parseInt(bidx));
			bv.setOriginbidx(Integer.parseInt(originbidx));
			bv.setDepth(Integer.parseInt(depth));
			bv.setLevel_(Integer.parseInt(level_));
			
			request.setAttribute("bv", bv);
			
			
			RequestDispatcher rd = request.getRequestDispatcher("/board/boardReply.jsp");
			rd.forward(request, response);
			
		}else if (command.equals("/board/boardReplyAction.do")) {
			System.out.println("답변 완료");
			
			
			//넘겨야할 목록들 ↓↓
			String bidx = request.getParameter("bidx");
			String originbidx = request.getParameter("originbidx");
			String depth = request.getParameter("depth");
			String level_ = request.getParameter("level_");
			String subject = request.getParameter("subject");
			String content = request.getParameter("content");
			String writer = request.getParameter("writer");
			

			String ip = request.getParameter("ip");
			
			HttpSession session = request.getSession();
			int midx = (int)session.getAttribute("midx");
			
			// 처리하는 부분에서 bv를 사용하기에 bv를 객체생성하고 닮는다.
			BoardVo bv = new BoardVo();
			bv.setBidx(Integer.parseInt(bidx));
			bv.setOriginbidx(Integer.parseInt(originbidx));
			bv.setDepth(Integer.parseInt(depth));
			bv.setLevel_(Integer.parseInt(level_));
			
			bv.setSubject(subject);
			bv.setContent(content);
			bv.setWriter(writer);
			bv.setIp(ip);
			bv.setMidx(midx);
			
			
			
			// 처리하는 부분 ↓
			BoardDao bd = new BoardDao();
			bd.replyBoard(bv);
			int value = bd.replyBoard(bv);
			
			if(value == 1) {
				response.sendRedirect(request.getContextPath()+"/board/baordList.do");
				
				
			} else {
				response.sendRedirect(request.getContextPath()+"/board/boardContent.do?bidx="+bidx);	
			}
			
			
		}else if(command.equals("/board/fileDownload.do")) {
			
			//파일이름을 넘겨받는다.
			String filename = request.getParameter("filename");
			//파일의 전체 경로
			String filePath = saveFullPath + File.separator+filename;
			
			
			//해당위치에 있는 파일을 읽어 들인다.
			FileInputStream fileInputStream = new FileInputStream(filePath);
			
			Path source = Paths.get(filePath);
			String mimeType = Files.probeContentType(source);
			//헤더 정보에 추출한 파일 형식을 담는다.
			response.setContentType(mimeType);
			
			String sEncoding = new String(filename.getBytes("UTF-8"));
			//헤더정보에 파일이름을 담는다.
			response.setHeader("Content-Disposition","attachment;fileName="+ sEncoding);
			
			//파일쓰기 
			ServletOutputStream servletOutStream = response.getOutputStream();
			byte[] b = new byte[4096];
			int read = 0;
			while((read = fileInputStream.read(b, 0, b.length))!=-1) {
				servletOutStream.write(b, 0, read);
			}
			
			servletOutStream.flush();
			servletOutStream.close();
			fileInputStream.close();
			
			
			
		}
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
