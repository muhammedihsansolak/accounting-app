package com.cydeo.controller;

import com.cydeo.dto.PaymentDTO;
import com.cydeo.entity.Payment;
import com.cydeo.service.PaymentService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @Value("${STRIPE_PUBLIC_KEY}")
    private String stripePublicKey;

    @GetMapping("/list")
    public String listAllPayments(Model model){

        List<PaymentDTO> payments = paymentService.retrieveAllPayments();

        model.addAttribute("payments", payments);
        model.addAttribute("year", LocalDateTime.now().getYear());

        return "payment/payment-list";
    }

    @GetMapping("/newpayment/{paymentId}")
    public String pay(@PathVariable("paymentId")Long paymentId, Model model){
        PaymentDTO paymentDTO = paymentService.findById(paymentId);

        model.addAttribute("amount", paymentDTO.getAmount().unscaledValue());
        model.addAttribute("stripePublicKey", stripePublicKey);
        model.addAttribute("currency", "USD");

        return "payment/payment-method";
    }

    @PostMapping("/charge")
    public String charge(Payment payment, Model model) throws StripeException {

        Charge charge = paymentService.charge(payment);

        model.addAttribute("id", charge.getId());
        model.addAttribute("status", charge.getStatus());
        model.addAttribute("chargeId", charge.getId());
        model.addAttribute("balance_transaction", charge.getBalanceTransaction());

        return "payment/payment-result";
    }

}
