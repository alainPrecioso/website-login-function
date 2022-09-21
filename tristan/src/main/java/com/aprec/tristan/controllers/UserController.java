package com.aprec.tristan.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.aprec.tristan.user.UserServiceInterface;


@PreAuthorize("hasRole('USER')")
@Controller
@RequestMapping(path = "/")
public class UserController {

	private UserServiceInterface userService;

	public UserController(UserServiceInterface userService) {
		super();
		this.userService = userService;
	}

	
	@GetMapping("/user")
	public String user(Model model) {
		model.addAttribute("delete", userService.getLoggedUser().isDeleteScheduled());
		model.addAttribute("daysleft", userService.getLoggedUser().getDaysToDelete());
		return "user";
	}

	@PostMapping("/delete")
	public String delete() {
		
		userService.scheduleDelete(userService.getLoggedUser());
		return "redirect:user";
	}
	
	@GetMapping("/canceldelete")
	public String cancelDelete() {
		
		userService.cancelDelete(userService.getLoggedUser());
		return "redirect:user";
	}

}