package DA.backend.service;

import DA.backend.entity.IdGenerator;
import DA.backend.entity.Role;
import DA.backend.entity.User;
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
            user1.setPosition(user.getPosition());
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
    public User checkUser(String id){
        Optional<User> optionalUser = userRepository.findById(id);
        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            return user;
        }
        return  null;
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

}
