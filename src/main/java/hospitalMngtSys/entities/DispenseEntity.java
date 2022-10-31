package hospitalMngtSys.entities;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "dispense")
public class DispenseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int hospitalID;
    private String doctorName;
    private String pharmacistID;
    private String  patientID;
    private int recordID;
    private String prescription;
    @CreationTimestamp
    private Date dispenseDate;

    public DispenseEntity(){}

    public DispenseEntity(String doctorName, String pharmacistID, String  patientID, int recordID, String prescription){
        this.setPatientID(patientID);
        this.setPharmacistID(pharmacistID);
        this.setDoctorName(doctorName);
        this.setRecordID(recordID);
        this.setPrescription(prescription);
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

    public String  getPatientID() {
        return patientID;
    }

    public void setPatientID(String  patientID) {
        this.patientID = patientID;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getPharmacistID() {
        return pharmacistID;
    }

    public void setPharmacistID(String pharmacistID) {
        this.pharmacistID = pharmacistID;
    }

    public int getRecordID() {
        return recordID;
    }

    public void setRecordID(int recordID) {
        this.recordID = recordID;
    }

    public Date getDispenseDate() {
        return dispenseDate;
    }

    public String getPrescription() {
        return prescription;
    }

    public void setPrescription(String prescription) {
        this.prescription = prescription;
    }
}
