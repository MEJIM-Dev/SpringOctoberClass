package First.Spring.controller;

import First.Spring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class webcontroller {

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "/home")
    public String webHomepage() {
        return "homepage";
    }

//    @GetMapping(value = "/login")
//    public String login(){
//        return "login";
//    }
//
//    @PostMapping(value = "/login")
//    public String login(@ModelAttribute LoginDTO loginDTO, Model model, HttpServletResponse response){
//        System.out.println(loginDTO);
//        if(loginDTO.getEmail()==null || loginDTO.getPassword()==null){
//            return "login";
//        }
//        Optional<User> byEmail = userRepository.findByEmail(loginDTO.getEmail());
//        if(byEmail.isEmpty()){
//            return "login";
//        }
//        User user = byEmail.get();
//        if(!user.getPassword().equals(loginDTO.getPassword())){
//            return "login";
//        }
//        Cookie cookie = new Cookie("email",user.getEmail());
//        cookie.setMaxAge(60*3);
//        response.addCookie(cookie);
//        return "homepage";
//    }

}
