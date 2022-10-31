package hospitalMngtSys.entities;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "visit_records")
public class RecordEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;
        private int hospitalID;
        private String  patientID;
        private String patientName;
        private String doctorName;
        private String specialist;
        private String doctorID;
        @Column(columnDefinition = "TEXT")
        private String observation;
        private float costOfTreatment;
        @Column(columnDefinition = "TEXT")
        private String diagnosis;
        @Column(columnDefinition = "TEXT")
        private String prescriptions;
        private int paid;
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        private LocalDate date;

        public RecordEntity(){}

        public RecordEntity(String  patientID, String patientName, String doctorID, String doctorName,String specialist, String observation,String prescriptions,float costOfTreatment, String diagnosis, LocalDate date, int paid){
            this.setPatientID(patientID);
            this.setPatientName(patientName);
            this.setDoctorID(doctorID);
            this.setDoctorName(doctorName);
            this.setSpecialist(specialist);
            this.setPrescriptions(prescriptions);
            this.setObservation(observation);
            this.setDiagnosis(diagnosis);
            this.setCostOfTreatment(costOfTreatment);
            this.setDate(date);
            this.setPaid(paid);
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

        public String getPatientName() {
            return patientName;
        }

        public void setPatientName(String patientName) {
            this.patientName = patientName;
        }

        public String getDoctorID() {
            return doctorID;
        }

        public void setDoctorID(String doctorID) {
            this.doctorID = doctorID;
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

    public String getPrescriptions() {
            return prescriptions;
        }

        public void setPrescriptions(String prescriptions) {
            this.prescriptions = prescriptions;
        }

        public String getObservation() {
            return observation;
        }

        public void setObservation(String observation) {
            this.observation = observation;
        }

    public String getDiagnosis() {
            return diagnosis;
        }

        public void setDiagnosis(String diagnosis) {
            this.diagnosis = diagnosis;
        }

        public float getCostOfTreatment() {
            return costOfTreatment;
        }

        public void setCostOfTreatment(float costOfTreatment) {
            this.costOfTreatment = costOfTreatment;
        }

        public LocalDate getDate() {
            return date;
        }

        public void setDate(LocalDate date) {
            this.date = date;
        }

        public int getPaid() {
            return paid;
        }

        public void setPaid(int paid) {
            this.paid = paid;
        }
}


