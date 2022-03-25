package com.smart.cofig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.smart.dao.UserRepository;  
import com.smart.entities.User;

public class UserServiceDetailsImpl implements UserDetailsService{
    @Autowired
	private UserRepository userrepo;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user = userrepo.getUserByUserName(username);
		System.out.println("This is loadUserByUsername method "+user.getEmail());
		if(user==null) {
			throw new UsernameNotFoundException("Could not found user !!");
		}
		CustomUserDetails customuserdetails =new CustomUserDetails(user);
		return customuserdetails;
	}
	

}
