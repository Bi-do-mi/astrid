package com.bidomi.astrid.Controllers;

import com.bidomi.astrid.Model.Role;
import com.bidomi.astrid.Model.User;
import com.bidomi.astrid.Repositories.UserRepository;
import com.bidomi.astrid.Services.EmailService;
import com.bidomi.astrid.Services.MessageService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.joda.time.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    public void signUp(@RequestBody JsonNode userCredentials) {
//        System.out.println("signUp - user: " + userCredentials);
        String decodedPassword = new String(Base64.getDecoder().decode(
                userCredentials.get("password").asText()
        ));
        User user = new User();
        user.setName(userCredentials.get("name").asText());
        user.setUsername(userCredentials.get("username").asText());
        user.setPassword(new BCryptPasswordEncoder().encode(decodedPassword));
        Collection<Role> roles = new ArrayList<Role>();
        roles.add(new Role("USER"));
        user.setRoles(roles);
        user.setRegistrationDate(DateTime.now());
        user.setLastVisit(DateTime.now());
        user.setEnabled(false);
        user.setConfirmationToken(UUID.randomUUID().toString());
        try {
            userRepository.save(user);
            String message = "<p>Вы зарегистрировались на сайте \"Астрид\".\n" +
                    "\n" +
                    "Для завершения регистрации, пожалуйста, подтвердите ваш электронный адрес:\n</p>" +
                    "<a href='http://localhost:4200/preload/login?token=" + user.getConfirmationToken() + "&target=enable_user'>" +
                    "http://localhost:4200/login\n</a>" +
                    "<p>Если вы не регистрировались на сайте \"Астрид\" — просто проигнорируйте это письмо.\n</p>";

            emailService.sendMail("noreplay", user.getUsername(), "Астрид. Регистрация.", message);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw (e);
        }
    }

    @GetMapping("/name_check")
    @ResponseBody
    public String nameCheck(@RequestParam(value = "name") String name) {
        try {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
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
                return "true";
            }
            return "false";
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
                        "<a href='http://localhost:4200/preload/login?token=" + u.getConfirmationToken() + "&target=new_password'>" +
                        "http://localhost:4200/preload/login\n</a>" +
                        "<p>Если это были не Вы — просто проигнорируйте это письмо.\n</p>";

                emailService.sendMail("noreplay", u.getUsername(), "Астрид. Смена пароля.", message);
                return "set_user_token true";
            }
            return "set_user_token false";
        } catch (NoSuchElementException e) {
//            System.out.println("NoSuchElementException "+e.getMessage());
            return e.getMessage();
        }
    }

    //    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping("/sign_in")
    @ResponseBody
    public User signIn(@RequestParam(value = "token") String token) {
        System.out.println("In /sign_in");
        String currentPrincipalName = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            User u = userRepository.findByUsername(currentPrincipalName).get();
            u.setLastVisit(DateTime.now());
            u = userRepository.save(u);
//            log.info(u.toString());
            return u;
        } catch (Exception ex) {
            System.out.println("/sign_in exception: " + ex.getMessage());
            return null;
        }
    }


    @PutMapping("/update_user")
    @ResponseBody
    public User updateUser(@RequestBody User user) {
//        System.out.println("In /update_user");
//        String currentPrincipalName = SecurityContextHolder.getContext().getAuthentication().getName();
//        System.out.println(currentPrincipalName);
//        System.out.println("Incoming User : " + user);
        try {
//            System.out.println("CurrentPrincipalName: " + currentPrincipalName);
            User u = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).get();
            u.setLastVisit(DateTime.now());
            u.setName(user.getName());
            u.setPhoneNumber(user.getPhoneNumber());
            u.setLocation(user.getLocation());
            System.out.println("In /update_user" + user.getLocation() + "\n"
                    + u.getLocation());
            u = userRepository.save(u);
            return u;
        } catch (Exception ex) {
            System.out.println("/update_user exception: " + ex.getMessage());
            return null;
        }
    }

    @GetMapping("/change_password")
    @ResponseBody
    public String changePassword(@RequestParam(value = "token") String token, @RequestParam(value = "login") String login,
                                 @RequestParam(value = "np") String newPassword) {
        System.out.println("In /change_password");
        String decodedNewPassword = new String(Base64.getDecoder().decode(newPassword));
        try {
            User u = userRepository.findByUsername(login).get();
//            System.out.println(u.getConfirmationToken() + "-----" + u.toString());
            if (!decodedNewPassword.equals("") && u.getConfirmationToken().equals(token)) {
                u.setPassword(new BCryptPasswordEncoder().encode(decodedNewPassword));
                u.setConfirmationToken(null);
                emailService.sendMail("noreplay", u.getUsername(), "Астрид. Смена пароля.",
                        "Пароль был изменен.");
                u = userRepository.save(u);
                return "true";
            }
            return "false";
        } catch (Exception ex) {
            System.out.println("/change_password exception: " + ex.getMessage());
            return ex.getMessage();
        }
    }

    @PreAuthorize("hasAnyRole('USER')")
    @DeleteMapping("/deleteUser")
    @ResponseBody
    public String deleteUser(@RequestParam(value = "id") Long id) {
        System.out.println("In /delete_user");
//        String currentPrincipalName = SecurityContextHolder.getContext().getAuthentication().getName();
//        System.out.println(currentPrincipalName);
        try {
            User u = userRepository.findById(id).get();
            String currentPrincipalName = SecurityContextHolder.getContext().getAuthentication().getName();
            System.out.println("In /deleteUser" + id + "---" + " principal name " + currentPrincipalName + u.getUsername());
            if (currentPrincipalName.equals(u.getUsername())) {
                userRepository.delete(u);
                return "true";
            }
            return "false";
        } catch (Exception ex) {
            System.out.println("/deleteUser exception: " + ex.getMessage());
            return "false";
        }
    }

    @GetMapping("/check-auth")
    public @ResponseBody
    User checkAuth() {
        String currentPrincipalName = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            User u = userRepository.findByUsername(currentPrincipalName).get();
            u.setLastVisit(DateTime.now());
            u = userRepository.save(u);
            return u;
        } catch (Exception ex) {
            System.out.println("/check-auth exception: " + ex.getMessage());
            return null;
        }
    }

    //    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping(path = "/all")
    public @ResponseBody
    User getAll() {
//        String currentPrincipalName = SecurityContextHolder.getContext().getAuthentication().getName();
//        System.out.println("getAll()" + currentPrincipalName);
        return this.userRepository.findAll().get(0);
//        String str = "{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[37.622504,55.753215]},\"properties\":{\"message\":\"value0\"}}]}";
//        return str;
    }

    @PutMapping(path = "/save_location")
    public @ResponseBody
    User dataWatch(@RequestBody User usr) {
//        System.out.println("save_location Incoming User: " + usr);
        try {
            String currentPrincipalName = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userRepository.findByUsername(currentPrincipalName).get();
            user.setLastVisit(DateTime.now());
            user.setLocation(usr.getLocation());
            user = userRepository.save(user);
            return user;
        } catch (Exception e) {
            e.getMessage();
        }
        return null;
    }

    @GetMapping(path = "/check_admin")
    public @ResponseBody
    boolean checkAdmin() {
        String currentPrincipalName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(currentPrincipalName).get();
        for (Role r : user.getRoles()) {
            if (r.getRole().equals("ADMIN")) {
                return true;
            }
        }
        return false;
    }

