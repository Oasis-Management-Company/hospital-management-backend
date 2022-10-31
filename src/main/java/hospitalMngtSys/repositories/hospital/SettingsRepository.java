package hospitalMngtSys.repositories.hospital;

import hospitalMngtSys.entities.SettingsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SettingsRepository extends JpaRepository<SettingsEntity,Integer> {
    SettingsEntity findByHospitalID(int id);
    SettingsEntity findByEmail(String email);
}