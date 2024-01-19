package com.cydeo.service.impl;

import com.cydeo.dto.CompanyDTO;
import com.cydeo.dto.PaymentDTO;
import com.cydeo.entity.Company;
import com.cydeo.entity.Payment;
import com.cydeo.enums.Months;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.repository.CompanyRepository;
import com.cydeo.repository.PaymentRepository;
import com.cydeo.service.PaymentService;
import com.cydeo.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Lazy
public class PaymentServiceImpl implements PaymentService {

    private final CompanyRepository companyRepository;
    private final PaymentRepository paymentRepository;
    private final SecurityService securityService;
    private final MapperUtil mapperUtil;

    @Override
    public List<PaymentDTO> retrieveAllPayments(){
        CompanyDTO companyDTO = securityService.getLoggedInUser().getCompany();
        Company company = mapperUtil.convert(companyDTO, new Company());

        List<Payment> paymentList = paymentRepository.findAllByCompany(company);

        return paymentList.stream()
                .map(payment -> mapperUtil.convert(payment, new PaymentDTO()))
                .collect(Collectors.toList());
    }

    /**
     * Generates monthly payment for each non-deleted company (does not consider whether company is ACTIVE or not).
     */
    public void generateMonthlyPayments() {
        List<Company> companies = companyRepository.findAll();
        companies.removeIf(company -> company.getTitle().equals("CYDEO"));//not generate payment objects for CYDEO

        LocalDate currentDate = LocalDate.now();
        int currentYear = currentDate.getYear();

        for (Company company : companies) {
            for (Months month : Months.values()) {

                // Check if payments for this month have already been generated
                boolean paymentsGenerated = paymentRepository.existsByCompanyAndMonthAndYear(company, month, currentYear);

                if (!paymentsGenerated) {
                    Payment payment = new Payment();
                    payment.setYear(currentYear);
                    payment.setAmount(BigDecimal.valueOf(250)); // Monthly subscription fee $250
                    payment.setPaymentDate(LocalDate.of(currentYear, month.ordinal() + 1, 1)); // Adding 1 because Months enum starts from 0
                    payment.setPaid(false);
                    payment.setMonth(month);
                    payment.setCompany(company);

                    paymentRepository.save(payment);
                }
            }
        }
    }

    /**
     * Scheduled method for generating monthly payments.
     *
     * This method is triggered in two ways:
     * - First, it is executed when the application starts and the application context is ready.
     * - Then, it runs on the 1st day of each month.
     */
    @Scheduled(cron = "0 0 1 1 * ?") // Run at 1:00 AM on the 1st day of each month
    @EventListener(ContextRefreshedEvent.class) //By using @EventListener(ContextRefreshedEvent.class), we ensure that the generateMonthlyPaymentsScheduled method will only be executed after the Spring application context has been fully initialized
    public void generateMonthlyPaymentsScheduled() {
        generateMonthlyPayments();
    }

}
