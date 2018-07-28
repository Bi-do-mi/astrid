package com.bidomi.astrid.Services;

import com.bidomi.astrid.Model.Role;
import com.bidomi.astrid.Model.User;
import com.google.common.collect.ImmutableList;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class UserService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return new User("George", "Mikle", "gm@gmail.com",
                ImmutableList.of(Role.GUEST), "password", "username",
        false, true, true, true);
    }
}
