package First.Spring.controller;

import First.Spring.dto.UserDTO;
import First.Spring.model.Test;
import First.Spring.model.User;
import First.Spring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

// JDBC JPA(java persistence api)
//JPQL(java persistence query language) SQL(native)

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

    @CrossOrigin
    @GetMapping(value = "/category/{unique}")
    public List<?> getCategory(
            @PathVariable("unique") String category
    ){
        System.out.println(category);
        if(category.equals("users")){
            return userRepository.findAll();
        } 
        if(category.equals("tests")){
            return tests;
        }
        return new ArrayList<>(Arrays.asList("404"));
    }

    @CrossOrigin
    @PostMapping("/json")
    public User submitData(@Valid @RequestBody UserDTO user){
        User save = userRepository.save(user.getUser());
        System.out.println(user);
        System.out.println(save);
        return save;
    }

    @CrossOrigin
    @GetMapping("/sample")
    public ResponseEntity<?> sample(@RequestParam("number") int num, HttpServletResponse response){
        boolean average = num > 49 ? true : false;
        response.setStatus(400);
        userRepository.save(new User());
        return new ResponseEntity<>("Failed", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/user")
    public ResponseEntity<?> user(@RequestParam("id") long num){
        Optional<User> byId = userRepository.findById(num);
        if(byId.isEmpty()){
//            byId.get().getFirstname();
            return new ResponseEntity<>("No user by that id", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(byId.get(), HttpStatus.OK);
    }

    @PatchMapping("/user/update/email")
    public String updateUser(@Valid @RequestBody UserDTO user){
        if(user==null) return "Invalid request body";
        if(user.getEmail()==null || user.getEmail().equals("")) {
            return "Invalid Email";
        }
        if(user.getId()<1){
            return "Invalid id";
        }
        Optional<User> byId = userRepository.findById(user.getId());
        if(byId.isEmpty() ){
            return "user doesn't exist";
        }
        User dbUser = byId.get();
        dbUser.setEmail(user.getEmail());
//        userRepository.save(dbUser);
        return "user updated";
    }

    @GetMapping("users/count")
    public String usersCount(@RequestParam String name, Pageable pageable){
        long count = userRepository.count();
//        userRepository.findByFirstname("asghashg");
        Page<User> byFirstname = userRepository.findByFirstname(name, pageable);
        System.out.println("content: "+byFirstname.getContent());
        System.out.println("total Pages: "+byFirstname.getTotalPages());
        System.out.println("total Users: "+byFirstname.getTotalElements());
        return String.valueOf(count);
    }

}
