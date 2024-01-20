package com.cydeo.service;

import com.cydeo.dto.PaymentDTO;
import com.cydeo.entity.Payment;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;

import java.util.List;

public interface PaymentService {

    void generateMonthlyPayments();

    List<PaymentDTO> retrieveAllPayments();

    PaymentDTO findById(Long paymentId);

    Charge charge(Payment payment) throws StripeException;

}
