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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    public void updateUser(User user){
        Optional<User> optionalUser = userRepository.findById(user.getId());
        if(optionalUser.isPresent()){
            User user1 =  optionalUser.get();
            user1.setName(user.getName());
            user1.setAddress(user.getAddress());
            user1.setEmail(user.getEmail());
            user1.setImage(user.getImage());
            user1.setSex(user.getSex());
            user1.setAddress(user.getAddress());
            user1.setBirthDay(user.getBirthDay());
            user1.setHomeTown(user.getHomeTown());
            user1.setNationality(user.getNationality());
            user1.setPhoneNumber(user.getPhoneNumber());
            userRepository.save(user1);
        }
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
        return userRepository.findById(id).orElse(null);
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
    public UserDTO convertToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setBirthDay(user.getBirthDay());
        userDTO.setNationality(user.getNationality());
        userDTO.setHomeTown(user.getHomeTown());
        userDTO.setAddress(user.getAddress());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        userDTO.setSex(user.getSex());
        userDTO.setImage(user.getImage());
        return userDTO;
    }
    public Optional<String> getUserImage(String userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return Optional.ofNullable(user.getImage());
        }
        return Optional.empty();
    }

    public User convertToEntity(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setName(userDTO.getName());
        user.setBirthDay(userDTO.getBirthDay());
        user.setNationality(userDTO.getNationality());
        user.setHomeTown(userDTO.getHomeTown());
        user.setAddress(userDTO.getAddress());
        user.setEmail(userDTO.getEmail());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setSex(userDTO.getSex());
        user.setImage(userDTO.getImage());
        return user;
    }

    public boolean addImages(String id, MultipartFile image) throws IOException {
        if (image == null || image.isEmpty()) {
            return false;
        }
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            String encodedImage = Base64.getEncoder().encodeToString(image.getBytes());

            user.setImage(encodedImage);
            userRepository.save(user);
            return true;
        }
        return false;
    }

}
