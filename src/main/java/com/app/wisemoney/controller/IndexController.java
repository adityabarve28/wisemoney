package com.app.wisemoney.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.app.wisemoney.entity.TransactionEntity;
import com.app.wisemoney.service.TransactionService;

import jakarta.servlet.http.HttpSession;

@Controller
public class IndexController {
	@Autowired
	private TransactionService transactionService;
	
	@GetMapping("/")
	public String index(HttpSession session, Model model) {
	    // Check if user is logged in
	    if (session.getAttribute("loggedIn") != null && (boolean) session.getAttribute("loggedIn")) {
	        model.addAttribute("loggedIn", true);
	        model.addAttribute("username", session.getAttribute("username"));
	        model.addAttribute("user_id", session.getAttribute("user_id"));
	    } else {
	        model.addAttribute("loggedIn", false);
	    }
	    return "index"; // User's session data will be passed to the template
	}
	
	@GetMapping("/transaction")
	public String transaction(HttpSession session, Model model) {
	    if (session.getAttribute("loggedIn") != null && (boolean) session.getAttribute("loggedIn")) {
	        Long userId = (Long) session.getAttribute("user_id");
	        model.addAttribute("loggedIn", true);
	        model.addAttribute("username", session.getAttribute("username"));
	        model.addAttribute("user_id", userId);
	     // Fetch user's transactions and spending details
	        List<TransactionEntity> transactions = transactionService.getTransactionsByUserId(userId);
	        double totalSpent = transactionService.getTotalAmountSpentByUserId(userId);
	        double monthlySpent = transactionService.getTotalAmountSpentInCurrentMonth(userId);

	        // Add data to the model
	        model.addAttribute("transactions", transactions);
	        model.addAttribute("totalSpent", totalSpent); // Ensures the total spent is passed to the view
	        model.addAttribute("monthlySpent", monthlySpent);
	        model.addAttribute("isEndOfMonth", isEndOfMonth());

//	        // Fetch transactions for the logged-in user
//	        List<TransactionEntity> transactions = transactionService.getTransactionsByUserId(userId);
	        model.addAttribute("transactions", transactions);
	    } else {
	        model.addAttribute("loggedIn", false);
	    }
	    return "transaction"; // User's session data will be passed to the template
	}



	  @GetMapping("/signup")
	    public String signup() {
	        return "signup";
	  }
	  private boolean isEndOfMonth() {
	        LocalDate today = LocalDate.now();
	        return today.equals(today.withDayOfMonth(today.lengthOfMonth()));
	    }
}