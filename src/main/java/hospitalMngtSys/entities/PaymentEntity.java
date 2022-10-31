package hospitalMngtSys.entities;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "payments")
public class PaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int hospitalID;
    private int recordID;
    private String  patientID;
    private String patientName;
    private String pharmacistID;
    private String staffName;
    @Column(columnDefinition = "TEXT")
    private String referenceID;
    private String paymentType;
    private int status;
    @CreationTimestamp
    private LocalDate date;

    public PaymentEntity(){}

    public PaymentEntity(int recordID, String  patientID, String pharmacistID, String paymentType, int status){
        this.setRecordID(recordID);
        this.setPharmacistID(pharmacistID);
        this.setPatientID(patientID);
        this.setPaymentType(paymentType);
        this.setStatus(status);
    }

    public int getId() {
        return id;
    }

    public String  getPatientID() {
        return patientID;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public int getHospitalID() {
        return hospitalID;
    }

    public void setPatientID(String  patientID) {
        this.patientID = patientID;
    }

    public int getRecordID() {
        return recordID;
    }

    public void setRecordID(int recordID) {
        this.recordID = recordID;
    }

    public String getPharmacistID() {
        return pharmacistID;
    }

    public void setPharmacistID(String pharmacistID) {
        this.pharmacistID = pharmacistID;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getReferenceID() {
        return referenceID;
    }

    public void setReferenceID(String referenceID) {
        this.referenceID = referenceID;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
