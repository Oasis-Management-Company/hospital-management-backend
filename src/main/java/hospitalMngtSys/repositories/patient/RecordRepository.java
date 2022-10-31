package hospitalMngtSys.repositories.patient;

import hospitalMngtSys.entities.RecordEntity;
import org.hibernate.type.UUIDCharType;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface RecordRepository extends JpaRepository<RecordEntity,Integer> {
    RecordEntity findById(int id);
    List<RecordEntity> findByHospitalID(int id,Sort sort);
    List<RecordEntity> findByHospitalIDAndPatientID(int hospitalID, String id, Sort sort);
    List<RecordEntity> findByHospitalIDAndDoctorID(int hospitalID,String id, Sort sort);
    List<RecordEntity> findByHospitalIDAndDate(int id, LocalDate date,Sort sort);


}
