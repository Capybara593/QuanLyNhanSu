package DA.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.springframework.boot.autoconfigure.domain.EntityScan;


import java.util.Date;

@Entity
@Table(name = "user")
public class User {
    @Id
    private String id;
    private String name;
    private Date birthDay;
    private String nationality;
    private String homeTown;
    private String address;
    private String email;
    private String phoneNumber;
    private String image;
    private String sex;
    private String password;
    private boolean isDelete = false;

    public User(String id, String name, Date birthDay, String nationality, String homeTown, String address, String email, String phoneNumber, String image, String sex, String password, boolean isDelete) {
        this.id = id;
        this.name = name;
        this.birthDay = birthDay;
        this.nationality = nationality;
        this.homeTown = homeTown;
        this.address = address;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.image = image;
        this.sex = sex;
        this.password = password;
        this.isDelete = isDelete;
    }


    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
 public User() {
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getHomeTown() {
        return homeTown;
    }

    public void setHomeTown(String homeTown) {
        this.homeTown = homeTown;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
