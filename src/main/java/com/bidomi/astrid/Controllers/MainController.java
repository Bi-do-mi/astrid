package com.bidomi.astrid.Controllers;

import com.bidomi.astrid.Model.User;
import com.bidomi.astrid.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(path = "/user")
public class MainController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping(path = "/add")
    public @ResponseBody String addNewUser(@RequestParam String firstName,
                                           @RequestParam String lastName){
        User n=new User();
        n.setFirstName(firstName);
        n.setLastName(lastName);
        userRepository.save(n);
        return Long.toString(n.getId());
    }

    @GetMapping(path = "/byName")
    public @ResponseBody User getByFirstName(@RequestParam String firstName){
        return userRepository.findByFirstName(firstName).get(0);
    }
}
