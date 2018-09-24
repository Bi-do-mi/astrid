package com.bidomi.astrid.Controllers;

import com.bidomi.astrid.Model.Role;
import com.bidomi.astrid.Model.User;
import com.bidomi.astrid.Repositories.UserRepository;
import com.bidomi.astrid.Services.EmailService;
import com.bidomi.astrid.Services.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;


//@CrossOrigin(origins = "*", maxAge = 3600)
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RestController
@RequestMapping(path = "/rest/users")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private EmailService emailService;

    private UserRepository userRepository;
    private MessageService messageService;

    public UserController(UserRepository userRepository, MessageService messageService) {
        this.userRepository = userRepository;
        this.messageService = messageService;
    }

    @PostMapping("/sign_up")
    public void signUp(@RequestBody User user) {
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        List<Role> roles = new ArrayList<Role>();
        roles.add(new Role("USER"));
        user.setRoles(roles);
        user.setLastVisit(new Date());
        user.setEnabled(false);
        user.setConfirmationToken(UUID.randomUUID().toString());

        String message = "<p>Вы зарегистрировались на сайте \"Астрид\".\n" +
                "\n" +
                "Для завершения регистрации, пожалуйста, подтвердите ваш электронный адрес:\n</p>" +
                "<a href='http://localhost:4200/login?token=" + user.getConfirmationToken() + "&target=enable_user'>" +
                "http://localhost:4200/login\n</a>" +
                "<p>Если вы не регистрировались на сайте \"Астрид\" — просто проигнорируйте это письмо.\n</p>";

        emailService.sendMail("noreplay", user.getUsername(), "Астрид. Регистрация.", message);
        try {
            userRepository.save(user);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw (e);
        }
    }

    @GetMapping("/name_check")
    @ResponseBody
    public String nameCheck(@RequestParam(value = "name") String name) {
        try {
            return userRepository.findByUsername(name).get().getUsername();
        } catch (NoSuchElementException e) {
            return "not found";
        }
    }

    @GetMapping("/enable_user")
    @ResponseBody
    public String enableUser(@RequestParam(value = "token") String token) {
        try {
            User u = userRepository.findByConfirmationToken(token).get();
            if (u.getConfirmationToken().equals(token)) {
                u.setEnabled(true);
                u.setConfirmationToken(null);
                userRepository.save(u);
                return "enable_user true";
            }
            return "enable_user false";
        } catch (NoSuchElementException e) {
            return "not found";
        }
    }

    @GetMapping("/set_user_token")
    @ResponseBody
    public String setUserToken(@RequestParam(value = "login") String login) {
        try {
            User u = userRepository.findByUsername(login).get();
            if (u.getUsername().equals(login)) {
                u.setConfirmationToken(UUID.randomUUID().toString());
                userRepository.save(u);
                String message = "<p>Вы или кто-то другой попытались сменить пароль на сайте \"Астрид\".\n" +
                        "\n" +
                        "Для смены пароля нажмите на ссылку ниже:\n</p>" +
                        "<a href='http://localhost:4200/login?token=" + u.getConfirmationToken() + "&target=new_password'>" +
                        "http://localhost:4200/login\n</a>" +
                        "<p>Если это были не Вы — просто проигнорируйте это письмо.\n</p>";

                emailService.sendMail("noreplay", u.getUsername(), "Астрид. Смена пароля.", message);
                return "set_user_token true";
            }
            return "set_user_token false";
        } catch (NoSuchElementException e) {
            return "not found";
        }
    }

    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping("/sign_in")
    @ResponseBody
    public User user(@RequestParam(value = "token") String token, @RequestParam(value = "np") String newPassword) {
        String currentPrincipalName = SecurityContextHolder.getContext().getAuthentication().getName();
        String decodedNewPassword = new String(Base64.getDecoder().decode(newPassword));
        try {
            User u = userRepository.findByUsername(currentPrincipalName).get();
            u.setLastVisit(new Date());
            if (!decodedNewPassword.equals("") && u.getConfirmationToken().equals(token)) {
                u.setPassword(new BCryptPasswordEncoder().encode(decodedNewPassword));
                u.setConfirmationToken(null);
                emailService.sendMail("noreplay", u.getUsername(), "Астрид. Смена пароля.",
                        "Пароль был изменен.");
            }
            u=userRepository.save(u);
            u.setPassword(null);
            return u;
        } catch (Exception ex) {
            return null;
        }
    }


    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping(path = "/")
    public @ResponseBody
    List<com.bidomi.astrid.Model.User> getAll() {
//        System.out.println(messageService.getMessage());
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String currentPrincipalName = authentication.getName();
//        System.out.println("getAll()" + currentPrincipalName);
        return this.userRepository.findAll();
    }
}
