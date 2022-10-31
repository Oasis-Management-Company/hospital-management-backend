package hospitalMngtSys.controllers.hospital;

import hospitalMngtSys.entities.*;
import hospitalMngtSys.repositories.hospital.DrugsRepository;
import hospitalMngtSys.repositories.hospital.LogsRepository;
import hospitalMngtSys.repositories.hospital.StaffRepository;
import hospitalMngtSys.repositories.hospital.VisitRepository;
import hospitalMngtSys.repositories.patient.AppointmentRepository;
import hospitalMngtSys.repositories.patient.RecordRepository;
import hospitalMngtSys.services.hospital.RecordService;
import hospitalMngtSys.services.hospital.StaffService;
import org.hibernate.type.UUIDCharType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("api/hospital/doctor")
public class DoctorController {

    @Autowired
    StaffRepository staffRepository;
    @Autowired
    AppointmentRepository appointmentRepository;
    @Autowired
    StaffService staffService;
    @Autowired
    RecordService recordService;
    @Autowired
    RecordRepository recordRepository;
    @Autowired
    VisitRepository visitRepository;
    @Autowired
    LogsRepository logsRepository;
    @Autowired
    DrugsRepository drugsRepository;

    @GetMapping("/{staffID}/{id}")
    public StaffEntity getDoctorDetails(@PathVariable String staffID,@PathVariable int id){
        return staffRepository.findByHospitalIDAndStaffID(id,staffID);
    }

    @GetMapping("/appointments/{staffID}/{id}")
    public List<AppointmentEntity> getAllAppointments(@PathVariable String staffID,@PathVariable int id){
        String name = staffService.getStaffNameById(staffID,id);
        return appointmentRepository.findByStatusOrStatusAndDoctorNameAndHospitalID(id,3,4,name,staffService.sortByDateAndTimeAndStatus());
    }

    @PutMapping("/appointment/start/{hospitalID}/{staffID}/{id}")
    public AppointmentEntity startAppointment(@PathVariable int id, @PathVariable String staffID,@RequestBody int status,@PathVariable int hospitalID){
        String staffName = staffService.getStaffNameById(staffID,hospitalID);
        AppointmentEntity appointment = appointmentRepository.findById(id);
        appointment.setStatus(status);
        String patientID = appointment.getPatientID();
        String name = staffService.getPatientNameById(patientID,hospitalID);
        LogEntity log = new LogEntity("Staff " + staffID,"has started an appointment with Patient " +patientID,"appointment","Dr. "+staffName+" has started appointment with " + name);
        log.setHospitalID(appointment.getHospitalID());
        logsRepository.save(log);
        return appointmentRepository.save(appointment);
    }

    @PostMapping("/appointment/end/{hospitalID}/{staffID}/{id}")
    public RecordEntity endAppointment(@PathVariable int id, @PathVariable String staffID,@PathVariable int hospitalID,@RequestBody RecordEntity body){
        String staffName = staffService.getStaffNameById(staffID,hospitalID);
        AppointmentEntity appointment = appointmentRepository.findById(id);
        appointment.setStatus(5);
        appointmentRepository.save(appointment);
        body.setDoctorName(staffName);
        body.setDate(LocalDate.now());
        String name = body.getPatientName();
        LogEntity log = new LogEntity("Staff " + staffID,"has ended the appointment with Patient " +body.getPatientID(),"appointment","Dr. "+staffName+" has ended the appointment with " + name);
        log.setHospitalID(appointment.getHospitalID());
        logsRepository.save(log);
        String drugs = body.getPrescriptions();
        LogEntity log1 = new LogEntity("Staff " + staffID,"has prescribed " +drugs.split(",").length +" drugs to Patient " +body.getPatientID(),"regular","Dr. "+staffName+" has prescribed "+ drugs +" to " + name);
        log1.setHospitalID(appointment.getHospitalID());
        logsRepository.save(log1);
        return recordRepository.save(body);
    }

    @GetMapping("/visits/{staffID}/{hospitalID}")
    public List<VisitEntity> getVisitByDoctor(@PathVariable String staffID,@PathVariable int hospitalID){
        String name = staffService.getStaffNameById(staffID,hospitalID);
        LocalDate date = LocalDate.now();
        return visitRepository.findByHospitalIDAndDoctorNameAndDateAndStatusOrStatus(hospitalID,name,date,1,2,staffService.sortByTime());
    }

    @GetMapping("/visit/{hospitalID}/{staffID}/{id}")
    public VisitEntity getVisitById(@PathVariable int id,@PathVariable int hospitalID){
        return visitRepository.findByHospitalIDAndId(hospitalID,id);
    }

    @PutMapping("/visit/start/{hospitalID}/{staffID}/{id}")
    public VisitEntity startVisit(@PathVariable int id, @PathVariable String staffID,@PathVariable int hospitalID,@RequestBody int status){
        String staffName = staffService.getStaffNameById(staffID,hospitalID);
        VisitEntity visit = visitRepository.findByHospitalIDAndId(hospitalID, id);
        visit.setStatus(status);
        String name = visit.getPatientName();
        LogEntity log = new LogEntity("Staff " + staffID,"has started a visit with Patient " +visit.getPatientID(),"regular","Dr. "+staffName+" has started visit with " + name);
        log.setHospitalID(visit.getHospitalID());
        logsRepository.save(log);
        return visitRepository.save(visit);
    }

    @PostMapping("/visit/end/{hospitalID}/{staffID}/{id}")
    public RecordEntity endVisit(@PathVariable int id, @PathVariable String staffID,@PathVariable int hospitalID,@RequestBody RecordEntity body){
        String staffName = staffService.getStaffNameById(staffID,hospitalID);
        VisitEntity visit = visitRepository.findByHospitalIDAndId(hospitalID, id);
        visit.setStatus(3);
        visitRepository.save(visit);
        body.setDoctorName(staffName);
        body.setDate(LocalDate.now());
        String name = body.getPatientName();
        LogEntity log = new LogEntity("Staff " + staffID,"has ended the visit with Patient " +body.getPatientID(),"appointment","Dr. "+staffName+" has ended the appointment with " + name);
        log.setHospitalID(visit.getHospitalID());
        logsRepository.save(log);
        String drugs = body.getPrescriptions();
        LogEntity log1 = new LogEntity("Staff " + staffID,"has prescribed " +drugs.split(",").length +" drugs to Patient " +body.getPatientID(),"regular","Dr. "+staffName+" has prescribed "+ drugs +" to " + name);
        log1.setHospitalID(visit.getHospitalID());
        logsRepository.save(log1);
        return recordRepository.save(body);
    }

    @GetMapping("/records/{staffID}/{hospitalID}")
    public List<RecordEntity> getAllRecords(@PathVariable String staffID,@PathVariable int hospitalID){
        return recordRepository.findByHospitalIDAndDoctorID(hospitalID,staffID,recordService.sortByDate());
    }

    @GetMapping("/patient/records/{staffID}/{hospitalID}/{id}")
    public List<RecordEntity> getRecordById(@PathVariable String staffID, @PathVariable String id,@PathVariable int hospitalID){
        return recordRepository.findByHospitalIDAndPatientID(hospitalID,id,recordService.sortByDate());
    }

    @GetMapping("/prescriptions/{staffID}/{id}")
    public List<DrugsEntity> getDrugs(@PathVariable String staffID,@PathVariable int id){
        Sort sortByName = Sort.by("drugName");
        return drugsRepository.findByHospitalID(id,sortByName);
    }
}
