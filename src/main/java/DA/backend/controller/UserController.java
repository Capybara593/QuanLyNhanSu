package DA.backend.controller;

import DA.backend.entity.User;
import DA.backend.service.EmailService;
import DA.backend.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.context.support.DefaultMessageSourceResolvable;


import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.apache.catalina.manager.StatusTransformer.writeHeader;

@RestController
@RequestMapping("api/user")
@CrossOrigin(origins = "*")
public class UserController {
    @Autowired
   private UserService userService;
    @Autowired
    private EmailService emailService;

    @PostMapping("/add")
    public ResponseEntity<?> addUser(@RequestBody @Valid User user, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }

        if (userService.addUser(user)) {
            emailService.sendEmail(user.getEmail(),
                    "ACCOUNT",
                    "ID: " + user.getId() + " Password: " + user.getId());
            return ResponseEntity.ok("OK");
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email đã tồn tại");
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

    // Endpoint để xuất dữ liệu người dùng ra file Excel
    @GetMapping("/export/excel")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        // Thiết lập kiểu nội dung cho file Excel
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        // Tạo tên file với định dạng thời gian hiện tại để tránh trùng tên
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());
        String fileName = "users_" + currentDateTime + ".xlsx";

        // Thiết lập Content-Disposition để trình duyệt nhận diện tên file khi tải về
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

        // Lấy danh sách người dùng từ cơ sở dữ liệu và tạo file Excel
        List<User> listUsers = userService.listUser();
        UserExcelExporter excelExporter = new UserExcelExporter(listUsers);

        // Gọi phương thức export để ghi file Excel vào output stream của response
        excelExporter.export(response);
    }
}
