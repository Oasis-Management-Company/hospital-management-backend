package hospitalMngtSys.controllers.hospital;

import hospitalMngtSys.entities.DrugsEntity;
import hospitalMngtSys.entities.LogEntity;
import hospitalMngtSys.entities.PaymentEntity;
import hospitalMngtSys.entities.RecordEntity;
import hospitalMngtSys.repositories.hospital.DrugsRepository;
import hospitalMngtSys.repositories.hospital.LogsRepository;
import hospitalMngtSys.repositories.hospital.PaymentRepository;
import hospitalMngtSys.repositories.patient.RecordRepository;
import hospitalMngtSys.services.hospital.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("api/hospital/pharmacy")
public class PharmacyController {
    @Autowired
    RecordRepository recordRepository;
    @Autowired
    PaymentRepository paymentRepository;
    @Autowired
    StaffService staffService;
    @Autowired
    DrugsRepository drugsRepository;
    @Autowired
    LogsRepository logsRepository;

    @GetMapping("/all/records/{staffID}/{id}")
    public List<RecordEntity> allRecords(@PathVariable String staffID,@PathVariable int id){
        return recordRepository.findByHospitalID(id,staffService.sortByDate());
    }

    @GetMapping("/record/{staffID}/{id}")
    public RecordEntity getRecordById(@PathVariable String staffID, @PathVariable int id){
        return recordRepository.findById(id);
    }

    @PutMapping("/record/{staffID}/{id}")
    public RecordEntity updateRecord(@PathVariable String staffID, @PathVariable int id,@RequestBody RecordEntity body){
        body.setId(id);
        RecordEntity record = recordRepository.findById(id);
        String drugs = record.getPrescriptions();
        String staffName = staffService.getStaffNameById(staffID,record.getHospitalID());
        LogEntity log = new LogEntity("Staff " + staffID,"has dispensed " +drugs.split(",").length +" drugs to Patient " +body.getPatientID(),"dispense","Pharmacist "+staffName+" has dispensed "+ drugs +" to " + record.getPatientName());
        log.setHospitalID(record.getHospitalID());
        logsRepository.save(log);
        return recordRepository.save(body);
    }

    @PostMapping("/new/payment/{staffID}/{id}")
    public PaymentEntity newPay(@PathVariable String staffID, @PathVariable int id, @RequestBody PaymentEntity body){
        String staffName = staffService.getStaffNameById(staffID,id);
        LogEntity log = new LogEntity("Staff " + staffID,"has collected "+ body.getPaymentType()+ " payment from Patient " +body.getPatientID(),"payment","Pharmacist "+staffName+" has collected payment from "+ body.getPatientName());
        log.setHospitalID(id);
        logsRepository.save(log);
        return paymentRepository.save(body);
    }

    @GetMapping("/dispense/{staffID}/{id}")
    public PaymentEntity getPaymentByRecordID(@PathVariable String staffID,@PathVariable int id){
        return paymentRepository.findByRecordID(id);
    }

    @GetMapping("/drugs/{id}/{staffID}/{drugs}")
    public Object[] getPrescriptions(@PathVariable String staffID,@PathVariable String drugs,@PathVariable int id){
        return staffService.prescription(drugs, id);
    }
}
