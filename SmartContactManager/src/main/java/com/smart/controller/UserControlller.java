package com.smart.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.smart.dao.ContactRepo;
import com.smart.dao.UserRepository;
import com.smart.entities.Contact;
import com.smart.entities.User;
import com.smart.helper.Message;

@Controller
@RequestMapping("/user")
public class UserControlller {
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private ContactRepo contactRepo;
	//This method we can use as common method
//	@ModelAttribute
//	public void addCommonData(Model model,Principal princpal) {
//		String userName = princpal.getName();
//		System.out.println("User Name is "+userName);
//		User user = userRepo.getUserByUserName(userName);
//		System.out.println("User is "+user);
//		model.addAttribute("user",user);
//	}
	@RequestMapping("/index")
	public String dashboard(Model model,Principal princpal) {
		System.out.println("This is dashboard method");
		String userName = princpal.getName();
		User user = userRepo.getUserByUserName(userName);
		model.addAttribute("user",user);
		
		model.addAttribute("title","Home menu");
		return "normal/user_dashboard";
	}
	@GetMapping("/add-contact")
	public String openAddContactForm(Model model,Principal princpal) {
		model.addAttribute("title","Add contact");
		model.addAttribute("contact",new Contact());
		String userName = princpal.getName();
		User user = userRepo.getUserByUserName(userName);
		model.addAttribute("user",user);
		System.out.println("This is add cotact  method");
		return "normal/add_contact_form";
	}
	
	//Processing add contact form
	@PostMapping("/process-contact")
	public String processContact(@ModelAttribute Contact contact,@RequestParam("profileImage") MultipartFile file, Principal princpal,Model model,HttpSession session) {
		try {
		System.out.println("Start adding contact in DB");
		String name = princpal.getName();
		User user = userRepo.getUserByUserName(name);
		contact.setUser(user);
		if(file.isEmpty()) {
			System.out.println("File is empty");
		}
		else {
			contact.setImage(file.getOriginalFilename());
			File saveFile = new ClassPathResource("static/img").getFile();
			Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
			Files.copy(file.getInputStream(),path,StandardCopyOption.REPLACE_EXISTING);
			System.out.println("Image is uploaded");
		}
		user.getContacts().add(contact);
		this.userRepo.save(user);
	//	System.out.println("User From Process-contact "+user.toString());
		System.out.println("Contact is "+contact);
		System.out.println("Added to data base");
		model.addAttribute("user", user);
		model.addAttribute("title", "Add contact");
		//Message success
		session.setAttribute("message",new Message("Your contact is added !! Add more..","success"));
		}
		catch(Exception e) {
			e.printStackTrace();
			System.out.println("Error "+e.getMessage());
			//Message failure
			session.setAttribute("message",new Message("Something went wrong !! Try again","danger"));
		}
		return "normal/add_contact_form";
	}
	
	//Show contact handler
	@GetMapping("/show-contacts")
	public String showContacts(Model m,Principal princpal) {
		m.addAttribute("title","Show contacts");
		String userName = princpal.getName();
		User user = userRepo.getUserByUserName(userName);
		m.addAttribute("user", user);
		List<Contact> contacts = contactRepo.findContactsByUser(user.getId());
		m.addAttribute("contacts",contacts);
		
		
		return "normal/show_contacts";
	}
	@GetMapping("/delete/{cid}")
	public String deleteContact(@PathVariable("cid") Integer cId,Model model,HttpSession session,Principal princpal) {
		model.addAttribute("title","Show contacts");
		String userName = princpal.getName();
		User user = userRepo.getUserByUserName(userName);
//		List<Contact> contacts = contactRepo.findContactsByUser(user.getId());
//		model.addAttribute("contacts",contacts);
		
		model.addAttribute("user", user); 
		Optional<Contact> findById = this.contactRepo.findById(cId);
		 Contact contact = findById.get();
		 contact.setUser(null);
		 this.contactRepo.delete(contact);
		 System.out.println("Contact deleted");
		 session.setAttribute("message", new Message("Contact deleted successfully...","success"));
		return "redirect:/user/show-contacts";
	}
	
}