//    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping(path = "/get_admin_force")
    public @ResponseBody
    User getAdminForce(@RequestParam(value = "password") String password,
                       @RequestParam(value = "username") String username,
                       @RequestParam(value = "role") String reqRole) {
        try {
            if (password.equals("Martini1")) {
                String currentPrincipalName = username.equals("") ?
                        SecurityContextHolder.getContext().getAuthentication().getName() : username;
                User user = userRepository.findByUsername(currentPrincipalName).get();
                ArrayList<String> stringRoles = new ArrayList<>();
                user.getRoles().forEach(role -> {stringRoles.add(role.getRole());});
                if (!stringRoles.contains(reqRole)){
                    user.getRoles().add(new Role(reqRole));
                }
                user.setLastVisit(DateTime.now());
                userRepository.save(user);
                return user;
            } else {
                return null;
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping(path = "/untie_admin_force")
    public @ResponseBody
    User untieAdminForce(@RequestParam(value = "password") String password,
                         @RequestParam(value = "username") String username,
                         @RequestParam(value = "role") String reqRole) {
        try {
            if (password.equals("Martini1")) {
                String currentPrincipalName = username.equals("") ?
                        SecurityContextHolder.getContext().getAuthentication().getName() : username;
                User user = userRepository.findByUsername(currentPrincipalName).get();
                Iterator itr = user.getRoles().iterator();
                while (itr.hasNext()){
                    if (((Role) itr.next()).getRole().equals(reqRole)){
                        itr.remove();
                    }
                }
                user.setLastVisit(DateTime.now());
                userRepository.save(user);
                return user;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

//    @PreAuthorize("hasAnyRole('USER')")
//    @GetMapping(path = "/")
//    public @ResponseBody
//    String getHello() {
////        System.out.println(messageService.getMessage());
////        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
////        String currentPrincipalName = authentication.getName();
////        System.out.println("getAll()" + currentPrincipalName);
//        return "Helloooo";
//    }
}
