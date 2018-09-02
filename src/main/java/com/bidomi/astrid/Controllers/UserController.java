package com.bidomi.astrid.Controllers;

import com.bidomi.astrid.Model.User;
import com.bidomi.astrid.Repositories.UserRepository;
import com.bidomi.astrid.Services.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.header.Header;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.Set;


//@CrossOrigin(origins = "*", maxAge = 3600)
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RestController
@RequestMapping(path = "/rest/users")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private UserRepository userRepository;
    private MessageService messageService;

    public UserController(UserRepository userRepository, MessageService messageService) {
        this.userRepository = userRepository;
        this.messageService = messageService;
    }

//    @PostMapping("/sign-up")
//    public void signUp(@RequestBody User user){
//        log.info("Post mapping sign-up work!");
//        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
//        userRepository.save(user);
//    }

    //    @GetMapping(path = "/authenticate")
//    public @ResponseBody User findByUsername(@RequestParam HttpServletRequest req){
//        System.out.println("authenticate()");
//        return this.userRepository.findByUsername(req.getParameter("username"));
//    }
    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping("/user")
    @ResponseBody
//    public Principal user(Principal user) {
//        return user;
//    }
    public Principal user(Principal user) {
        String currentPrincipalName = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("Log Principal to string" + currentPrincipalName);
        return user;
    }

//    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping(path = "/")
    public @ResponseBody
    List<com.bidomi.astrid.Model.User> getAll() {
        System.out.println(messageService.getMessage());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        System.out.println("getAll()" + currentPrincipalName);
        return this.userRepository.findAll();
    }
}
