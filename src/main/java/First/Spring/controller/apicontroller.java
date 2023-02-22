package First.Spring.controller;

import First.Spring.dto.LoginDTO;
import First.Spring.dto.UserDTO;
import First.Spring.dto.response.UserResponseDTO;
import First.Spring.model.Test;
import First.Spring.model.User;
import First.Spring.repository.UserRepository;
import First.Spring.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.*;


@RestController
@RequestMapping(value = "/api/v1/")
@RequiredArgsConstructor
public class apicontroller {

    ArrayList<Test> tests = new ArrayList<>();
    ArrayList<User> users = new ArrayList<>(
            Arrays.asList(
                    new User("Sam","Den"),
                    new User("Ken","Sarah")
            )
    );


    private final UserRepository userRepository;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    private final PasswordEncoder passwordEncoder;

    @GetMapping(value = "/users")
    public List<UserResponseDTO> getUsers(){
        Page<User> all = userRepository.findAll(Pageable.ofSize(5));
        List<UserResponseDTO> list =new ArrayList<>();
        all.getContent().forEach(e->{
            list.add(new UserResponseDTO(e));
        });
        return list;
    }
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
            List<User> all = userRepository.findAll();
            List<UserResponseDTO> list =new ArrayList<>();
            all.forEach(e->{
                list.add(new UserResponseDTO(e));
            });
            return list;
        } 
        if(category.equals("tests")){
            return tests;
        }
        return new ArrayList<>(Arrays.asList("404"));
    }

    @CrossOrigin
    @PostMapping("/auth/register")
    public ResponseEntity<?> submitData(@Valid @RequestBody UserDTO user){
        Optional<User> byEmail = userRepository.findByEmail(user.getEmail());
        if (byEmail.isPresent()){
            return new ResponseEntity<>("Email already used",HttpStatus.UNAUTHORIZED);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User save = userRepository.save(user.getUser());
        System.out.println(save);
        Map<String, Object> extraClaims = setExtraClaims(save);
        String jwt = jwtService.generateJwt(save,extraClaims);
        Map accessToken = new HashMap();
        accessToken.put("access_token",jwt);
        accessToken.put("msg","Registration Successfully");
        return ResponseEntity.ok(accessToken);
    }

    @PostMapping(value = "/auth/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDTO loginDTO){
        Optional<User> byEmail = userRepository.findByEmail(loginDTO.getEmail());
        if(byEmail.isEmpty()){
            return new ResponseEntity<>("Invalid credentials",HttpStatus.FORBIDDEN);
        }
        User user = byEmail.get();
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                loginDTO.getEmail(),
                loginDTO.getPassword(),
                user.getAuthorities()
        );
        authenticationManager.authenticate(token);
        Map<String, Object> extraClaims = setExtraClaims(user);
        String jwt = jwtService.generateJwt(user,extraClaims);
        Map accessToken = new HashMap();
        accessToken.put("access_token",jwt);
        accessToken.put("msg","Login Successfully");
        return ResponseEntity.ok(accessToken);
    }

    private static Map<String, Object> setExtraClaims(User user) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("phone", user.getPhone());
        extraClaims.put("gender", user.getGender());
        extraClaims.put("firstname", user.getFirstname());
        extraClaims.put("lastname", user.getLastname());
        extraClaims.put("role", user.getRole());
        return extraClaims;
    }

//    @PostMapping(value = "/login")
//    public ResponseEntity<?> login (@Valid @RequestBody() LoginDTO login, HttpServletResponse response){
//        ResponseEntity responseEntity = ResponseEntity.badRequest().body("Invalid Username or Password");
//        Optional<User> byEmail = userRepository.findByEmail(login.getEmail());
//        if(byEmail.isEmpty()){
//            return responseEntity;
//        }
//        User dbUser = byEmail.get();
//        if(!dbUser.getPassword().equals(login.getPassword())){
//            return responseEntity;
//        }
//        Cookie cookie = new Cookie("email",dbUser.getEmail());
//        cookie.setPath("/");
//        Map body = new HashMap();
//        body.put("message", "login successfully");
//        body.put("status", "200");
//        responseEntity = new ResponseEntity<>(body,HttpStatus.OK);
//        response.addCookie(cookie);
////        response.setHeader("set-cookie","name=email;value=hasdasgdhagsd");
//        return responseEntity;
//    }

    @GetMapping("/cookie")
    public ResponseEntity<?> cookie (HttpServletResponse response, HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        if(cookies!=null){
            for (Cookie cookie: cookies) {
                System.out.println(cookie.getName());
            }
            System.out.println(request.getHeader("Cookie"));
            System.out.println(request.getHeaderNames());
        }
        Cookie cookie = new Cookie("sample","information");
        cookie.setMaxAge(60*10);
        Cookie cookie2 = new Cookie("test","information");
        cookie2.setMaxAge(60*10);
        response.addCookie(cookie);
        response.addCookie(cookie2);
        return new ResponseEntity<>("Check your cookies",HttpStatus.ACCEPTED);
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

    @GetMapping(value = "/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response){
        Cookie[] cookies = request.getCookies();
        if(cookies==null){
            return ResponseEntity.ok().body(null);
        }
        for (Cookie cookie:cookies) {
            if(cookie.getName().equals("email")){
                cookie.setMaxAge(-1);
                response.addCookie(cookie);
            }
        }
        return new ResponseEntity<>("Logout successfully",HttpStatus.OK);

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

    @PostMapping("/auth/signin")
    public ResponseEntity<?> signIn(@Valid @RequestBody LoginDTO login, HttpServletRequest request,HttpServletResponse response){
        ResponseEntity responseEntity = ResponseEntity.badRequest().body("Invalid Username or Password");
        Optional<User> byEmail = userRepository.findByEmail(login.getEmail());
        if(byEmail.isEmpty()){
            return responseEntity;
        }
        User dbUser = byEmail.get();
        if(!dbUser.getPassword().equals(login.getPassword())){
            return responseEntity;
        }
        HttpSession session = request.getSession();
        System.out.println(session);
        session.setAttribute("user",dbUser);

        return ResponseEntity.ok().body("Logged in");
    }

}
