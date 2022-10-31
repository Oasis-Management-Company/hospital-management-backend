package hospitalMngtSys.controllers.hospital;

import hospitalMngtSys.entities.*;
import hospitalMngtSys.repositories.hospital.LabRepository;
import hospitalMngtSys.repositories.hospital.LogsRepository;
import hospitalMngtSys.repositories.hospital.StaffRepository;
import hospitalMngtSys.repositories.hospital.VisitRepository;
import hospitalMngtSys.repositories.patient.PatientRepository;
import hospitalMngtSys.repositories.patient.RecordRepository;
import hospitalMngtSys.services.hospital.SettingsService;
import hospitalMngtSys.services.hospital.StaffService;
import hospitalMngtSys.services.patient.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("api/hospital")
public class StaffController {

    @Autowired
    StaffRepository staffRepository;
    @Autowired
    StaffService staffService;
    @Autowired
    PatientRepository patientRepository;
    @Autowired
    VisitRepository visitRepository;
    @Autowired
    LogsRepository logsRepository;
    @Autowired
    LabRepository labRepository;
    @Autowired
    SettingsService settingsService;
    @Autowired
    RecordRepository recordRepository;
    @Autowired
    PatientService patientService;

    @PostMapping("/login")
    public StaffEntity login(@RequestBody StaffEntity body){
        int hospitalID = body.getHospitalID();
        String staffID = body.getStaffID();
        StaffEntity staff = staffRepository.findByHospitalIDAndStaffID(hospitalID,staffID);
        String password = body.getPassword();
        boolean isMatch = staffService.unhashPassword(password,staff.getPassword());
        if(isMatch && !staff.isDeactivate() && staff.isEmailActivated()){
            LogEntity log = new LogEntity("Staff "+ staff.getStaffID(),"has logged in to system","regular",staff.getName() +" has just logged in to the system");
            log.setHospitalID(staff.getHospitalID());
            logsRepository.save(log);
            return staff;
        }
        return null;
    }

    @GetMapping("/{staffID}/{id}")
    public StaffEntity getStaffByID(@PathVariable String staffID,@PathVariable int id){
        return staffRepository.findByHospitalIDAndStaffID(id,staffID);
    }

    @PostMapping("/forgotPassword")
    public boolean forgotPass(@RequestBody StaffEntity body) throws MessagingException, IOException {
        StaffEntity staff = staffRepository.findByHospitalIDAndEmail(body.getHospitalID(),body.getEmail());
        if(staff != null){
            settingsService.sendmail(body.getEmail(),staff.getName(),"forgotHosp",staff.getStaffID(),body.getHospitalID());
            return true;
        }else{
            return false;
        }
    }

    @PutMapping("/changePass/{hospitalID}/{id}")
    public StaffEntity changePassword(@PathVariable String id,@PathVariable int hospitalID,@RequestBody StaffEntity body) throws NoSuchAlgorithmException, InvalidKeySpecException {
        body.setStaffID(id);
        body.setHospitalID(hospitalID);
        String password = body.getPassword();
        String hashPass = patientService.hashPassword(password);
        body.setPassword(hashPass);
        return staffRepository.save(body);
    }

    @PostMapping("/matchPassword/{hospitalID}/{id}")
    public boolean matchPassword(@RequestBody StaffEntity body,@PathVariable int hospitalID,@PathVariable String id){
        StaffEntity staff = staffRepository.findByHospitalIDAndStaffID(hospitalID, id);
        return patientService.unhashPassword(body.getPassword(),staff.getPassword());
    }

    @GetMapping("/specialisation/{id}/{specialisation}/{date}")
    public List<StaffEntity> getDoctor(@PathVariable String specialisation,@PathVariable String date,@PathVariable int id){
        LocalDate localDate = LocalDate.parse(date);
        List <StaffEntity> staff = staffRepository.findByHospitalIDAndRoleAndSpecialisation(id,"doctor",specialisation);
        if(staff == null){
            return null;
        }
        String day = localDate.getDayOfWeek().toString();
        List<StaffEntity> staffs = new ArrayList<>();
        for(int i =0;i<staff.size();i++){
            StaffEntity staffEntity = staff.get(i);
            String onDuty = staffEntity.getOnDuty().toUpperCase();
            if (onDuty.contains(day)) {
                staffs.add(staffEntity);
            }
        }
        return staffs;
    }

    @PutMapping("/patient/{staffID}/{id}")
    public PatientEntity updatePatient(@PathVariable String staffID, @PathVariable String id, @RequestBody PatientEntity body){
        body.setID(id);
        return patientRepository.save(body);
    }

    @GetMapping("/patient/{hospitalID}/{id}")
    public PatientEntity getPatientByID(@PathVariable String id,@PathVariable int hospitalID){
        PatientEntity patient = patientRepository.findByHospitalIDAndID(hospitalID, id);
        return patient;
    }

    @PostMapping("/patient/search")
    public PatientEntity getPatientByName(@RequestBody PatientEntity body){
        int hospitalID = body.getHospitalID();
        String fname = body.getFname();
        String lname = body.getLname();
        LocalDate dob = body.getDob();
        PatientEntity patient = patientRepository.findByHospitalIDAndFnameAndLnameAndDob(hospitalID, fname,lname,dob);
        return patient;
    }

    @GetMapping("/visits/{hospitalID}/{staffID}")
    public List<VisitEntity> getPatients(@PathVariable int hospitalID){
        LocalDate date = LocalDate.now();
        return visitRepository.findByHospitalIDAndDate(hospitalID,date,staffService.sortByDateAndTime());
    }

