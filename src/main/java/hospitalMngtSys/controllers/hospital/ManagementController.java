package hospitalMngtSys.controllers.hospital;

import hospitalMngtSys.entities.*;
import hospitalMngtSys.repositories.hospital.*;
import hospitalMngtSys.repositories.patient.AppointmentRepository;
import hospitalMngtSys.repositories.patient.PatientRepository;
import hospitalMngtSys.repositories.patient.RecordRepository;
import hospitalMngtSys.services.hospital.SettingsService;
import hospitalMngtSys.services.hospital.StaffService;
import hospitalMngtSys.services.patient.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/hospital/settings")
public class ManagementController {

    @Autowired
    StaffRepository staffRepository;
    @Autowired
    SettingsRepository settingsRepository;
    @Autowired
    PatientRepository patientRepository;
    @Autowired
    RecordRepository recordRepository;
    @Autowired
    AppointmentRepository appointmentRepository;
    @Autowired
    SettingsService settingsService;
    @Autowired
    PatientService patientService;
    @Autowired
    VisitRepository visitRepository;
    @Autowired
    StaffService staffService;
    @Autowired
    LogsRepository logsRepository;
    @Autowired
    DrugsRepository drugsRepository;
    @Autowired
    PaymentRepository paymentRepository;


    @PostMapping("/")
    public ResponseEntity setup(@RequestBody SettingsEntity body) throws MessagingException, IOException {
        boolean emailExists = settingsService.emailExists(body.getEmail());
        if(emailExists){
            return ResponseEntity.ok(null);
        }
        String password = body.getPassword();
        String hashPass = patientService.hashPassword(password);
        body.setPassword(hashPass);
        settingsRepository.save(body);
        settingsService.sendmail(body.getEmail(),body.getName(),"hospital",null,body.getHospitalID());
        SettingsEntity settings = settingsRepository.findByEmail(body.getEmail());
        LogEntity log = new LogEntity("Admin","has finished system setup","regular",body.getName() +" has just setup the system");
        log.setHospitalID(settings.getHospitalID());
        logsRepository.save(log);
        //send email to the person there with their hospital id or link to login to the system
        return ResponseEntity.ok("success");
    }

    @PostMapping("/login")
    public SettingsEntity login(@RequestBody SettingsEntity body){
        SettingsEntity settings = settingsRepository.findByEmail(body.getEmail());
        String password = body.getPassword();
        boolean isMatch = staffService.unhashPassword(password,settings.getPassword());
        if(isMatch && settings.isEmailActivated()){
            LogEntity log = new LogEntity("Admin","has logged in to system","regular",settings.getName() +" has just logged in to the system");
            log.setHospitalID(settings.getHospitalID());
            logsRepository.save(log);
            return settings;
        }
        return null;
    }

    @PostMapping("/forgotPassword")
    public boolean forgotPassword(@RequestBody SettingsEntity body) throws MessagingException, IOException {
        SettingsEntity settings = settingsRepository.findByEmail(body.getEmail());
        if(settings != null){
            settingsService.sendmail(body.getEmail(),settings.getName(),"forgotHosp",null,settings.getHospitalID());
            return true;
        }else{
            return false;
        }
    }

    @PutMapping("/changePass/hospital/{hospitalID}")
    public SettingsEntity changeHospital(@RequestBody SettingsEntity body, @PathVariable int hospitalID){
        body.setHospitalID(hospitalID);
        String password = patientService.hashPassword(body.getPassword());
        body.setPassword(password);
        return settingsRepository.save(body);
    }

    @PutMapping("/changePass/patient/{hospitalID}/{id}")
    public PatientEntity changePatient(@RequestBody PatientEntity body,@PathVariable int hospitalID,@PathVariable String id) {
        PatientEntity patient = patientRepository.findByHospitalIDAndID(hospitalID, id);
        String password = patientService.hashPassword(body.getPassword());
        patient.setPassword(password);
        return patientRepository.save(patient);
    }

    @PutMapping("/changePass/staff/{hospitalID}/{staffID}")
    public StaffEntity changeStaff(@RequestBody StaffEntity body,@PathVariable int hospitalID,@PathVariable String id) {
        StaffEntity staff = staffRepository.findByHospitalIDAndStaffID(hospitalID, id);
        String password = patientService.hashPassword(body.getPassword());
        staff.setPassword(password);
        return staffRepository.save(staff);
    }

    @GetMapping("/hospital/list")
    public List<SettingsEntity> hospitalList(){
        return settingsRepository.findAll(staffService.sortByName());
    }

    @GetMapping("/{id}")
    public SettingsEntity settings(@PathVariable int id){
        return settingsRepository.findByHospitalID(id);
    }

