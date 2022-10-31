package hospitalMngtSys.repositories.hospital;

import hospitalMngtSys.entities.LogEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogsRepository extends JpaRepository<LogEntity,Integer> {
    List<LogEntity> findByHospitalID(int id,Sort sort);
    List<LogEntity> findByHospitalIDAndType(int id, String type, Sort sort);
}
