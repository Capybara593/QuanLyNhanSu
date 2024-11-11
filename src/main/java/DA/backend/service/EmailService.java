package DA.backend.service;

import DA.backend.entity.IdGenerator;
import DA.backend.entity.User;
import DA.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmailService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JavaMailSender mailSender;

    public String Code;

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }
    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }
    public void sendCode(String id, String email, String subject){
        Optional<User> optionalUser = userRepository.findById(id);
        if(optionalUser.isPresent()){
            Code = IdGenerator.generateUniqueId();
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject(subject);
            message.setText(Code);
            mailSender.send(message);
        }

    }
}