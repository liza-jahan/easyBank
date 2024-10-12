package com.example.easybank.controller;

import com.example.easybank.model.Customer;
import com.example.easybank.repository.CustomerRepository;
import com.example.easybank.service.EasyBankDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class UserController {
    private final EasyBankDetailsService easyBankDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final CustomerRepository customerRepository;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody Customer customer) {
        try {
            String hashPwd = passwordEncoder.encode(customer.getPwd());
            customer.setPwd(hashPwd);
            Customer saveCustomer = customerRepository.save(customer);
            if (saveCustomer.getId() > 0) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Given user details are successfully registered ");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("User registration failed ");

            }
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An exception occurred " + ex.getMessage());
        }
    }
}
