package com.cydeo.controller;

import com.cydeo.dto.PaymentDTO;
import com.cydeo.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/list")
    public String listAllPayments(Model model){

        List<PaymentDTO> payments = paymentService.retrieveAllPayments();

        model.addAttribute("payments", payments);
        model.addAttribute("year", LocalDateTime.now().getYear());

        return "payment/payment-list";
    }

}
