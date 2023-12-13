package br.edu.utfpr.commerceapi.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import br.edu.utfpr.commerceapi.repositories.PessoaRepository;

@Component
public class TokenUserDetailService implements UserDetailsService {
    @Autowired
    private PessoaRepository personRepository;

    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return personRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}