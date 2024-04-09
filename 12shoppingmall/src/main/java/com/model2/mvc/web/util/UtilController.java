package com.model2.mvc.web.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/util/*")
public class UtilController {

	public UtilController() {
		// TODO Auto-generated constructor stub
	}

	// HandlerAdaptor�� HttpServletRequest�� HttpServletResponse�� ������ proxy ��ü�� �������� Controller ȣ����
	@GetMapping("/history")
	public String history(HttpServletRequest request, Model model) {
		String histories = "";
		String historyNo = "";
		Cookie[] cookies = request.getCookies();

		System.out.println("=================");
		System.out.println("cookie ���� = "+cookies.length);
		for (Cookie cookie : request.getCookies()) {
			System.out.println(cookie.getName()+" Cookie�� ���� = "+cookie.getMaxAge());
			if(cookie.getName().equals("histories")) {
				histories = cookie.getValue();
				System.out.println("histories = " + histories);
			}
			if(cookie.getName().equals("historyNo")) {
				historyNo = cookie.getValue();
				System.out.println("historyNo = " + historyNo);
			}
		}
		System.out.println("=================");

		model.addAttribute("histories", histories.split("-") );
		model.addAttribute("historyNo", historyNo.split("-") );
		
		return "forward:/history.jsp";
	}
}
