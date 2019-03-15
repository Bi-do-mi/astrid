package com.bidomi.astrid.Controllers;

import com.bidomi.astrid.Model.Role;
import com.bidomi.astrid.Model.User;
import com.bidomi.astrid.Model.UserDeserializer;
import com.bidomi.astrid.Repositories.UserRepository;
import com.bidomi.astrid.Services.EmailService;
import com.bidomi.astrid.Services.MessageService;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import org.geolatte.geom.Point;
import org.geolatte.geom.jts.JTS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
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
    ObjectMapper mapper = new ObjectMapper();
    SimpleModule module = new SimpleModule("UserDeserializer",
            new Version(1, 0, 0, null, null, null));


    public UserController(UserRepository userRepository, MessageService messageService) {
        this.userRepository = userRepository;
        this.messageService = messageService;
        this.module.addDeserializer(User.class, new UserDeserializer());
        this.mapper.registerModule(module);
    }

    @PostMapping("/sign_up")
    public void signUp(@RequestBody String usr) {
        User user = new User();
        try {
            user = this.mapper.readValue(usr, User.class);
        } catch (Exception e) {
            e.getMessage();
        }
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        Collection<Role> roles = new ArrayList<Role>();
        roles.add(new Role("USER"));
        user.setRoles(roles);
        user.setRegistrationDate(System.currentTimeMillis());
        user.setLastVisit(System.currentTimeMillis());
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
    public User signIn() {
        System.out.println("In /sign_in");
        String currentPrincipalName = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            User u = userRepository.findByUsername(currentPrincipalName).get();
            u.setLastVisit(System.currentTimeMillis());
            u = userRepository.save(u);
            u.setPassword(null);
//            log.info(u.getLastVisit().toString());
            return u;
        } catch (Exception ex) {
            System.out.println("/sign_in exception: " + ex.getMessage());
            return null;
        }
    }


    @PutMapping("/update_user")
    @ResponseBody
    public User updateUser(@RequestBody String usr) {
        User user = new User();
        try {
            user = this.mapper.readValue(usr, User.class);
        } catch (Exception e) {
            e.getMessage();
        }
        System.out.println("In /update_user");
//        String currentPrincipalName = SecurityContextHolder.getContext().getAuthentication().getName();
//        System.out.println(currentPrincipalName);
        System.out.println("Incoming User : " + user);
        try {
//            System.out.println("CurrentPrincipalName: " + currentPrincipalName);
            User u = userRepository.findById(user.getId()).get();
            u.setLastVisit(System.currentTimeMillis());
            u.setName(user.getName());
            u.setPhoneNumber(user.getPhoneNumber());
            u = userRepository.save(u);
            u.setPassword(null);
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
                u.setPassword(null);
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
//            System.out.println("In /deleteUser" + id + "---" + token + "---" + u.getRegistrationDate());
            userRepository.delete(u);
            return "true";
        } catch (Exception ex) {
            System.out.println("/deleteUser exception: " + ex.getMessage());
            return "false";
        }
    }

    @PostMapping("/check-auth")
    public @ResponseBody
    User checkAuth() {
        String currentPrincipalName = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            User u = userRepository.findByUsername(currentPrincipalName).get();
            u.setLastVisit(System.currentTimeMillis());
            u = userRepository.save(u);
            u.setPassword(null);
            return u;
        } catch (Exception ex) {
            System.out.println("/check-auth exception: " + ex.getMessage());
            return null;
        }
    }

    //    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping(path = "/")
    public @ResponseBody
    List<com.bidomi.astrid.Model.User> getAll() {
//        System.out.println(messageService.getMessage());
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String currentPrincipalName = authentication.getName();
//        System.out.println("getAll()" + currentPrincipalName);
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getName());
        return this.userRepository.findAll();
    }

    @PutMapping(path = "/data_watch")
    public void dataWatch(@RequestBody String user) {
        try {
            User u = this.mapper.readValue(user, User.class);
            System.out.println("dataWatch: " + u.getLocation());
        } catch (Exception e) {
            e.getMessage();
        }

//        try {
////            Point point = (Point) Wkt.fromWkt(user.get("location").toString());
//            System.out.println("\n dataWatch - Point: " + u.getLocation());
////            String s = user.get("location").toString();
////            System.out.printf("dataWatch - Point: " + s);
//        } catch (Exception e) {
//            System.out.println(e.getMessage()); ;
//        }
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
