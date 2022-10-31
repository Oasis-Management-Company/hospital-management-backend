package hospitalMngtSys.controllers.hospital;


import hospitalMngtSys.entities.*;
import hospitalMngtSys.repositories.hospital.LogsRepository;
import hospitalMngtSys.repositories.patient.AppointmentRepository;
import hospitalMngtSys.repositories.patient.PatientRepository;
import hospitalMngtSys.repositories.patient.RecordRepository;
import hospitalMngtSys.services.hospital.StaffService;
import hospitalMngtSys.services.patient.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("api/hospital/appointment")
public class HospitalAppointmentController {

    @Autowired
    AppointmentRepository appointmentRepository;
    @Autowired
    PatientService patientService;
    @Autowired
    StaffService staffService;
    @Autowired
    LogsRepository logsRepository;

    @GetMapping("/list/today/{staffID}/{id}")
    public List<AppointmentEntity> getTodayAppointments(@PathVariable String staffID,@PathVariable int id){
        LocalDate date = LocalDate.now();
        return appointmentRepository.findByHospitalIDAndDate(id,date,staffService.sortByTimeAndStatus());
    }

    @GetMapping("/list/{staffID}/{id}")
    public List<AppointmentEntity> getAllAppointments(@PathVariable String staffID,@PathVariable int id){
        List<AppointmentEntity> appointmentEntities = appointmentRepository.findByHospitalID(id,staffService.sortByDateAndTimeAndStatus());
        LocalDate localDate = LocalDate.now();
        for (int i = 0;i<appointmentEntities.size();i++){
            AppointmentEntity appointment = appointmentEntities.get(i);
            int status = appointment.getStatus();
            if(appointment.getDate().isBefore(localDate) && (status == 0 || status == 1)){
                appointment.setStatus(6);
                appointmentRepository.save(appointment);
            }
        }
        return appointmentEntities;
    }

    @GetMapping("/post/{staffID}/{hospitalID}")
    public List<AppointmentEntity> postAppointments(@PathVariable String staffID,@PathVariable int hospitalID){
        LocalDate date = LocalDate.now();
        return appointmentRepository.findByHospitalIDAndStatusAndDate(hospitalID,3,date,staffService.sortByDateAndTimeAndStatus());
    }

    @PutMapping("/post/{hospitalID}/{staffID}/{id}")
    public AppointmentEntity updatePost(@PathVariable int hospitalID, @PathVariable String staffID, @PathVariable int id, @RequestBody AppointmentEntity body){
        body.setId(id);
        String staffName = staffService.getStaffNameById(staffID,hospitalID);
        LogEntity log = new LogEntity("Staff " + staffID,"has posted Patient " + id + " to the "+ body.getSpecialist(),"appointment",staffName+" has posted " + body.getPatientName() + " to Dr. "+ body.getDoctorName());
        log.setHospitalID(body.getHospitalID());
        logsRepository.save(log);
        return appointmentRepository.save(body);
    }

    @GetMapping("/{staffID}/{id}")
    public AppointmentEntity getAppointmentById(@PathVariable String staffID,@PathVariable int id){
        return appointmentRepository.findById(id);
    }

    @PutMapping("/{hospitalID}/{staffID}/{id}")
    public AppointmentEntity updateAppointment(@PathVariable String staffID, @PathVariable int id,@PathVariable int hospitalID,@RequestBody AppointmentEntity body){
        body.setId(id);
        String staffName = staffService.getStaffNameById(staffID,hospitalID);
        String name = body.getPatientName();
        LogEntity log = new LogEntity("Staff " + staffID,"has updated the appointment of Patient " + id,"appointment",staffName+" has updated the appointment of " + name);
        log.setHospitalID(body.getHospitalID());
        logsRepository.save(log);
        return appointmentRepository.save(body);
    }

}