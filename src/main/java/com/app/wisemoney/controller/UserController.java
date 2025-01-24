package com.app.wisemoney.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.app.wisemoney.entity.UserEntity;
import com.app.wisemoney.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping(value="/user")
public class UserController {
	@Autowired
	private UserService userService;
	
	@PostMapping("/adduser")
	public String addUser(@ModelAttribute UserEntity user, RedirectAttributes redirectAttributes) {
	    // Check if username or email already exists
	    if (userService.userExists(user.getUsername(), user.getEmail())) {
	        redirectAttributes.addFlashAttribute("error", "Username or Email already exists. Please try with a different one.");
	        return "redirect:/signup"; // Redirect back to the signup page
	    }

	    // Save the new user
	    userService.addUser(user);
	    redirectAttributes.addFlashAttribute("message", "User Added Successfully");
	    return "redirect:/";
	}

	 // Method to handle user login
    @PostMapping("/login")
    public String loginUser(@ModelAttribute("eun") String eun, @ModelAttribute("password") String password, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        // Retrieve user by username or email
        var userOpt = userService.getUserByUsernameOrEmail(eun, eun); // Searching by username or email
        
        if (userOpt.isPresent()) {
            UserEntity user = userOpt.get();
            // Check if passwords match
            if (user.getPassword().equals(password)) {
                // Set session attributes
            	session.setAttribute("user_id", user.getUserId());
                session.setAttribute("username", user.getUsername());
                session.setAttribute("email", user.getEmail());
                session.setAttribute("loggedIn", true);
                redirectAttributes.addFlashAttribute("message", "Login Successful!");
                return "redirect:/transaction"; // Redirect to the home page or any authenticated page
            } else {
                redirectAttributes.addFlashAttribute("error", "Invalid Password");
                return "redirect:/"; // Redirect to login page with error message
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "User not found");
            return "redirect:/"; // Redirect to login page with error message
        }
    }

    // Method to logout
    @RequestMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate(); // Invalidate the session to log the user out
        redirectAttributes.addFlashAttribute("message", "Logged out successfully");
        return "redirect:/"; // Redirect to the home page after logout
    }
    
    // Method to show the Forgot Password form
    @GetMapping("/forgot-password")
    public String showForgotPasswordForm(Model model) {
        model.addAttribute("showResetForm", false); // Initially, do not show reset form
        return "forgot-password";
    }

    // Method to handle Forgot Password request (Step 1)
    @PostMapping("/forgot-password")
    public String processForgotPassword(@ModelAttribute("eun") String eun, Model model, RedirectAttributes redirectAttributes) {
        // Retrieve user by username or email
        Optional<UserEntity> userOpt = userService.getUserByUsernameOrEmail(eun, eun);
        
        if (userOpt.isPresent()) {
            model.addAttribute("showResetForm", true); // Show reset password form
            model.addAttribute("userId", userOpt.get().getUserId()); // Store userId in model
            return "forgot-password";
        } else {
            redirectAttributes.addFlashAttribute("error", "User not found");
            return "redirect:/user/forgot-password"; // Redirect back with error
        }
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam("userId") Long userId, 
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmPassword") String confirmPassword,
                                 RedirectAttributes redirectAttributes) {

        System.out.println("Reset Password: Received UserId: " + userId);

        // Check if passwords match
        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Passwords do not match");
            System.out.println("Reset Password: Passwords do not match");
            return "redirect:/user/forgot-password"; // Redirect back with error
        }

        // Retrieve the user and update password
        Optional<UserEntity> userOpt = userService.getUserById(userId);
        if (userOpt.isPresent()) {
            UserEntity user = userOpt.get();
            user.setPassword(newPassword); // Update the password
            userService.addUser(user); // Save the user with updated password
            redirectAttributes.addFlashAttribute("message", "Password successfully reset. Please login.");
            System.out.println("Reset Password: Password reset successful. Redirecting to /");
            return "redirect:/"; // Redirect to login page
        } else {
            redirectAttributes.addFlashAttribute("error", "User not found");
            System.out.println("Reset Password: User not found");
            return "redirect:/user/forgot-password"; // Redirect back with error
        }
    }

}

