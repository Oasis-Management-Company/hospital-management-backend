package hospitalMngtSys.entities;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "staff")
public class StaffEntity {
    @Id
    private String staffID;
    private int hospitalID;
    private String name;
    private String email;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dob;
    private String password;
    private String type;
    private String role;
    private String specialisation;
    private String phone;
    private String rank;
    private String gender;
    @Column(columnDefinition = "LONGBLOB")
    private byte[] logo;
    @Column(columnDefinition = "TEXT")
    private String onDuty;
    private boolean admin;
    private boolean deactivate;
    private boolean emailActivated;

    public StaffEntity() {
    }

    public StaffEntity(byte[] logo) {
        this.setLogo(logo);
    }


    public StaffEntity(String staffID, String name, String email, String password, String type, String specialisation, LocalDate dob, String role, String phone, String rank, String gender, byte[] logo, String onDuty) {
        this.setStaffID(staffID);
        this.setName(name);
        this.setEmail(email);
        this.setPassword(password);
        this.setType(type);
        this.setSpecialisation(specialisation);
        this.setDob(dob);
        this.setRole(role);
        this.setPhone(phone);
        this.setRank(rank);
        this.setGender(gender);
        this.setLogo(logo);
        this.setOnDuty(onDuty);
    }

    public String getStaffID() {
        return staffID;
    }

    public void setStaffID(String staffID) {
        this.staffID = staffID;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSpecialisation() {
        return specialisation;
    }

    public void setSpecialisation(String specialisation) {
        this.specialisation = specialisation;
    }

    public LocalDate getDob() {
        return dob;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getRank() {
        return rank;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public byte[] getLogo() {
        return logo;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    public String getOnDuty() {
        return onDuty;
    }

    public void setOnDuty(String onDuty) {
        this.onDuty = onDuty;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isDeactivate() {
        return deactivate;
    }

    public void setDeactivate(boolean deactivate) {
        this.deactivate = deactivate;
    }

    public void setEmailActivated(boolean emailActivated) {
        this.emailActivated = emailActivated;
    }

    public boolean isEmailActivated() {
        return emailActivated;
    }
}