package com.bidomi.astrid.Services;

import com.bidomi.astrid.Model.CustomUserDetails;
import com.bidomi.astrid.Model.User;
import com.bidomi.astrid.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Optional;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

//    @PostConstruct
//    public void init(){
//        userRepository.findByUsername("Alla").ifPresent(user -> {
//            user.setPassword(new BCryptPasswordEncoder()
//                    .encode("password"));
//            userRepository.save(user);
//        });
//    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
        user.orElseThrow(() -> new UsernameNotFoundException("Username not found!!!"));

        return user.map(CustomUserDetails::new).get();
    }
}
