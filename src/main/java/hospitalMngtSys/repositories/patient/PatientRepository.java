package hospitalMngtSys.repositories.patient;

import hospitalMngtSys.entities.PatientEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<PatientEntity, String> {
    PatientEntity findByHospitalIDAndID(int hospitalID, String id);
    List<PatientEntity> findByHospitalID(int id, Sort sort);
    PatientEntity findByHospitalIDAndFnameAndLnameAndDob(int hospitalID, String fname, String lname, LocalDate date);
    PatientEntity findByHospitalIDAndEmail(int id, String email);
}
