package hospitalMngtSys.repositories.hospital;

import hospitalMngtSys.entities.VisitEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VisitRepository extends JpaRepository<VisitEntity,Integer> {
    VisitEntity findByHospitalIDAndId(int hospitalID, int id);
    List<VisitEntity> findByHospitalID(int id,Sort sort);
    @Query("SELECT visit from VisitEntity visit WHERE doctorName = :name AND hospitalID = :hospitalID AND date = :date AND (status = :status1 OR status = :status2)")
    List<VisitEntity> findByHospitalIDAndDoctorNameAndDateAndStatusOrStatus(@Param("hospitalID") int hospitalID,
                                                                            @Param("name") String name,
                                                                            @Param("date") LocalDate date,
                                                                            @Param("status1") int status1,
                                                                            @Param("status2") int status2, Sort sort);
    List<VisitEntity> findByHospitalIDAndDate(int id,LocalDate date,Sort sort);
    List<VisitEntity> findByHospitalIDAndDateAndStatus(int id, LocalDate date,int status,Sort sort);
}
