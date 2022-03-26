package com.smart.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smart.dao.UserRepository;
import com.smart.entities.User;
import com.smart.helper.Message;

@Controller
public class HomeController {
	//This is home controller
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Autowired
	private UserRepository userRepo;
	
	@RequestMapping("/")
	public String home(Model model) {
		model.addAttribute("title","Home-Smart Contact Manager");
		return "home";
	}
	@RequestMapping("/about")
	public String about(Model model) {
		model.addAttribute("title","About-Smart Contact Manager");
		return "about";
	}
	@RequestMapping("/signup")
	public String signup(Model model) {
		model.addAttribute("title","Register-Smart Contact Manager");
		model.addAttribute("user",new User());
		return "signup";
	}
	@PostMapping("/do_register")
	public String registerUser(@ModelAttribute("user") User user,@RequestParam(value="agreement",defaultValue="false") boolean agreement,Model model ,HttpSession session ) {
		try{
			if(!agreement) {
				System.out.println("You have not agreed the terms and conditions");
				throw new Exception("You have not agreed the terms and conditions");
			}
			user.setRole("ROLE_USER");
			user.setEnabled("true");
			user.setImageurl("default.png");
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			User result = userRepo.save(user);
			
			System.out.println("User "+user);
			System.out.println("Agreement "+agreement);
			model.addAttribute("user",new User());
			session.setAttribute("message", new Message("Successfully Registered !! ","alert-success"));
			return "signup";
		}
		catch(Exception e){
			e.printStackTrace();
			model.addAttribute("user",user);
			session.setAttribute("message", new Message("Something went wrong !! "+e.getMessage(),"alert-danger"));
			return "signup";
		}
		
		
	}
	
	//Handler for custom Login
	@GetMapping("/signin")
	public String customLogIn(Model model) {
		model.addAttribute("title","LogIn Page");
		System.out.println("Title login is "+LogIn Page);
		return "Login";
	}
	
}
