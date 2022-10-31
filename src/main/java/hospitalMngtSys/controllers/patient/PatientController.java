package hospitalMngtSys.controllers.patient;

import hospitalMngtSys.entities.*;
import hospitalMngtSys.repositories.hospital.LabRepository;
import hospitalMngtSys.repositories.hospital.LogsRepository;
import hospitalMngtSys.repositories.patient.AppointmentRepository;
import hospitalMngtSys.repositories.patient.PatientRepository;
import hospitalMngtSys.repositories.patient.RecordRepository;
import hospitalMngtSys.services.hospital.SettingsService;
import hospitalMngtSys.services.patient.PatientService;
import org.hibernate.boot.jaxb.internal.stax.LocalSchemaLocator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.xml.crypto.Data;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("api/patient")
public class PatientController {

    @Autowired
    PatientService patientService;
    @Autowired
    PatientRepository patientRepository;
    @Autowired
    AppointmentRepository appointmentRepository;
    @Autowired
    RecordRepository recordRepository;
    @Autowired
    SettingsService settingsService;
    @Autowired
    LogsRepository logsRepository;
    @Autowired
    LabRepository labRepository;

    @PostMapping("/register")
    public PatientEntity create(@RequestBody PatientEntity body) throws MessagingException, IOException {
        boolean exists = patientService.emailExists(body.getHospitalID(), body.getEmail());
        if(!exists){
            String password = body.getPassword();
            if (password == null){
                password = "123";
            }
            String hashPass = patientService.hashPassword(password);
            body.setPassword(hashPass);
            LocalDate dob = body.getDob();
            LocalDate localDate = LocalDate.now();
            int age = Period.between(dob,localDate).getYears();
            body.setAge(age);
            UUID generatedID = UUID.randomUUID();
            String hex = Integer.toHexString((int) generatedID.getMostSignificantBits());
            body.setID(hex);
            settingsService.sendmail(body.getEmail(),body.getFname(),"patient",hex,body.getHospitalID());
            return patientRepository.save(body);
        }
        return null;
    }

