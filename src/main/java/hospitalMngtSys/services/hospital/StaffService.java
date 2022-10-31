package hospitalMngtSys.services.hospital;

import hospitalMngtSys.entities.AppointmentEntity;
import hospitalMngtSys.entities.DrugsEntity;
import hospitalMngtSys.entities.PatientEntity;
import hospitalMngtSys.entities.StaffEntity;
import hospitalMngtSys.repositories.hospital.DrugsRepository;
import hospitalMngtSys.repositories.hospital.StaffRepository;
import hospitalMngtSys.repositories.patient.PatientRepository;
import jdk.swing.interop.SwingInterOpUtils;
import org.hibernate.type.UUIDCharType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class StaffService {

    @Autowired
    StaffRepository staffRepository;
    @Autowired
    PatientRepository patientRepository;
    @Autowired
    DrugsRepository drugsRepository;


    public Sort sortByName(){
        Sort sortByName = Sort.by("name");
        return sortByName;
    }

    public Sort sortByDate(){
        Sort sortByDate = Sort.by("date").descending();
        return sortByDate;
    }

    public Sort sortByDateAndTimeAndStatus(){
        Sort sortByDateAndTimeAndStatus = Sort.by("date").descending().and(Sort.by("time")).and(Sort.by("status"));
        return sortByDateAndTimeAndStatus;
    }

    public Sort sortByTimeAndStatus(){
        Sort sortByTimeAndStatus = Sort.by("status").and(Sort.by("time"));
        return sortByTimeAndStatus;
    }

    public Sort sortByDateAndTime(){
        Sort sortByDateAndTime = Sort.by("date").descending().and(Sort.by("time"));
        return sortByDateAndTime;
    }

    public Sort sortByDateAndPaid(){
        Sort sortByDateAndPaid = Sort.by("date").descending().and(Sort.by("paid"));
        return sortByDateAndPaid;
    }

    public Sort sortByTime(){
        Sort sortbyTime = Sort.by("time");
        return sortbyTime;
    }

    public String getStaffNameById(String id,int hospitalID){
        StaffEntity staff = staffRepository.findByHospitalIDAndStaffID(hospitalID,id);
        String name = staff.getName();
        return name;
    }

    public String getPatientNameById(String id,int hospitalID){
        PatientEntity patient = patientRepository.findByHospitalIDAndID(hospitalID, id);
        String fname = patient.getFname();
        String lname = patient.getLname();
        String name = fname + " " + lname;
        return name;
    }

    public Object[] prescription(String drug, int id){
        String [] split = drug.split(",");
        Object[] objects = new Object[split.length*2];
        int d = 0;
        int e=0;
        for(int i=0;i<objects.length;i++){
            if(i%2==0){
                objects[i] = split[d];
                d++;
            }else{
                objects[i] = getDrugsCost(split[e], id);
                e++;
            }
        }
        return objects;
    }

    public boolean emailExists(String email){
        StaffEntity staff = staffRepository.findByEmail(email);
        if(staff != null){
            return true;
        }else{
            return false;
        }
    }

    public double getDrugsCost(String name,int id){
        DrugsEntity drugsEntity = drugsRepository.findByHospitalIDAndDrugName(id,name);
        if(drugsEntity == null){
            return 0;
        }
        return drugsEntity.getCost();
    }

    public boolean unhashPassword(String password, String encodedPassword){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        boolean isMatch = passwordEncoder.matches(password,encodedPassword);
        if(isMatch){
            return true;
        }else{
            return false;
        }
    }

//    public boolean findByOnDuty(String staffID,int id){
//        LocalDate localDate = LocalDate.now();
//        DayOfWeek day = localDate.getDayOfWeek();
//        StaffEntity doctor = staffRepository.findByHospitalIDAndStaffID(id,staffID);
//        String onDuty = doctor.getOnDuty();
//        onDuty.contains(day.toString());
//        return true;
//    }

//    public List<Object> findByDay(List <StaffEntity> staff)throws NullPointerException{
//        int length = staff.toArray().length;
//        LocalDate localDate = LocalDate.now();
//        String day = localDate.getDayOfWeek().toString();
//        String[] staffs = new String[length];
//        for(int i =0;i<length;i++){
//            StaffEntity staff1 = staff.get(i);
//            String onDuty = staff1.getOnDuty().toUpperCase();
//            if(onDuty.contains(day)){
//                staffs[i] = staff1.toString();
//            }
//        }
//        System.out.println(List.of(staffs));
//        return List.of(staffs);
//    }

    public List<StaffEntity> getAllDoctors(int id){
        List<StaffEntity> doctor = staffRepository.findByHospitalIDAndRole(id,"doctor",sortByName());
        return doctor;
    }

    public List<StaffEntity> getAllPharmacists(int id){
        List<StaffEntity> pharmacist = staffRepository.findByHospitalIDAndRole(id,"pharmacist",sortByName());
        return pharmacist;
    }

    public List<StaffEntity> getAllLab(int id){
        List<StaffEntity> lab = staffRepository.findByHospitalIDAndRole(id,"lab",sortByName());
        return lab;
    }

    public List<StaffEntity> getAllNurse(int id){
        List<StaffEntity> nurse = staffRepository.findByHospitalIDAndRole(id,"nurse",sortByName());
        return nurse;
    }
}
