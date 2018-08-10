package com.bidomi.astrid.Controllers;

import com.bidomi.astrid.Model.User;
import com.bidomi.astrid.Repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@CrossOrigin
@RequestMapping(path = "/api")
public class MainController {
    private static final Logger log= LoggerFactory.getLogger(MainController.class);

    @Autowired
    private UserRepository userRepository;

    @GetMapping(path = "/add")
    public @ResponseBody String addNewUser(@RequestParam String firstName,
                                           @RequestParam String lastName,
                                           @RequestParam String email){
        User n=new User();
        n.setFirstName(firstName);
        n.setLastName(lastName);
        n.setEmail(email);
        userRepository.save(n);
        log.info("added User with "+n.getFirstName()+" name.");
        return Long.toString(n.getId());
    }

    @GetMapping(path = "/byId")
    public @ResponseBody User getById(@RequestParam long id) {
        System.out.println("byName parameter = "+id);
        return userRepository.findById(id);
    }

    @GetMapping(path = "/byName")
    public @ResponseBody
    User getByFirstName(@RequestParam String firstName){
        System.out.println("byName parameter = "+firstName);
        return userRepository.findByFirstName(firstName);
    }

    @GetMapping(path = "/byEmail")
    public @ResponseBody List<User> getByEmail(@RequestParam String email) {
        System.out.println("byName parameter = "+email);
        return userRepository.findByEmail(email);
    }

    @GetMapping(path = "/all")
    public @ResponseBody User getAll(){
        System.out.println("getAll()");
        return getById(1);
    }
}
