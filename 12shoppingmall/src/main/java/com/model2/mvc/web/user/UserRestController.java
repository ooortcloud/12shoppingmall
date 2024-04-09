package com.model2.mvc.web.user;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.model2.mvc.common.Message;
import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.user.UserService;


//==> ȸ������ RestController
@RestController  // default Content-Type :: application/json
@RequestMapping("/rest/user/*")  // DispatcherServlet���� ������ LogonCheckIntercepter�� ���ϱ� ����
public class UserRestController {
	
	///Field
	@Autowired
	@Qualifier("userServiceImpl")
	private UserService userService;
	//setter Method ���� ����... spring container�κ��� auto DI �ޱ� ����.
	
	// @Value("#{commonProperties['pageUnit']}")
	@Value("${common.pageUnit}")
	int pageUnit;
	
	// @Value("#{commonProperties['pageSize']}")
	@Value("${common.pageSize}")
	int pageSize;
		
	public UserRestController(){
		System.out.println(this.getClass());
	}
	
	// �Ϲ� App���Դ� UI ������ ���ʿ���. �̹� UI�� ���� �����ϱ�.
	/*
	 *  @ModelAttribute :: HTTP request msg�� header �κ��� query string�� auto parsing :: GET�̸� url, POST�̸� body
	 *  @RequestBody :: HTTP request msg�� body �κ��� auto parsing (query string�� ����)
	 */
	@PostMapping("/json/addUser")
	public Message addUser(@RequestBody User user) throws Exception {
		
		int result = userService.addUser(user);
		
		// �׳� String���� return�ϸ� ���󰡱� �ϴµ�, �̰��� content-type�� �𸣰���.
		if(result == 1) 
			return new Message("���������� ȸ�������� �Ϸ��Ͽ����ϴ�!");
		else
			return new Message("ȸ������ ����...");
	}
	
	@RequestMapping( value="json/getUser/{userId}", method=RequestMethod.GET )
	public User getUser( @PathVariable String userId ) throws Exception{
		
		System.out.println("/user/json/getUser : GET");
		
		//Business Logic
		return userService.getUser(userId);
	}
	
	// GET�� POST�� �����ؾ� ������ auto binder�� ����� �� �ִ�...
	// Map���� return�ϸ� �װʹ�� �˾Ƽ� inner JSON �����ؼ� key-value ���·� generate����
	@GetMapping("/json/listUser")
	public Map<String, Object> getListUser() throws Exception {
		
		// GET�� ����ϴ� ������ ���� left.jsp�� ���� ù �������� ���� �� ��... �������� ��� POST��
		Search search = new Search();
		search.setCurrentPage(1);
		search.setPageSize(pageSize);
		
		// Business logic ����
		Map<String , Object> map=userService.getUserList(search);
		
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		System.out.println(resultPage);
		
		Map<String, Object> jsonMap = new HashMap<>();
		jsonMap.put("list", map.get("list"));
		jsonMap.put("resultPage", resultPage);
		jsonMap.put("search", search);
		// jsonMap.put("title", "user");
		
		return jsonMap;
	}
	
	@PostMapping("/json/listUser")
	public Map<String, Object> postListUser(@RequestBody Search search) throws Exception {

		// null ����ó��
		if(search.getCurrentPage() == null) 
			search.setCurrentPage(1);
		search.setPageSize(pageSize);
		
		// Business logic ����
		Map<String , Object> map=userService.getUserList(search);
		
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		System.out.println(resultPage);
		
		Map<String, Object> jsonMap = new HashMap<>();
		jsonMap.put("list", map.get("list"));
		jsonMap.put("resultPage", resultPage);
		jsonMap.put("search", search);
		// jsonMap.put("title", "user");
		
		return jsonMap;
	}
	
	@PostMapping("/json/updateUser")
	public Message updateUser(@RequestBody User user, HttpSession session) throws Exception {
		//Business Logic
		int result = userService.updateUser(user);
		Message returnMsg = new Message();
		if(result == 1)
			returnMsg.setMsg("���������� ȸ�� ������ ����Ǿ����ϴ�!");
		else
			returnMsg.setMsg("ȸ�� ���� ���� ����...");
		
		// ���� session�� �������� �����Ƿ� ��ȸ �� NullPointerException�� ���...
//		String sessionId=((User)session.getAttribute("user")).getUserId();
//		if(sessionId.equals(user.getUserId())){
//			session.setAttribute("user", user);
//		}
		
		// �����غ��� view�� ���� �ʿ䰡 ������? �׳� data�� ������ ���̴� redirect�� �ʿ䰡 ���� ����.
		// @RestController������ template�� ������� �����Ƿ� "redirect:" �������� navigation ó���� �Ұ����ϴ�...
		return returnMsg;
//		return ResponseEntity.ok().location( UriComponentsBuilder
//																			.fromPath("/rest/user/json/getUser/" + user.getUserId() )
//																			.build().toUri() ).body(returnMsg);
//		response.sendRedirect("/rest/user/json/getUser/" + user.getUserId() );
		/*
		 *  @RestController���� navigation�ϴ� ���
		 *  1. @Controller�� ��ȯ��Ű��, REST API�� �ʿ��� method�� ���ؼ��� @ResponseBody ����
		 *  2. HttpServletResponse�� DI�ް� response.sendRedirect() ���
		 *  3. ResponseEntity�� location ����ϸ� return
		 */
	}
	
	@GetMapping("/json/checkDuplication/{userId}")
	public Map<String, Object> checkDuplication(@PathVariable String userId) throws Exception {
		boolean result=userService.checkDuplication(userId);

		Map<String, Object> jsonMap = new HashMap<>();
		jsonMap.put("result", new Boolean(result));
		jsonMap.put("userId", userId);
		
		return jsonMap;
	}

	// @RequestBody :: HTTP request msg�� Body �κп� �ִ� data�� Content-Type�� �°� parsing�Ͽ� domain ��ü�� auto binding
	@RequestMapping( value="json/login", method=RequestMethod.POST )
	public User login(	@RequestBody User user, HttpSession session ) throws Exception{
	
		System.out.println("/user/json/login : POST");
		//Business Logic
		System.out.println("::"+user);
		User dbUser=userService.getUser(user.getUserId());
		
		// session üũ ������ interceptor�� �Űܺ���...
		if( user.getPassword().equals(dbUser.getPassword())){
			session.setAttribute("user", dbUser);
		}
		
		return dbUser;
	}
	
	@PostMapping("/json/login/check")
	public Message loginChecker(@RequestBody User user) throws Exception {
		
		System.out.println(user);
		User result = userService.getUser(user.getUserId() );
		
		System.out.println(result);
		/// ������ �˻縦 ���� null ������ Ȯ��
		if( result == null || !result.getPassword().equals( user.getPassword() ) )
			return new Message("ID �Ǵ� PW�� �߸��Ǿ����ϴ�...");
		else
			return new Message("ok");
	}
	
	@GetMapping("/logout")
	public Message logout(HttpSession session) throws Exception {
		session.invalidate();
		
		return new Message("logout�� �����Ͽ����ϴ�.");
	}
}