    @PutMapping("/{id}")
    public SettingsEntity updateSettings(@PathVariable int id, @RequestBody SettingsEntity body){
        body.setHospitalID(id);
        LogEntity log = new LogEntity("Admin","has updated the system settings","regular",body.getName() +" has updated the system settings");
        log.setHospitalID(body.getHospitalID());
        logsRepository.save(log);
        return settingsRepository.save(body);
    }

    @PutMapping("/changePass/{hospitalID}")
    public SettingsEntity changePassword(@PathVariable int hospitalID,@RequestBody SettingsEntity body) throws NoSuchAlgorithmException, InvalidKeySpecException {
        body.setHospitalID(hospitalID);
        String password = body.getPassword();
        String hashPass = patientService.hashPassword(password);
        body.setPassword(hashPass);
        return settingsRepository.save(body);
    }

    @PostMapping("/matchPassword/{hospitalID}")
    public boolean matchPassword(@RequestBody SettingsEntity body,@PathVariable int hospitalID){
        SettingsEntity settings = settingsRepository.findByHospitalID(hospitalID);
        return patientService.unhashPassword(body.getPassword(),settings.getPassword());
    }

    @PostMapping("/logo/{id}")
    public ResponseEntity setLogo(@PathVariable int id, @RequestParam("logo") MultipartFile logo)throws IOException {
        SettingsEntity settings = settingsRepository.findByHospitalID(id);
        if(logo.getContentType().contains("jpg") || logo.getContentType().contains("png") || logo.getContentType().contains("jpeg")){
            settings.setLogo(settingsService.compressBytes(logo.getBytes()));
            LogEntity log = new LogEntity("Admin","has changed the logo","regular","Admin has just updated the logo");
            log.setHospitalID(id);
            logsRepository.save(log);
            return ResponseEntity.ok(settingsRepository.save(settings));
        }
        return ResponseEntity.ok(null);
    }

    @GetMapping("/logo/{id}")
    public SettingsEntity getLogo(@PathVariable int id){
        SettingsEntity settings = settingsRepository.findByHospitalID(id);
        byte[] logo = settingsService.decompressBytes(settings.getLogo());
        SettingsEntity settings1 = new SettingsEntity(logo);
        return settings1;
    }

    @PostMapping("/staff/register/{id}")
    public StaffEntity create(@RequestBody StaffEntity body,@PathVariable int id) throws MessagingException, IOException {
        StaffEntity staff = staffRepository.findByHospitalIDAndStaffID(id, body.getStaffID());
        if(staff == null){
            String password = body.getPassword();
            if (password == null){
                password = "123";
            }
            String hashPass = patientService.hashPassword(password);
            body.setPassword(hashPass);
            if(body.getRole() != "doctor"){
                body.setSpecialisation(null);
            }
            LogEntity log = new LogEntity("Admin","has added a new "+body.getRole(),"regular",body.getName()+" has been registered");
            log.setHospitalID(id);
            logsRepository.save(log);
            settingsService.sendmail(body.getEmail(),body.getName(),"staff",body.getStaffID(),id);
            return staffRepository.save(body);
        }
        return null;
    }

    @PutMapping("/staff/update/{hospitalID}/{staffID}")
    public StaffEntity updateStaff(@PathVariable String staffID,@PathVariable int hospitalID, @RequestBody StaffEntity body){
        body.setHospitalID(hospitalID);
        body.setStaffID(staffID);
        if(!body.getRole().contains("doctor")){
            body.setSpecialisation(null);
        }
        if(body.isDeactivate()){
            body.setAdmin(false);
        }
        LogEntity log = new LogEntity("Admin"," has updated staff "+body.getStaffID(),"regular",body.getName()+" has been updated");
        log.setHospitalID(hospitalID);
        logsRepository.save(log);
        return staffRepository.save(body);
    }

    @PostMapping("/staff/update/pic/{id}/{hospitalID}")
    public StaffEntity staffPic(@PathVariable String id,@PathVariable int hospitalID, @RequestParam("logo") MultipartFile logo)throws IOException {
        StaffEntity staff = staffRepository.findByHospitalIDAndStaffID(hospitalID, id);
        if(logo.getContentType().contains("jpg") || logo.getContentType().contains("png") || logo.getContentType().contains("jpeg")){
            staff.setLogo(settingsService.compressBytes(logo.getBytes()));
            LogEntity log = new LogEntity("Admin"," has changed the profile picture of Staff "+ id,"regular","Admin has just updated the profile pic of "+staffService.getStaffNameById(id,hospitalID));
            log.setHospitalID(hospitalID);
            logsRepository.save(log);
            return staffRepository.save(staff);
        }
        return null;
    }

