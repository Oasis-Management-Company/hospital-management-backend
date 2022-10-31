package hospitalMngtSys.repositories.patient;

import hospitalMngtSys.entities.AppointmentEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<AppointmentEntity, Integer> {
    AppointmentEntity findById(int id);
    List<AppointmentEntity> findByHospitalID(int id, Sort sort);
    List<AppointmentEntity> findByHospitalIDAndPatientID(int id, String patientId, Sort sort);
    @Query("SELECT appointment from AppointmentEntity appointment WHERE patientID = :id AND hospitalID = :hospitalID AND (status = :status1 OR status = :status2)")
    List<AppointmentEntity> findByStatusOrStatusAndPatientIDAndHospitalID(@Param("hospitalID") int hospitalID,
                                                                          @Param("status1") int status1,
                                                                          @Param("status2") int status2,
                                                                          @Param("id") String patientID, Sort sort);
    @Query("SELECT appointment from AppointmentEntity appointment WHERE doctorName = :name AND hospitalID = :hospitalID AND (status = :status1 OR status = :status2)")
    List<AppointmentEntity> findByStatusOrStatusAndDoctorNameAndHospitalID(@Param("hospitalID") int hospitalID,
                                                                           @Param("status1") int status1,
                                                                           @Param("status2") int status2,
                                                                           @Param("name") String name, Sort sort);
    List<AppointmentEntity> findByHospitalIDAndDate(int id,LocalDate date, Sort sort);
    List<AppointmentEntity> findByHospitalIDAndDateAndTimeAndDoctorName(int id,LocalDate date,String time,String name);
    List<AppointmentEntity> findByHospitalIDAndStatusAndDate(int id, int status,LocalDate date, Sort sort);
}