package com.app.wisemoney.controller;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.app.wisemoney.entity.TransactionEntity;
import com.app.wisemoney.service.TransactionService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/saveTransaction")
    public String saveTransaction(@ModelAttribute TransactionEntity transaction, HttpSession session, RedirectAttributes redirectAttributes) {
        Date currentDate = new Date(System.currentTimeMillis());
        transaction.setDate(currentDate);
        Long userId = (Long) session.getAttribute("user_id");
        transaction.setUserId(userId);

        transactionService.saveTransaction(transaction);
        redirectAttributes.addFlashAttribute("message", "Transaction Added Successfully");
        return "redirect:/transaction";
    }

    @PostMapping("/updateTransaction")
    public String updateTransaction(@ModelAttribute TransactionEntity transaction, HttpSession session, RedirectAttributes redirectAttributes) {
        Date currentDate = new Date(System.currentTimeMillis());
        transaction.setDate(currentDate);
        Long userId = (Long) session.getAttribute("user_id");
        transaction.setUserId(userId);

        transactionService.updateTransaction(transaction);
        redirectAttributes.addFlashAttribute("message", "Transaction Updated Successfully");
        return "redirect:/transaction";
    }

    @PostMapping("/deleteTransaction")
    public String deleteTransaction(Long transactionId, RedirectAttributes redirectAttributes) {
        transactionService.deleteTransaction(transactionId);
        redirectAttributes.addFlashAttribute("message", "Transaction Deleted Successfully");
        return "redirect:/transaction";
    }

}