    @GetMapping("/staff/pic/{id}/{hospitalID}")
    public StaffEntity getStaffPic(@PathVariable String id, @PathVariable int hospitalID){
        StaffEntity staff = staffRepository.findByHospitalIDAndStaffID(hospitalID,id);
        if(staff.getLogo() == null){
            return null;
        }else{
            byte[] pic = settingsService.decompressBytes(staff.getLogo());
            StaffEntity staffEntity = new StaffEntity(pic);
            return staffEntity;
        }
    }

    @GetMapping("/staff/schedule/{day}/{hospitalID}")
    public List<StaffEntity> getStaffOnDuty(@PathVariable String day,@PathVariable int hospitalID){
        List<StaffEntity> staffEntities = new ArrayList<>();
        List<StaffEntity> staff = staffRepository.findByHospitalID(hospitalID,staffService.sortByName());
        for(int i=0;i<staff.size();i++){
            StaffEntity staffEntity = staff.get(i);
            String onDuty = staffEntity.getOnDuty();
            if(onDuty.contains(day)){
                staffEntities.add(staffEntity);
            }
        }
        return staffEntities;
    }

    @GetMapping("/patients/{id}")
    public List<PatientEntity> getAllPatients(@PathVariable int id){
        Sort sortByFName = Sort.by("fname").descending();
        return patientRepository.findByHospitalID(id,sortByFName);
    }

    @GetMapping("/patient/{hospitalID}/{id}")
    public PatientEntity getPatientById(@PathVariable String id,@PathVariable int hospitalID){
        return patientRepository.findByHospitalIDAndID(hospitalID, id);
    }

    @PutMapping("/patient/{hospitalID}/{id}")
    public PatientEntity updatePatient(@PathVariable String id,@PathVariable int hospitalID,@RequestBody PatientEntity body){
        body.setID(id);
        String name = staffService.getPatientNameById(body.getID(),hospitalID);
        LocalDate dob = body.getDob();
        LocalDate localDate = LocalDate.now();
        int age = Period.between(dob,localDate).getYears();
        body.setAge(age);
        LogEntity log = new LogEntity("Admin","has updated Patient "+body.getID(),"regular",name+" details have been updated");
        log.setHospitalID(hospitalID);
        logsRepository.save(log);
        return patientRepository.save(body);
    }

    @GetMapping("/patient/records/{hospitalID}/{id}")
    public List<RecordEntity> patientRecords(@PathVariable String id,@PathVariable int hospitalID){
        return recordRepository.findByHospitalIDAndPatientID(hospitalID, id,staffService.sortByDate());
    }

    @GetMapping("/today/date")
    public ResponseEntity<String> todaysDate(){
        String pattern = "EEEEE dd MMMMM yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String newDate = simpleDateFormat.format(new Date());
        return ResponseEntity.ok(newDate);
    }

    @GetMapping("/today/time")
    public ResponseEntity<String> time(){
        DateFormat dateFormat = DateFormat.getTimeInstance();
        String time = dateFormat.format(new Date());
        return ResponseEntity.ok(time);
    }

