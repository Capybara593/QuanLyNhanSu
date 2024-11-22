package DA.backend.controller;

import DA.backend.entity.User;
import DA.backend.entity.UserDTO;
import DA.backend.service.EmailService;
import DA.backend.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.web.multipart.MultipartFile;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import DA.backend.entity.Role;
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
//        if (result.hasErrors()) {
//            List<String> errors = result.getFieldErrors()
//                    .stream()
//                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
//                    .collect(Collectors.toList());
//            return ResponseEntity.badRequest().body(errors);
//        }

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
    public ResponseEntity<?> login(@RequestParam String id, @RequestParam String password) {
        Optional<User> optionalUser = userService.findById(id);

        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid ID or password");
        }

        User user = optionalUser.get();

        // Kiểm tra mật khẩu và trạng thái xóa
        if (!user.isDelete() && userService.checkPassword(password, user.getPassword())) {
            // Lấy vai trò của người dùng
            String role = user.getRoles().stream()
                    .findFirst()
                    .map(Role::getName)
                    .orElse("EMPLOYEE");

            // Trả về JSON với trạng thái và vai trò
            Map<String, String> response = new HashMap<>();
            response.put("status", "Login Success");
            response.put("role", role);

            return ResponseEntity.ok(response);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid ID or password");
    }
    @PutMapping("/update")
    public ResponseEntity<String> updateUser(
            @RequestBody UserDTO userDTO,
            @RequestParam(value = "image", required = false) MultipartFile image
    ) {
        try {
            // Kiểm tra ID của người dùng
            if (userDTO.getId() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User ID is missing");
            }
            User user = userService.checkUser(userDTO.getId());
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }

            // Cập nhật thông tin từ DTO sang Entity
            user = userService.convertToEntity(userDTO);

            // Kiểm tra xem `image` có null hay không
            if (image != null && !image.isEmpty()) {
                // Thực hiện xử lý ảnh nếu tồn tại
                userService.updateUser(user, image);
            } else {
                // Nếu không có ảnh, chỉ cập nhật thông tin
                userService.updateUserWithoutImage(user);
            }

            return ResponseEntity.ok("Update success");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update failed: " + e.getMessage());
        }
    }


    // API lấy ảnh đại diện
    @GetMapping("/{id}/image")
    public ResponseEntity<String> getAvatar(@PathVariable String id) {
        Optional<String> optionalImage = userService.getUserImage(id);
        if (optionalImage.isPresent()) {
            return ResponseEntity.ok(optionalImage.get());
        } else {
            return ResponseEntity.notFound().build();
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
//    @GetMapping("/list")
//    public List<User> userList(){
//        return userService.listUser();
//    }
@GetMapping("/list")
public ResponseEntity<?> userList() {
    List<Map<String, Object>> usersWithRoles = userService.listUser().stream()
            .map(user -> {
                Map<String, Object> userMap = new HashMap<>();
                userMap.put("id", user.getId());
                userMap.put("name", user.getName());
                userMap.put("email", user.getEmail());
                userMap.put("avatar", user.getImage());
                userMap.put("role", user.getRoles().stream()
                        .findFirst()
                        .map(Role::getName) // Lấy tên role đầu tiên
                        .orElse("N/A")); // Nếu không có role, trả về "N/A"
                return userMap;
            })
            .collect(Collectors.toList());
    return ResponseEntity.ok(usersWithRoles);
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

    @PostMapping("/add/images")
    public ResponseEntity<?> addImage(@RequestParam String id, @RequestParam("image")MultipartFile image) throws  IOException{
       if(userService.addImages(id,image)){
           return  ResponseEntity.ok("OK");
       }
       return ResponseEntity.status(HttpStatus.CONFLICT).body("Failed");
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
//    @GetMapping("/profile")
//    public ResponseEntity<?> profileUser(@RequestParam String id) {
//        User user = userService.checkUser(id);
//        if (user == null) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
//        }
//        UserDTO userDTO = userService.convertToDTO(user);
//        return ResponseEntity.ok(userDTO);
//    }
@GetMapping("/profile")
public ResponseEntity<?> profileUser(@RequestParam String id) {
    User user = userService.checkUser(id);
    if (user == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }

    // Tạo DTO
    UserDTO userDTO = userService.convertToDTO(user);

    // Lấy vai trò (giả sử User có danh sách roles)
    String role = user.getRoles().stream()
            .findFirst()
            .map(Role::getName)
            .orElse("N/A"); // Giá trị mặc định nếu không có role
    userDTO.setRole(role);

    return ResponseEntity.ok(userDTO);
}
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/updateRole")
    public ResponseEntity<?> updateUserRole(@RequestParam String userId, @RequestParam String newRole) {
        Optional<User> optionalUser = userService.findById(userId);

        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        User user = optionalUser.get();

        try {
            // Cập nhật vai trò
            boolean updated = userService.updateUserRole(user, newRole);

            if (updated) {
                return ResponseEntity.ok("Role updated successfully");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Role update failed. Role not found.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating role");
        }
    }
    @GetMapping("/profile/image")
    public ResponseEntity<byte[]> getUserImage(@RequestParam String id) throws Exception {
        User user = userService.checkUser(id);
        if (user == null || user.getImage() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        // Giải mã chuỗi Base64 thành byte array
        byte[] imageBytes = Base64.getDecoder().decode(user.getImage());

        // Đoán loại MIME từ dữ liệu byte array
        String mimeType = URLConnection.guessContentTypeFromStream(new ByteArrayInputStream(imageBytes));
        if (mimeType == null) {
            mimeType = MediaType.APPLICATION_OCTET_STREAM_VALUE; // Dùng giá trị mặc định nếu không xác định được loại MIME
        }

        // Thiết lập header với loại MIME tương ứng
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(mimeType));
        headers.setContentLength(imageBytes.length);

        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
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
