package hospitalMngtSys.repositories.hospital;

import hospitalMngtSys.entities.LabEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LabRepository extends JpaRepository<LabEntity,Integer> {
    List<LabEntity> findByHospitalID(int hospitalID, Sort sort);
    LabEntity findByHospitalIDAndId(int hospitalID, int id);
    List<LabEntity> findByHospitalIDAndPatientID(int hospitalID,String patientID,Sort sort);
}
