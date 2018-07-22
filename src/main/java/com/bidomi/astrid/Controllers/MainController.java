package com.bidomi.astrid.Controllers;

import com.bidomi.astrid.Model.User;
import com.bidomi.astrid.Repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping(path = "/user")
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

    @GetMapping(path = "/byName")
    public @ResponseBody User getByFirstName(@RequestParam String firstName){
        return userRepository.findByFirstName(firstName).get(0);
    }

    @GetMapping(path = "/byEmail")
    public @ResponseBody String getByEmail(@RequestParam String email) {
        return userRepository.findByEmail(email).get(0).toString();
    }
}
