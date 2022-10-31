package hospitalMngtSys.services.patient;

import hospitalMngtSys.entities.PatientEntity;
import hospitalMngtSys.entities.TestsEntity;
import hospitalMngtSys.repositories.patient.AppointmentRepository;
import hospitalMngtSys.repositories.patient.PatientRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
public class PatientService {

    @Autowired
    PatientRepository patientRepository;

    public List<PatientEntity> getAllPatients(Sort sort) {
        return patientRepository.findAll(sort);
    }

    public String getPatientNameById(String id,int hospitalID){
        PatientEntity patient = patientRepository.findByHospitalIDAndID(hospitalID,id);
        String fname = patient.getFname();
        String lname = patient.getLname();
        String name = fname + " " + lname;
        return name;
    }

    public String hashPassword(String password){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(password);
        return encodedPassword;
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

    public boolean emailExists(int id, String email){
        PatientEntity patient = patientRepository.findByHospitalIDAndEmail(id,email);
        if(patient != null){
            return true;
        }else{
            return false;
        }
    }

    public Sort sortByDateAndTime(){
        Sort sortByDateAndTime = Sort.by("date").descending().and(Sort.by("time")).and(Sort.by("status"));
        return sortByDateAndTime;
    }
//
//    public void getPatientAndRecords(){
//        TypedQuery<AppointmentEntity> query = entityManager.createQuery("SELECT * FROM appointments INNER JOIN patients ON patients.ID = records.patientID", AppointmentEntity.class);
//        List<AppointmentEntity> resultList = query.getResultList();
//    }
}