    @GetMapping("/visit/{hospitalID}/{staffID}/{id}")
    public VisitEntity getVisitById(@PathVariable int id,@PathVariable int hospitalID){
        return visitRepository.findByHospitalIDAndId(hospitalID,id);
    }

    @PutMapping("/visit/{staffID}/{id}")
    public VisitEntity updateVisit(@PathVariable String staffID, @PathVariable int id,@RequestBody VisitEntity body){
        body.setId(id);
        //new log
        return visitRepository.save(body);
    }

    @PostMapping("/visit/{staffID}/{id}")
    public VisitEntity createVisit(@PathVariable String staffID,@PathVariable int id, @RequestBody VisitEntity body){
        String staffName = staffService.getStaffNameById(staffID,id);
        String name = staffService.getPatientNameById(body.getPatientID(),id);
        body.setPatientName(name);
        LocalDate date = LocalDate.now();
        body.setDate(date);
        LogEntity log = new LogEntity("Staff" + staffID,"has created a new visit for Patient "+body.getPatientID(),"regular",staffName +" has just created a new visit for " + name);
        log.setHospitalID(body.getHospitalID());
        logsRepository.save(log);
        return visitRepository.save(body);
    }

    @GetMapping("/visit/waiting/{staffID}/{id}")
    public List<VisitEntity> getWaiting(@PathVariable int id){
        LocalDate date = LocalDate.now();
        return visitRepository.findByHospitalIDAndDateAndStatus(id,date,0,staffService.sortByTimeAndStatus());
    }

    @GetMapping("/lab/{staffID}/{hospitalID}")
    public List<LabEntity> getTests(@PathVariable int hospitalID,@PathVariable String staffID){
        List<LabEntity> lab = labRepository.findByHospitalID(hospitalID,staffService.sortByDate());
        for(int i = 0;i<lab.size();i++){
            LabEntity lab1 = lab.get(i);
            if(lab1.getTestResult() != null){
                lab1.setTestResult(settingsService.decompressBytes(lab1.getTestResult()));
            }
        }
        return lab;
    }

    @GetMapping("/lab/{staffID}/{hospitalID}/{id}")
    public LabEntity getTest(@PathVariable int hospitalID, @PathVariable int id){
        return labRepository.findByHospitalIDAndId(hospitalID,id);
    }

    @PutMapping("/lab/{staffID}/{hospitalID}/{id}")
    public LabEntity updateTest(@PathVariable String staffID, @PathVariable int hospitalID, @PathVariable int id, @RequestParam("logo") MultipartFile logo) throws IOException, MessagingException {
        LabEntity lab = labRepository.findByHospitalIDAndId(hospitalID,id);
        if(logo.getContentType().contains("pdf")){
            lab.setUploadName(logo.getOriginalFilename());
            lab.setTestResult(settingsService.compressBytes(logo.getBytes()));
            String name = staffService.getStaffNameById(staffID,hospitalID);
            LogEntity log = new LogEntity("Staff" + staffID,"has uploaded a test result for Patient "+lab.getPatientID(),"regular",name +" has just uploaded a new test result for " + lab.getPatientName());
            log.setHospitalID(lab.getHospitalID());
            logsRepository.save(log);
            PatientEntity patient = patientRepository.findByHospitalIDAndID(hospitalID, lab.getPatientID());
            settingsService.sendmail(patient.getEmail(),patient.getFname() + " " + patient.getLname(),"lab",patient.getID(),hospitalID);
            return labRepository.save(lab);
        }
        return null;
    }

    @PostMapping("/lab/{staffID}/{hospitalID}")
    public LabEntity createTest(@PathVariable String staffID, @PathVariable int hospitalID,@RequestBody LabEntity body) throws Exception {
        LocalDate localDate = LocalDate.now();
        body.setDate(localDate);
        body.setPatientName(staffService.getPatientNameById(body.getPatientID(),hospitalID));
        String name = staffService.getStaffNameById(staffID,hospitalID);
        LogEntity log = new LogEntity("Staff" + staffID,"has requested a test for Patient "+body.getPatientID(),"regular",name +" has just requested " + body.getTest() + " for " + staffService.getPatientNameById(body.getPatientID(),hospitalID));
        log.setHospitalID(hospitalID);
        logsRepository.save(log);
        return labRepository.save(body);
    }

    @GetMapping("/statistics/mon/{hospitalID}/{id}")
    public List<RecordEntity> getStatisticsMon(@PathVariable int hospitalID, @PathVariable String id){
        Sort sortByDate = Sort.by(Sort.Direction.DESC,"date");
        List<RecordEntity> record = recordRepository.findByHospitalID(hospitalID,sortByDate);
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
    public List<RecordEntity> getStatisticsTue(@PathVariable int hospitalID){
        Sort sortByDate = Sort.by(Sort.Direction.DESC,"date");
        List<RecordEntity> record = recordRepository.findByHospitalID(hospitalID,sortByDate);
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
        List<RecordEntity> record = recordRepository.findByHospitalID(hospitalID,sortByDate);
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
        List<RecordEntity> record = recordRepository.findByHospitalID(hospitalID,sortByDate);
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
        List<RecordEntity> record = recordRepository.findByHospitalID(hospitalID,sortByDate);
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
        List<RecordEntity> record = recordRepository.findByHospitalID(hospitalID,sortByDate);
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
        List<RecordEntity> record = recordRepository.findByHospitalID(hospitalID,sortByDate);
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
