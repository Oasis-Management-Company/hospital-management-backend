package hospitalMngtSys.entities;

import org.hibernate.type.UUIDCharType;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "appointments")
public class AppointmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int hospitalID;
    private String patientID;
    private String patientName;
    @Column(columnDefinition = "TEXT")
    private String complaint;
    private String doctorName;
    private String specialist;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;
    private String time;
    private int status;

    public AppointmentEntity(){}

    public AppointmentEntity(int id,String patientID,String patientName, String complaint,String doctorName,String specialist,LocalDate date,String time,int status){
        this.setId(id);
        this.setPatientID(patientID);
        this.setPatientName(patientName);
        this.setComplaint(complaint);
        this.setDoctorName(doctorName);
        this.setSpecialist(specialist);
        this.setDate(date);
        this.setTime(time);
        this.setStatus(status);
    }

    public int getId(){
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHospitalID() {
        return hospitalID;
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

    public String getComplaint() {
        return complaint;
    }

    public void setComplaint(String complaint) {
        this.complaint = complaint;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getSpecialist() {
        return specialist;
    }

    public void setSpecialist(String specialist) {
        this.specialist = specialist;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