    @PostMapping("/login")
    public PatientEntity login(@RequestBody PatientEntity body) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String email = body.getEmail();
        PatientEntity patient = patientRepository.findByHospitalIDAndEmail(body.getHospitalID(), email);
        String password = body.getPassword();
        boolean isMatch = patientService.unhashPassword(password,patient.getPassword());
        if(isMatch && !patient.isDeactivate() && patient.isEmailActivated()){
            return patient;
        }
        return null;
    }

    @PostMapping("/forgotPassword")
    public boolean forgotPass(@RequestBody PatientEntity body) throws MessagingException, IOException {
        PatientEntity patient = patientRepository.findByHospitalIDAndEmail(body.getHospitalID(),body.getEmail());
        if(patient != null){
            settingsService.sendmail(body.getEmail(),patient.getFname(),"forgotPat",patient.getID(),body.getHospitalID());
            return true;
        }else{
            return false;
        }
    }

    @GetMapping("/{hospitalID}/{id}")
    public PatientEntity getPatient(@PathVariable String id,@PathVariable int hospitalID){
        return patientRepository.findByHospitalIDAndID(hospitalID, id);
    }

    @PutMapping("/{hospitalID}/{id}")
    public PatientEntity updatePatient(@PathVariable String id,@PathVariable int hospitalID,@RequestBody PatientEntity body) throws NoSuchAlgorithmException, InvalidKeySpecException {
        body.setID(id);
        return patientRepository.save(body);
    }

    @PutMapping("/changePass/{hospitalID}/{id}")
    public PatientEntity changePassword(@PathVariable String id,@PathVariable int hospitalID,@RequestBody PatientEntity body) throws NoSuchAlgorithmException, InvalidKeySpecException {
        body.setID(id);
        body.setHospitalID(hospitalID);
        String password = body.getPassword();
        String hashPass = patientService.hashPassword(password);
        body.setPassword(hashPass);
        return patientRepository.save(body);
    }

    @PostMapping("/matchPassword/{hospitalID}/{id}")
    public boolean matchPassword(@RequestBody PatientEntity body,@PathVariable int hospitalID,@PathVariable String id){
        PatientEntity patient = patientRepository.findByHospitalIDAndID(hospitalID, id);
        return patientService.unhashPassword(body.getPassword(),patient.getPassword());
    }

    @GetMapping("/appointment/list/{hospitalID}/{id}")
    public List<AppointmentEntity> getAllAppointments(@PathVariable String id,@PathVariable int hospitalID){
        Sort sort = patientService.sortByDateAndTime();
        List<AppointmentEntity> appointmentEntities = appointmentRepository.findByHospitalIDAndPatientID(hospitalID, id,sort);
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

    @GetMapping("/appointment/upcoming/{hospitalID}/{id}")
    public List<AppointmentEntity> getUpcomingAppointments(@PathVariable String id,@PathVariable int hospitalID){
        return appointmentRepository.findByStatusOrStatusAndPatientIDAndHospitalID(hospitalID,0,1,id,patientService.sortByDateAndTime());
    }

    @GetMapping("/appointment/{hospitalID}/{id}")
    public AppointmentEntity getAppointmentById(@PathVariable int id,@PathVariable int hospitalID){
        return appointmentRepository.findById(id);
    }

    @PutMapping("/appointment/{hospitalID}/{id}")
    public AppointmentEntity updateAppointment(@PathVariable int id, @RequestBody AppointmentEntity body){
        body.setId(id);
        return appointmentRepository.save(body);
    }

    @PostMapping("/appointment/book")
    public AppointmentEntity bookAppointment(@RequestBody AppointmentEntity body){
        List<AppointmentEntity> appointment = appointmentRepository.findByHospitalIDAndDateAndTimeAndDoctorName(body.getHospitalID(), body.getDate(),body.getTime(),body.getDoctorName());
        if(appointment == null || appointment.size() <3){
            String name = patientService.getPatientNameById(body.getPatientID(),body.getHospitalID());
            body.setPatientName(name);
            return appointmentRepository.save(body);
        }
        return null;
    }

    @GetMapping("/record/{hospitalID}/{id}")
    public List<RecordEntity> getAllRecords(@PathVariable String id,@PathVariable int hospitalID){
        Sort sortByDate = Sort.by(Sort.Direction.DESC,"date");
        return recordRepository.findByHospitalIDAndPatientID(hospitalID, id,sortByDate);
    }

    @GetMapping("lab/results/{hospitalID}/{id}")
    public List<LabEntity> getAllResults(@PathVariable int hospitalID,@PathVariable String id){
        Sort sortByDate = Sort.by(Sort.Direction.DESC,"date");
        List<LabEntity> lab = labRepository.findByHospitalIDAndPatientID(hospitalID,id,sortByDate);
        for(int i = 0;i<lab.size();i++){
            LabEntity lab1 = lab.get(i);
            if(lab1.getTestResult() != null){
                lab1.setTestResult(settingsService.decompressBytes(lab1.getTestResult()));
            }
        }
        return lab;
    }

    @PostMapping("/logo/{hospitalID}/{id}")
    public PatientEntity setLogo(@PathVariable String id, @PathVariable int hospitalID, @RequestParam("logo") MultipartFile logo)throws IOException {
        PatientEntity patient = patientRepository.findByHospitalIDAndID(hospitalID, id);
        if(logo.getContentType().contains("jpg") || logo.getContentType().contains("png") || logo.getContentType().contains("jpeg")){
            patient.setLogo(settingsService.compressBytes(logo.getBytes()));
            LogEntity log = new LogEntity("Patient " + id,"has updated their profile picture","regular",patient.getFname() + " " + patient.getLname() +" has just updated their profile picture");
            log.setHospitalID(patient.getHospitalID());
            logsRepository.save(log);
            return patientRepository.save(patient);
        }
        return null;
    }

    @GetMapping("logo/{hospitalID}/{id}")
    public PatientEntity getLogo(@PathVariable String id, @PathVariable int hospitalID){
        PatientEntity patient = patientRepository.findByHospitalIDAndID(hospitalID, id);
        byte[] logo = settingsService.decompressBytes(patient.getLogo());
        PatientEntity patient1 = new PatientEntity(logo);
        return patient1;
    }

    @GetMapping("/statistics/mon/{hospitalID}/{id}")
    public List<RecordEntity> getStatisticsMon(@PathVariable int hospitalID, @PathVariable String id){
        Sort sortByDate = Sort.by(Sort.Direction.DESC,"date");
        List<RecordEntity> record = recordRepository.findByHospitalIDAndPatientID(hospitalID,id,sortByDate);
        List<RecordEntity> list = new ArrayList<>();
        for(int i =0;i<record.size();i++){
            RecordEntity record1 = record.get(i);
            LocalDate date = record1.getDate();
            if(date.getMonth() == LocalDate.now().getMonth()){
                list.add(record1);
            }
        }
        List<RecordEntity> list1 = new ArrayList<>();
        for(int i=0;i<list.size();i++){
            RecordEntity record1 = list.get(i);
            LocalDate date = record1.getDate();
            if(date.getDayOfWeek().toString().contains("MONDAY")){
                list1.add(record1);
            }
        }
        return list1;
    }

    @GetMapping("/statistics/tue/{hospitalID}/{id}")
    public List<RecordEntity> getStatisticsTue(@PathVariable int hospitalID, @PathVariable String id){
        Sort sortByDate = Sort.by(Sort.Direction.DESC,"date");
        List<RecordEntity> record = recordRepository.findByHospitalIDAndPatientID(hospitalID,id,sortByDate);
        List<RecordEntity> list = new ArrayList<>();
        for(int i =0;i<record.size();i++){
            RecordEntity record1 = record.get(i);
            LocalDate date = record1.getDate();
            if(date.getMonth() == LocalDate.now().getMonth()){
                list.add(record1);
            }
        }
        List<RecordEntity> list1 = new ArrayList<>();
        for(int i=0;i<list.size();i++){
            RecordEntity record1 = list.get(i);
            LocalDate date = record1.getDate();
            if(date.getDayOfWeek().toString().contains("TUESDAY")){
                list1.add(record1);
            }
        }
        return list1;
    }

    @GetMapping("/statistics/wed/{hospitalID}/{id}")
    public List<RecordEntity> getStatisticsWed(@PathVariable int hospitalID, @PathVariable String id){
        Sort sortByDate = Sort.by(Sort.Direction.DESC,"date");
        List<RecordEntity> record = recordRepository.findByHospitalIDAndPatientID(hospitalID,id,sortByDate);
        List<RecordEntity> list = new ArrayList<>();
        for(int i =0;i<record.size();i++){
            RecordEntity record1 = record.get(i);
            LocalDate date = record1.getDate();
            if(date.getMonth() == LocalDate.now().getMonth()){
                list.add(record1);
            }
        }
        List<RecordEntity> list1 = new ArrayList<>();
        for(int i=0;i<list.size();i++){
            RecordEntity record1 = list.get(i);
            LocalDate date = record1.getDate();
            if(date.getDayOfWeek().toString().contains("WEDNESDAY")){
                list1.add(record1);
            }
        }
        return list1;
    }

    @GetMapping("/statistics/thur/{hospitalID}/{id}")
    public List<RecordEntity> getStatisticsThur(@PathVariable int hospitalID, @PathVariable String id){
        Sort sortByDate = Sort.by(Sort.Direction.DESC,"date");
        List<RecordEntity> record = recordRepository.findByHospitalIDAndPatientID(hospitalID,id,sortByDate);
        List<RecordEntity> list = new ArrayList<>();
        for(int i =0;i<record.size();i++){
            RecordEntity record1 = record.get(i);
            LocalDate date = record1.getDate();
            if(date.getMonth() == LocalDate.now().getMonth()){
                list.add(record1);
            }
        }
        List<RecordEntity> list1 = new ArrayList<>();
        for(int i=0;i<list.size();i++){
            RecordEntity record1 = list.get(i);
            LocalDate date = record1.getDate();
            if(date.getDayOfWeek().toString().contains("THURSDAY")){
                list1.add(record1);
            }
        }
        return list1;
    }

    @GetMapping("/statistics/fri/{hospitalID}/{id}")
    public List<RecordEntity> getStatisticsFri(@PathVariable int hospitalID, @PathVariable String id){
        Sort sortByDate = Sort.by(Sort.Direction.DESC,"date");
        List<RecordEntity> record = recordRepository.findByHospitalIDAndPatientID(hospitalID,id,sortByDate);
        List<RecordEntity> list = new ArrayList<>();
        for(int i =0;i<record.size();i++){
            RecordEntity record1 = record.get(i);
            LocalDate date = record1.getDate();
            if(date.getMonth() == LocalDate.now().getMonth()){
                list.add(record1);
            }
        }
        List<RecordEntity> list1 = new ArrayList<>();
        for(int i=0;i<list.size();i++){
            RecordEntity record1 = list.get(i);
            LocalDate date = record1.getDate();
            if(date.getDayOfWeek().toString().contains("FRIDAY")){
                list1.add(record1);
            }
        }
        return list1;
    }

    @GetMapping("/statistics/sat/{hospitalID}/{id}")
    public List<RecordEntity> getStatisticsSat(@PathVariable int hospitalID, @PathVariable String id){
        Sort sortByDate = Sort.by(Sort.Direction.DESC,"date");
        List<RecordEntity> record = recordRepository.findByHospitalIDAndPatientID(hospitalID,id,sortByDate);
        List<RecordEntity> list = new ArrayList<>();
        for(int i =0;i<record.size();i++){
            RecordEntity record1 = record.get(i);
            LocalDate date = record1.getDate();
            if(date.getMonth() == LocalDate.now().getMonth()){
                list.add(record1);
            }
        }
        List<RecordEntity> list1 = new ArrayList<>();
        for(int i=0;i<list.size();i++){
            RecordEntity record1 = list.get(i);
            LocalDate date = record1.getDate();
            if(date.getDayOfWeek().toString().contains("SATURDAY")){
                list1.add(record1);
            }
        }
        return list1;
    }

    @GetMapping("/statistics/sun/{hospitalID}/{id}")
    public List<RecordEntity> getStatisticsSun(@PathVariable int hospitalID, @PathVariable String id){
        Sort sortByDate = Sort.by(Sort.Direction.DESC,"date");
        List<RecordEntity> record = recordRepository.findByHospitalIDAndPatientID(hospitalID,id,sortByDate);
        List<RecordEntity> list = new ArrayList<>();
        for(int i =0;i<record.size();i++){
            RecordEntity record1 = record.get(i);
            LocalDate date = record1.getDate();
            if(date.getMonth() == LocalDate.now().getMonth()){
                list.add(record1);
            }
        }
        List<RecordEntity> list1 = new ArrayList<>();
        for(int i=0;i<list.size();i++){
            RecordEntity record1 = list.get(i);
            LocalDate date = record1.getDate();
            if(date.getDayOfWeek().toString().contains("SUNDAY")){
                list1.add(record1);
            }
        }
        return list1;
    }
}