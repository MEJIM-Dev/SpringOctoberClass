package First.Spring.controller;

import First.Spring.model.Test;
import First.Spring.model.User;
import First.Spring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;

@RestController
public class GetController {

    ArrayList<Test> tests = new ArrayList<>();
    ArrayList<User> users = new ArrayList<>(
            Arrays.asList(
                    new User("Sam","Den"),
                    new User("Ken","Sarah")
            )
    );

    @Autowired
    private UserRepository userRepository;


//    public GetController(UserRepository userRepository){
//        this.userRepository = userRepository;
//    }

    @CrossOrigin
    @RequestMapping(value = "/")
    public String homepage(){
        return "homepage";
    }

    @PostMapping(value = "/test")
    public Test aboutPage(@RequestParam(name = "score") String score,
                          @RequestParam String some,
                          @RequestParam(name = "score") int id){
        System.out.println("score: "+score+" id: "+id+" some: "+some);
        Test test = new Test(score,"100");
        tests.add(test);
        return test;
    }

    @CrossOrigin
    @GetMapping(value = "/test")
    public ArrayList<Test> aboutPageLogin(){
        return tests;
    }

    @GetMapping(value = "/category/{unique}")
    public ArrayList<?> getCategory(
            @PathVariable("unique") String category
    ){
        System.out.println(category);
        if(category.equals("users")){
            return users;
        } 
        if(category.equals("tests")){
            return tests;
        }
        return new ArrayList<>(Arrays.asList("404"));
    }

    @CrossOrigin
    @PostMapping("/json")
    public User submitData(@RequestBody User user){
        User save = userRepository.save(user);
        System.out.println(user);
        System.out.println(save);
        return save;
//        System.out.println(user);
//        return user;
    }

    @CrossOrigin
    @GetMapping("/sample")
    public ResponseEntity<?> sample(@RequestParam("number") int num, HttpServletResponse response){
        boolean average = num > 49 ? true : false;
        response.setStatus(400);
        return new ResponseEntity<>("Failed", HttpStatus.BAD_REQUEST);
    }
}
