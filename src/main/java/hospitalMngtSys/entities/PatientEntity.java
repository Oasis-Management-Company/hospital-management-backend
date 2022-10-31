package hospitalMngtSys.entities;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.type.UUIDCharType;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "patients")
public class PatientEntity {
    @Id
    @Column(columnDefinition = "VARCHAR(20)", updatable = false, nullable = false)
    private String ID;
    private int hospitalID;
    private String fname;
    private String lname;
    private String email;
    private String password;
    private int age;
    @Column(columnDefinition = "LONGBLOB")
    private byte[] logo;
    @Column(columnDefinition = "TEXT")
    private String address;
    private String phone;
    private String gender;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dob;
    private String id_type;
    private String id_no;
    private float height;
    private float weight;
    private String bloodGroup;
    private String bloodType;
    private String bloodPressure;
    @Column(columnDefinition = "TEXT")
    private String allergies;
    @CreationTimestamp
    private Date createdAt;
    private boolean emailActivated;
    private boolean deactivate;

    public PatientEntity(){}

    public PatientEntity(byte[] logo){
        this.setLogo(logo);
    }

    public PatientEntity(String fname, String lname,String email,int age, String password,String address,String phone,String gender,LocalDate dob,String id_type,String id_no,String allergies,String bloodPressure,String bloodGroup,String bloodType,float weight,float height,byte[] logo){
        this.setFname(fname);
        this.setLname(lname);
        this.setEmail(email);
        this.setAge(age);
        this.setPassword(password);
        this.setAddress(address);
        this.setPhone(phone);
        this.setGender(gender);
        this.setDob(dob);
        this.setId_no(id_no);
        this.setId_type(id_type);
        this.setBloodGroup(bloodGroup);
        this.setBloodType(bloodType);
        this.setBloodPressure(bloodPressure);
        this.setHeight(height);
        this.setWeight(weight);
        this.setLogo(logo);
        this.setAllergies(allergies);
    }

    public String getID(){
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public int getHospitalID() {
        return hospitalID;
    }

    public void setHospitalID(int hospitalID) {
        this.hospitalID = hospitalID;
    }

    public String getFname(){
        return fname;
    }

    public void setFname(String fname){
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getId_no() {
        return id_no;
    }

    public void setId_no(String id_no) {
        this.id_no = id_no;
    }

    public String getId_type() {
        return id_type;
    }

    public void setId_type(String id_type) {
        this.id_type = id_type;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public String getBloodPressure() {
        return bloodPressure;
    }

    public void setBloodPressure(String bloodPressure) {
        this.bloodPressure = bloodPressure;
    }

    public byte[] getLogo() {
        return logo;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    public String getAllergies() {
        return allergies;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public boolean isEmailActivated() {
        return emailActivated;
    }

    public void setEmailActivated(boolean emailActivated) {
        this.emailActivated = emailActivated;
    }

    public boolean isDeactivate() {
        return deactivate;
    }

    public void setDeactivate(boolean deactivate) {
        this.deactivate = deactivate;
    }
}