package com.example.easybank.service;

import com.example.easybank.model.Customer;
import com.example.easybank.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class EasyBankDetailsService implements UserDetailsService {
 private final CustomerRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      Customer customer= customerRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User details not found for the user:"+ username));
        List<GrantedAuthority> authorities=List.of(new SimpleGrantedAuthority(customer.getRole()));
        return new User(customer.getEmail(),customer.getPwd(),authorities);
    }
}
