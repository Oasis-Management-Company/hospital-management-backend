package hospitalMngtSys.repositories.hospital;

import hospitalMngtSys.entities.PaymentEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity,Integer> {
    List<PaymentEntity> findByHospitalID(int id, Sort sort);
    PaymentEntity findByRecordID(int id);
    PaymentEntity findById(int id);
}
