package hospitalMngtSys.entities;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "drugs")
public class DrugsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int hospitalID;
    private String staffID;
    private String drugName;
    @Column(columnDefinition = "TEXT")
    private String description;
    private double quantity;
    private double cost;
    @Column(columnDefinition = "TEXT")
    private String uses;
    @CreationTimestamp
    private Date date;

    public DrugsEntity(){}

    public DrugsEntity(String staffID, String drugName, String description, String uses,double cost, double quantity, Date date){
        this.setStaffID(staffID);
        this.setDrugName(drugName);
        this.setUses(uses);
        this.setCost(cost);
        this.setDescription(description);
        this.setQuantity(quantity);
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

    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public String getUses() {
        return uses;
    }

    public void setUses(String uses) {
        this.uses = uses;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
