package DA.backend.controller;

import DA.backend.entity.User;
import DA.backend.service.EmailService;
import DA.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/user")
public class UserController {
    @Autowired
   private UserService userService;
    @Autowired
    private EmailService emailService;

    @PostMapping("/add")
    public String addUser(@RequestBody User user){

            if(userService.addUser(user)){
                emailService.sendEmail(user.getEmail(),
                        "ACCOUNT",
                        "ID: " + user.getId()
                                + "Password: " + user.getId());
                return "OK";
            } else {
                return "Email da ton tai";
            }
    }
    @PostMapping("/login")
    public String login(@RequestParam String id,@RequestParam String password){
        if(userService.login(id, password)){
            return "Login Success";
        }else {
            return "Login Fail";
        }
    }
    @PutMapping("/update")
    public String update(@RequestBody User user){
        try {
            userService.updateUser(user);
            return "update success";
        }catch (Exception ex){
            return "update faile";
        }
    }
    @PostMapping("/delete")
    public String deleteUser(@RequestParam String id){
        try {
            userService.deleteUser(id);
            return "OK";
        }catch (Exception ex){
            return "Faile";
        }
    }
    @GetMapping("/list")
    public List<User> userList(){
        return userService.listUser();
    }
    @GetMapping("/listUserDelete")
    public List<User> userListDelete(){
        return userService.listUserDelete();
    }
    @PostMapping("/sendCode")
    public void sendCode(@RequestParam String id){
        User user = userService.checkUser(id);
        emailService.sendCode(id,user.getEmail(),"SEND CODE");
    }
    @PostMapping("/checkSendCode")
    public boolean checkSendCode(@RequestParam String code){
        if (userService.checkSendCode(code)){
            return true;
        }
        return  false;
    }
    @PostMapping("/resetPassword")
    public String resetPassword(@RequestParam String id, String passwordNew, String passwordNew1){
        try {
            userService.checkPassword(passwordNew,passwordNew1);
            userService.resetPassword(id,passwordNew,passwordNew1);
            return "OK";
        }catch (Exception ex){
            return "Failed";
        }

    }
    @GetMapping("/profile")
    public User profileUser(@RequestParam String id){
        return userService.checkUser(id);
    }


}
