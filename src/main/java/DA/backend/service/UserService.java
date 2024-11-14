package DA.backend.service;

import DA.backend.entity.IdGenerator;
import DA.backend.entity.Role;
import DA.backend.entity.User;
import DA.backend.entity.UserDTO;
import DA.backend.repository.RoleRepository;
import DA.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
   @Autowired
   private  EmailService emailService;
    @Autowired
    private RoleRepository roleRepository;
    public static final Random random = new Random();
    private final BCryptPasswordEncoder paswordHash = new BCryptPasswordEncoder();

    public boolean addUser(User user){
        Optional<User> optionalUser = userRepository.findByEmail(user.getEmail());
        if(optionalUser.isPresent()){
            return false;
        }

        // Gán vai trò mặc định "EMPLOYEE"
        Role employeeRole = roleRepository.findByName("EMPLOYEE")
                .orElseThrow(() -> new RuntimeException("Error: Role EMPLOYEE not found."));

        Set<Role> roles = new HashSet<>();
        roles.add(employeeRole);
        user.setRoles(roles);

        String userId = IdGenerator.generateUniqueId();
        boolean status = false;
        while (!status){

            String encryptedPassword = paswordHash.encode(userId);
            user.setPassword(encryptedPassword);
            user.setId(userId);
            try {
                userRepository.save(user);
                status = true;
            }catch (DataIntegrityViolationException ex){
                status = false;
            }
        }
        return true;
    }

    public boolean checkPassword(String password,String encodedPassword){
        return paswordHash.matches(password,encodedPassword);
    }

    public boolean login(String id, String password){
        Optional<User> optionalUser = userRepository.findById(id);
        if(optionalUser.isPresent()){
            if(!optionalUser.get().isDelete()){
                return checkPassword(password,optionalUser.get().getPassword());
            }
        }
        return false;

    }
    public void deleteUser(String id){
        Optional<User> optionalUser = userRepository.findById(id);
        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            user.setDelete(true);
            userRepository.save(user);
        }
    }
    public void updateUser(User user) {
        Optional<User> optionalUser = userRepository.findById(user.getId());
        if (optionalUser.isPresent()) {
            User user1 = optionalUser.get();

            user1.setName(user.getName());
            user1.setAddress(user.getAddress());
            user1.setEmail(user.getEmail());
            user1.setSex(user.getSex());
            user1.setBirthDay(user.getBirthDay());
            user1.setHomeTown(user.getHomeTown());
            user1.setNationality(user.getNationality());
            user1.setPhoneNumber(user.getPhoneNumber());

            // Xử lý ảnh
            if (user.getImage() != null) {
                try {
                    // Kiểm tra xem chuỗi Base64 có hợp lệ không
                    Base64.getDecoder().decode(user.getImage());
                    user1.setImage(user.getImage()); // Lưu chuỗi Base64 vào cơ sở dữ liệu
                } catch (IllegalArgumentException e) {
                    System.err.println("Lỗi giải mã Base64: " + e.getMessage());
                    throw new RuntimeException("Invalid image format", e);
                }
            }

            userRepository.save(user1);
        } else {
            throw new RuntimeException("User not found with ID: " + user.getId());
        }
    }


    public Optional<String> getUserImage(String userId) {
        Optional<User> optionalUser = userRepository.findById(userId);

        return optionalUser.map(User::getImage); // Trả về chuỗi Base64 từ DB
    }

    public List<User> listUser(){
        return userRepository.findAll()
                .stream()
                .filter(user -> !user.isDelete())
                .collect(Collectors.toList());
    }
    public List<User> listUserDelete(){
        return userRepository.findAll()
                .stream()
                .filter(User::isDelete)
                .collect(Collectors.toList());
    }
    public User checkUser(String id) {
        System.out.println("Checking user with ID: " + id);
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            System.out.println("User found: " + optionalUser.get());
            return optionalUser.get();
        } else {
            System.out.println("No user found for ID: " + id);
            return null;
        }
    }

    public boolean checkSendCode( String sendCode){
        if(sendCode.equals(emailService.Code)){
           return true;
        }
           return false;
    }

    public void resetPassword(String id,String passwordNew, String passwordNew1){
        if(passwordNew.equals(passwordNew1)){
            Optional<User> optionalUser = userRepository.findById(id);
            String encryptedPassword = paswordHash.encode(passwordNew);
            User user = optionalUser.get();
          user.setPassword(encryptedPassword);
            userRepository.save(user);
        }
    }

    //------------------
    public UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setBirthDay(user.getBirthDay());
        dto.setNationality(user.getNationality());
        dto.setHomeTown(user.getHomeTown());
        dto.setAddress(user.getAddress());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setSex(user.getSex());

        dto.setImage(user.getImage());

        return dto;
    }

    public User convertToEntity(UserDTO dto) {
        User user = new User();
        // Gán các thuộc tính khác
        user.setId(dto.getId());
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setSex(dto.getSex());
        user.setNationality(dto.getNationality());
        user.setHomeTown(dto.getHomeTown());
        user.setBirthDay(dto.getBirthDay());

        user.setImage(dto.getImage());

        return user;
    }

}
