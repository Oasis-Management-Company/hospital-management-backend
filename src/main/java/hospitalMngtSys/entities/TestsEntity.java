package hospitalMngtSys.entities;

import javax.persistence.*;

@Entity
@Table(name = "testResults")
public class TestsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int hospitalID;
    private String staffID;
    private String  patientID;
    private String results;
    private String description;

    public TestsEntity(){}

    public TestsEntity(String staffID, String  patientID, String results, String description){
        this.setStaffID(staffID);
        this.setPatientID(patientID);
        this.setResults(results);
        this.setDescription(description);
    }

    public int getId() {
        return id;
    }

    public String getStaffID() {
        return staffID;
    }

    public int getHospitalID() {
        return hospitalID;
    }

    public void setStaffID(String staffID) {
        this.staffID = staffID;
    }

    public String  getPatientID() {
        return patientID;
    }

    public void setPatientID(String  patientID) {
        this.patientID = patientID;
    }

    public String getResults() {
        return results;
    }

    public void setResults(String results) {
        this.results = results;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}