package hospitalMngtSys.entities;

import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.sql.Blob;

@Entity
@Table(name = "settings")
public class SettingsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int hospitalID;
    private String name;
    private String password;
    private String address;
    @Column(columnDefinition = "LONGBLOB")
    private byte[] logo;
    private String phone;
    private String email;
    private boolean emailActivated;

    public  SettingsEntity(){}

    public SettingsEntity(byte[] logo){
        this.setLogo(logo);
    }

    public SettingsEntity(String name, String password, String address, String phone,String email,byte[] logo){
        this.setName(name);
        this.setPassword(password);
        this.setAddress(address);
        this.setPhone(phone);
        this.setEmail(email);
        this.setLogo(logo);
    }

    public int getHospitalID() {
        return hospitalID;
    }

    public void setHospitalID(int hospitalID) {
        this.hospitalID = hospitalID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public byte[] getLogo() {
        return logo;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEmailActivated() {
        return emailActivated;
    }

    public void setEmailActivated(boolean emailActivated) {
        this.emailActivated = emailActivated;
    }
}
