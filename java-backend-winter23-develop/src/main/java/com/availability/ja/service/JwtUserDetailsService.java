package com.availability.ja.service;


import com.availability.ja.model.Credentials;
import com.availability.ja.model.Users;
import com.availability.ja.repository.CredentialsRepository;
import com.availability.ja.repository.UsersRepository;
import com.availability.ja.rest.CredentialsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

@Component
public class JwtUserDetailsService implements UserDetailsService {
    @Autowired private CredentialsRepository credentialRepository;
    @Autowired private UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Credentials> ret = credentialRepository.findByEmail(email);
        Optional<Users> userRet = usersRepository.findByEmail(email);
        if (ret.isPresent() && userRet.isPresent()) {
            Users user = userRet.get();
            String authName;
            if (user.isManager()) {
                authName = "MANAGER";
            } else if (user.isAdmin()) {
                authName = "ADMIN";
            } else {
                authName = "JA";
            }
            GrantedAuthority authority = new SimpleGrantedAuthority(authName);
            return new User(ret.get().getEmail(), ret.get().getPword(),
                    Arrays.asList(authority));
        } else throw new UsernameNotFoundException("User not found with Email: " + email);

    }
    public Boolean checkEmailDomain(String request){
        Boolean DomainAuth;

            String[] em= request.split("@");
            if(em[1].equals("teamsparq.com")){
                DomainAuth=true;
            }
            else{
                DomainAuth=false;
            }
            return DomainAuth;
    }
    public Boolean emailInDB(String email){
        Optional<Credentials> ret = credentialRepository.findByEmail(email);
        if (ret.isPresent()){
            return true;
        }
        else{
            return false;
        }
    }
}