    @GetMapping("/appointments/{id}")
    public List<AppointmentEntity> getAllAppointments(@PathVariable int id){
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

    @GetMapping("/today/appointments/{id}")
    public List<AppointmentEntity> getTodayAppointments(@PathVariable int id){
        LocalDate date = LocalDate.now();
        return appointmentRepository.findByHospitalIDAndDate(id,date,staffService.sortByTimeAndStatus());
    }

    @GetMapping("/appointment/{id}")
    public AppointmentEntity getAppointmentById(@PathVariable int id){
        return appointmentRepository.findById(id);
    }

    @PutMapping("/appointment/{hospitalID}/{id}")
    public AppointmentEntity updateAppointment(@PathVariable int hospitalID, @PathVariable int id,@RequestBody AppointmentEntity body){
        body.setId(id);
        String name = body.getPatientName();
        String patientID = body.getPatientID();
        LogEntity log = new LogEntity("Admin","has updated the appointment of Patient " + patientID,"appointment","Admin has updated the appointment of " + name);
        log.setHospitalID(hospitalID);
        logsRepository.save(log);
        return appointmentRepository.save(body);
    }

    @GetMapping("/today/visits/{id}")
    public List<VisitEntity> getTodaysVisits(@PathVariable int id){
        LocalDate date = LocalDate.now();
        return visitRepository.findByHospitalIDAndDate(id,date,staffService.sortByTimeAndStatus());
    }

    @GetMapping("/visits/{id}")
    public List<VisitEntity> getAllVisits(@PathVariable int id){
        return visitRepository.findByHospitalID(id,staffService.sortByDateAndTimeAndStatus());
    }

    @GetMapping("/visit/{hospitalID}/{id}")
    public VisitEntity visitById(@PathVariable int id,@PathVariable int hospitalID){
        return visitRepository.findByHospitalIDAndId(hospitalID,id);
    }

    @PutMapping("/visit/{hospitalID}/{id}")
    public VisitEntity updateVisit(@PathVariable int hospitalID, @PathVariable int id,@RequestBody VisitEntity body){
        body.setId(id);
        String name = body.getPatientName();
        String patientID = body.getPatientID();
        LogEntity log = new LogEntity("Admin","has updated the visit details of Patient " + patientID,"regular","Admin has updated the visit details of " + name);
        log.setHospitalID(hospitalID);
        logsRepository.save(log);
        return visitRepository.save(body);
    }

    @GetMapping("/staff/{id}")
    public List<StaffEntity> getAllStaff(@PathVariable int id){
        return staffRepository.findByHospitalID(id,staffService.sortByName());
    }

    @GetMapping("/today/staff/{id}")
    public List<StaffEntity> availableStaff(@PathVariable int id){
        LocalDate localDate = LocalDate.now();
        String day = localDate.getDayOfWeek().toString();
        List <StaffEntity> staff = staffRepository.findByHospitalID(id,staffService.sortByName());
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

    @GetMapping("/staff/medical/{id}")
    public List<StaffEntity> getMedicalStaff(@PathVariable int id){
        return staffRepository.findByHospitalIDAndType(id,"medical");
    }

    @GetMapping("/staff/regular/{id}")
    public List<StaffEntity> getNonMedicalStaff(@PathVariable int id){
        return staffRepository.findByHospitalIDAndType(id,"regular");
    }

    @GetMapping("staff/logs/{staffID}/{hospitalID}")
    public List<LogEntity> getStaffLogs(@PathVariable String staffID,@PathVariable int hospitalID){
        List<LogEntity> logs = logsRepository.findByHospitalID(hospitalID,staffService.sortByDate());
        List<LogEntity> logList = new ArrayList<>();
        for(int i =0;i<logs.size();i++){
            LogEntity log = logs.get(i);
            if(log.getPerson().contains(staffID)){
                logList.add(log);
            }
        }
        return logList;
    }

    @GetMapping("/doctors/{id}")
    public List<StaffEntity> getDoctors(@PathVariable int id){
        return staffRepository.findByHospitalIDAndRole(id,"doctor");
    }

    @PostMapping("/prescriptions/create/{hospitalID}/{staffID}")
    public DrugsEntity createDrug(@PathVariable int hospitalID,@PathVariable String staffID,@RequestBody DrugsEntity body){

        return drugsRepository.save(body);
    }

    @GetMapping("/prescriptions/{id}")
    public List<DrugsEntity> getDrugs(@PathVariable int id){
        Sort sortByName = Sort.by("drugName");
        return drugsRepository.findByHospitalID(id,sortByName);
    }

    @GetMapping("/prescriptions/{hospitalID}/{id}")
    public DrugsEntity getDrugsById(@PathVariable int hospitalID,@PathVariable int id){
        return drugsRepository.findByHospitalIDAndId(hospitalID,id);
    }

    @PutMapping("/prescriptions/{hospitalID}/{staffID}/{id}")
    public DrugsEntity updateDrug(@PathVariable int hospitalID,@PathVariable String staffID,@PathVariable int id,@RequestBody DrugsEntity body){
        body.setId(id);
        return drugsRepository.save(body);
    }

    @GetMapping("/payment/pending/{hospitalID}")
    public List<PaymentEntity> getPayment(@PathVariable int hospitalID){
        return paymentRepository.findByHospitalID(hospitalID,staffService.sortByDate());
    }

    @GetMapping("/record/{hospitalID}/{id}")
    public RecordEntity getRecordById(@PathVariable int id,@PathVariable int hospitalID){
        return recordRepository.findById(id);
    }

    @GetMapping("/payment/{hospitalID}/{id}")
    public PaymentEntity getPaymentByRecordID(@PathVariable int hospitalID,@PathVariable int id){
        return paymentRepository.findByRecordID(id);
    }

    @GetMapping("confirm/staff/{hospitalID}/{id}")
    public boolean confirmStaff(@PathVariable int hospitalID, @PathVariable String id){
        StaffEntity staff = staffRepository.findByHospitalIDAndStaffID(hospitalID, id);
        staff.setEmailActivated(true);
        staffRepository.save(staff);
        return true;
    }

    @GetMapping("confirm/patient/{hospitalID}/{id}")
    public boolean confirmPatient(@PathVariable int hospitalID, @PathVariable String id){
        PatientEntity patient = patientRepository.findByHospitalIDAndID(hospitalID, id);
        patient.setEmailActivated(true);
        patientRepository.save(patient);
        return true;
    }

    @GetMapping("confirm/hospital/{hospitalID}")
    public boolean confirmHospital(@PathVariable int hospitalID){
        SettingsEntity settings = settingsRepository.findByHospitalID(hospitalID);
        settings.setEmailActivated(true);
        settingsRepository.save(settings);
        return true;
    }
}
