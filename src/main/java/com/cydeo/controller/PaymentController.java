package com.cydeo.controller;

import com.cydeo.dto.PaymentDTO;
import com.cydeo.dto.request.ChargeRequest;
import com.cydeo.entity.Payment;
import com.cydeo.enums.Currency;
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
import java.math.BigDecimal;
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

        model.addAttribute("amount", paymentDTO.getAmount().multiply(BigDecimal.valueOf(100)));
        model.addAttribute("stripePublicKey", stripePublicKey);
        model.addAttribute("currency", Currency.USD);
        model.addAttribute("modelId", paymentDTO.getId());

        return "payment/payment-method";
    }

    @PostMapping("/charge/{paymentId}")
    public String charge(ChargeRequest request, Model model, @PathVariable("paymentId") Long paymentId ) throws StripeException {

        request.setCurrency(Currency.USD);
        request.setDescription("Monthly subscription fee for CYDEO Accounting App");

        Charge charge = paymentService.charge(request, paymentId);

        model.addAttribute("id", charge.getId());
        model.addAttribute("status", charge.getStatus());
        model.addAttribute("chargeId", charge.getId());
        model.addAttribute("balance_transaction", charge.getBalanceTransaction());

        if (charge.getId()==null){
            model.addAttribute("error", "Transaction Failed");
        }

        return "payment/payment-result";
    }

}
