package com.cydeo.service;

import com.cydeo.dto.PaymentDTO;

import java.util.List;

public interface PaymentService {

    List<PaymentDTO> retrieveAllPayments();

}
