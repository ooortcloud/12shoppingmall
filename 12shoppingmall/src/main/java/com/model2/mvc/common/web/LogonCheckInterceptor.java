package com.model2.mvc.common.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.model2.mvc.service.domain.User;


/*
 * FileName : LogonCheckInterceptor.java
 *  �� Controller ȣ���� interceptor �� ���� ��ó��/��ó��/�Ϸ�ó���� ����
 *  	- preHandle() : Controller ȣ���� ��ó��   
 * 			(true return ==> Controller ȣ�� / false return ==> Controller ��ȣ�� ) 
 *  	- postHandle() : Controller ȣ�� �� ��ó��
 *    	- afterCompletion() : view ������ ó��
 *    
 *    ==> �α����� ȸ���̸� Controller ȣ�� : true return
 *    ==> �� �α����� ȸ���̸� Controller �� ȣ�� : false return
 */
public class LogonCheckInterceptor extends HandlerInterceptorAdapter {

	///Field
	
	///Constructor
	public LogonCheckInterceptor(){
		System.out.println("\nCommon :: "+this.getClass()+"\n");		
	}
	
	///Method
	public boolean preHandle(	HttpServletRequest request,
														HttpServletResponse response, 
														Object handler) throws Exception {
		
		System.out.println("\n[ LogonCheckInterceptor start........]");
		
		//==> �α��� ����Ȯ��
		HttpSession session = request.getSession(true);
		User user = (User)session.getAttribute("user");

		//==> �α����� ȸ���̶��...
		if(   user != null   )  {
			//==> �α��� ���¿��� ���� �Ұ� URI
			String uri = request.getRequestURI();  // query string�� ����
			
			if(		uri.indexOf("addUserView") != -1 	|| 	uri.indexOf("addUser") != -1 || 
					uri.indexOf("loginView") != -1 			||	uri.indexOf("login") != -1 		|| 
					uri.indexOf("checkDuplication") != -1 ){
				request.getRequestDispatcher("/not_login_index.jsp").forward(request, response);
				System.out.println("[ �̹� �α����� �����Դϴ�...]");
				System.out.println("[ LogonCheckInterceptor end........]\n");
				return false;
			}
			
			/////////////////// security ���� ��� start /////////////////////
			
			if( uri.indexOf("getUser") != -1) {
				
				String userId = user.getUserId();
				if(  !(userId.equals("admin") || request.getParameter("userId").equals(userId)) ) {
					
					System.out.println("�ش� �������� Ÿ ���� ��ȸ ������ �����ϴ�...");
					return false;
				}
			}
			
			if( uri.indexOf("listProduct") != -1) {
				
				String userId = user.getUserId();
				if( uri.indexOf("manage") != -1 && !(userId.equals("admin")) ) {
					
					System.out.println("�ش� �������� ��ǰ ���� ������ �����ϴ�...");
					return false;
				}
			}
			
			if(uri.indexOf("updateProduct") != -1 ) {
				
				if( !user.getUserId().equals("admin") ) {
					System.out.println("�ش� �������� ��ǰ ���� ������ �����ϴ�...");
					return false;
				}
			}
			/////////////////// security ���� ��� end /////////////////////
			
			System.out.println("[ ���� �α��� ���� ȸ���Դϴ�. ]");
			System.out.println("[ LogonCheckInterceptor end........]\n");
			return true;
		}else{ //==> �� �α����� ȸ���̶��...
			//==> �α��� �õ� ��.....
			String uri = request.getRequestURI();
			System.out.println("uri = " + uri);
			if(		uri.indexOf("addUserView") != -1 	|| 	uri.indexOf("addUser") != -1 || 
					uri.indexOf("loginView") != -1 			||	uri.indexOf("login") != -1 		|| 
					uri.indexOf("checkDuplication") != -1 || uri.indexOf("loginChecker") != -1 ){
 				System.out.println("[ �α��� �õ� �� .... ]"); 
				System.out.println("[ LogonCheckInterceptor end........]\n");
				return true;
			}
			    
			// if ( ( uri.indexOf("listProduct") != -1 || uri.indexOf("getProduct") != -1 ) && request.getParameter("menu").equals("search") ) {
			if ( ( uri.indexOf("listProduct") != -1 || uri.indexOf("getProduct") != -1 ) && uri.indexOf("search") != -1 ) {    
				System.out.println("[��α��� ȸ���� ��ǰ ��ȸ�� �����մϴ�.]");
				// System.out.println("currentPage = "+request.getParameter("currentPage"));
				return true;
			}
			
			request.getRequestDispatcher("/user/loginView.jsp").forward(request, response);
			System.out.println("[ ���� �α������� �ʾҽ��ϴ� ... ]");
			System.out.println("[ LogonCheckInterceptor end........]\n");
			return false;
		}
	}
}//end of class