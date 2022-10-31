package hospitalMngtSys.repositories.hospital;

import hospitalMngtSys.entities.StaffEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StaffRepository extends JpaRepository<StaffEntity,String> {
    StaffEntity findByHospitalIDAndStaffID(int hospitalID,String id);
    StaffEntity findByHospitalIDAndEmail(int hospitalID,String email);
    List<StaffEntity> findByHospitalID(int id,Sort sort);
    List<StaffEntity> findByHospitalIDAndRole(int id,String role, Sort sort);
    StaffEntity findByEmail(String email);
    List<StaffEntity> findByHospitalIDAndRole(int id,String role);
    List<StaffEntity> findByHospitalIDAndType(int id,String type);
    List<StaffEntity> findByHospitalIDAndRoleAndSpecialisation(int id,String role, String specialisation);
}
