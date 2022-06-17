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

		// ��DB�� �����Ҷ� �ѱ� ������ �ʵ��� �ϴ� ���� ���
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");

		//������ ����
		String uri = request.getRequestURI();
		String pj= request.getContextPath();
		String command= uri.substring(pj.length());
		
		String uploadPath = "D:\\openapi(A)\\dev\\jspstudy\\src\\main\\webapp\\";
		String saveFolder = "images";
		String saveFullPath = uploadPath+saveFolder;

		if (command.equals("/board/boardWrite.do")){

			System.out.println("�Խ��Ǳ��ۼ� ����");
			RequestDispatcher rd = request.getRequestDispatcher("/board/boardWrite.jsp");
			rd.forward(request, response);

		}else if (command.equals("/board/boardWriteAction.do")) {
			
			int sizeLimit = 1024*1024;
			
			MultipartRequest multi = null;
			multi = new MultipartRequest(request, saveFullPath, sizeLimit, "UTF-8", new DefaultFileRenamePolicy());
			
			
			String subject = multi.getParameter("subject");
			String content = multi.getParameter("content");
			String writer = multi.getParameter("writer");
			
			
			//�����ڿ� ����� ������ ��� ��ü�� �����Ѵ�.
			Enumeration files =  multi.getFileNames();
			//��� ���� ��ü�� ���� �̸��� ��´�.
			String file = (String)files.nextElement();
			//����Ǵ� ���� �̸�
			String fileName = multi.getFilesystemName(file);
			//���� �����̸�
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
			System.out.println("����Ʈ ������");
			
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
			
			// ó���ϴ� �κ� 

			BoardDao bd = new BoardDao();
			int cnt = bd.boardTotal(scri);
			System.out.println("cnt"+cnt);
			
			
			PageMaker pm = new PageMaker();
			pm.setScri(scri);
			pm.setTotalCount(cnt);
			
			
			ArrayList<BoardVo> alist = bd.boardSelectAll(scri);
			request.setAttribute("alist", alist);  			// ������(�ڿ�) ����
			request.setAttribute("pm", pm);
			


			//�̵��ϴ� �κ�
			RequestDispatcher rd = request.getRequestDispatcher("/board/boardList.jsp");
			rd.forward(request, response);

		} else if (command.equals("/board/boardContent.do")) {
			System.out.println("���뺸�� ������");
			// 1. parameter�� �Ѿ��
			String bidx = request.getParameter("bidx");
			int bidx_ = Integer.parseInt(bidx);

			// 2. ó����
			BoardDao bd = new BoardDao();
			BoardVo bv = bd.boardSelectOne(bidx_);

			request.setAttribute("bv", bv);   // ���������� �ڿ� ����

			// 3. �̵���
			RequestDispatcher rd = request.getRequestDispatcher("/board/boardContent.jsp");
			rd.forward(request, response);
			
		} else if (command.equals("/board/boardModify.do")){
			System.out.println("�Խ��� �ۼ��� ����");
		
			// 1. parameter�� �Ѿ��
						String bidx = request.getParameter("bidx");
						int bidx_ = Integer.parseInt(bidx);

						// 2. ó����
						BoardDao bd = new BoardDao();
						BoardVo bv = bd.boardSelectOne(bidx_);

						request.setAttribute("bv", bv);   // ���������� �ڿ� ����

						// 3. �̵���
						RequestDispatcher rd = request.getRequestDispatcher("/board/boardModify.jsp");
						rd.forward(request, response);
						
		}else if (command.equals("/board/boardModifyAction.do")) {
			
			System.out.println("�Խñ� ���� ��");
			
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
			System.out.println("�Խñ� ���� ����");
			
			String bidx = request.getParameter("bidx");
			int bidx_ = Integer.parseInt(bidx);
			BoardDao bd = new BoardDao();
			BoardVo bv = bd.boardSelectOne(bidx_);

			request.setAttribute("bv", bv);  
			
			RequestDispatcher rd = request.getRequestDispatcher("/board/boardDelete.jsp");
			rd.forward(request, response);
			
		} else if (command.equals("/board/boardDeleteAction.do")) {
			
				System.out.println("�Խñ� ���� ��");
				
				
				
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
			System.out.println("�亯 ������ ����");
			
			//�Ѱܾ��� ��ϵ� ��
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
			System.out.println("�亯 �Ϸ�");
			
			
			//�Ѱܾ��� ��ϵ� ���
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
			
			// ó���ϴ� �κп��� bv�� ����ϱ⿡ bv�� ��ü�����ϰ� ��´�.
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
			
			
			
			// ó���ϴ� �κ� ��
			BoardDao bd = new BoardDao();
			bd.replyBoard(bv);
			int value = bd.replyBoard(bv);
			
			if(value == 1) {
				response.sendRedirect(request.getContextPath()+"/board/baordList.do");
				
				
			} else {
				response.sendRedirect(request.getContextPath()+"/board/boardContent.do?bidx="+bidx);	
			}
			
			
		}else if(command.equals("/board/fileDownload.do")) {
			
			//�����̸��� �Ѱܹ޴´�.
			String filename = request.getParameter("filename");
			//������ ��ü ���
			String filePath = saveFullPath + File.separator+filename;
			
			
			//�ش���ġ�� �ִ� ������ �о� ���δ�.
			FileInputStream fileInputStream = new FileInputStream(filePath);
			
			Path source = Paths.get(filePath);
			String mimeType = Files.probeContentType(source);
			//��� ������ ������ ���� ������ ��´�.
			response.setContentType(mimeType);
			
			String sEncoding = new String(filename.getBytes("UTF-8"));
			//��������� �����̸��� ��´�.
			response.setHeader("Content-Disposition","attachment;fileName="+ sEncoding);
			
			//���Ͼ��� 
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
