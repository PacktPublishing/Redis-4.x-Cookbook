package packt.rediscookbook.springdataredisdemo.controller;

import packt.rediscookbook.springdataredisdemo.model.User;
import packt.rediscookbook.springdataredisdemo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/rest/user")
public class UserController {

    @Autowired
    private UserService userRepository;

    @PostMapping("/{id}")
    public User add(@PathVariable String id,
                    @RequestParam String name,
                    @RequestParam String sex,
                    @RequestParam String nation){
        return userRepository.save(
                new User(id,name,sex,nation, Instant.now().getEpochSecond())
        );
    }

    @GetMapping("/{id}")
    public User findById(@PathVariable String id){
        return userRepository.findById(id);
    }


    @PutMapping("/{id}")
    public User updateUserById(@PathVariable String id,
        @RequestParam String name, @RequestParam String sex,
        @RequestParam String nation, @RequestParam long register_time){

        return userRepository.update(
                new User(id,name,sex,nation,register_time)
        );
    }

    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable String id){
        userRepository.delete(id);
    }
}
