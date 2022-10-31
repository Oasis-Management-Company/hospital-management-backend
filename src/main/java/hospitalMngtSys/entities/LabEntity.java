package hospitalMngtSys.entities;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "lab")
public class LabEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int hospitalID;
    private String staffID;
    private String patientID;
    private String patientName;
    private String doctorName;
    @Column(columnDefinition = "LONGBLOB")
    private byte[] testResult;
    private String uploadName;
    private String test;
    private LocalDate date;

    public LabEntity(){}

    public LabEntity(String staffID,String patientID,String patientName,String doctorName,byte[] testResult, String test, LocalDate date){
        this.setStaffID(staffID);
        this.setPatientID(patientID);
        this.setPatientName(patientName);
        this.setDoctorName(doctorName);
        this.setTestResult(testResult);
        this.setTest(test);
        this.setDate(date);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHospitalID() {
        return hospitalID;
    }

    public String getStaffID() {
        return staffID;
    }

    public void setStaffID(String staffID) {
        this.staffID = staffID;
    }

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getUploadName() {
        return uploadName;
    }

    public void setUploadName(String uploadName) {
        this.uploadName = uploadName;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    public byte[] getTestResult() {
        return testResult;
    }

    public void setTestResult(byte[] testResult) {
        this.testResult = testResult;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
