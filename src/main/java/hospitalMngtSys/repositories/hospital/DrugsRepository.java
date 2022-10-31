package hospitalMngtSys.repositories.hospital;

import hospitalMngtSys.entities.DrugsEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DrugsRepository extends JpaRepository<DrugsEntity,Integer> {
    DrugsEntity findByHospitalIDAndId(int hospitalID, int id);
    List<DrugsEntity> findByHospitalID(int id, Sort sort);
    DrugsEntity findByHospitalIDAndDrugName(int id, String name);
}
