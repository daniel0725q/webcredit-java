package com.quinterodaniel.webcreditjava.controller;

import com.quinterodaniel.webcreditjava.dto.PaymentDTO;
import com.quinterodaniel.webcreditjava.service.impl.PaymentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
public class PaymentsController {

    private PaymentServiceImpl paymentService;

    @Autowired
    public PaymentsController(PaymentServiceImpl paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    @RequestMapping("/pay")
    public ResponseEntity pay(@RequestBody PaymentDTO payment) throws Exception {
        var quota = paymentService.performPayment(payment);
        paymentService.recalculateQuotas(quota);

        return ResponseEntity.ok(null);
    }
}
