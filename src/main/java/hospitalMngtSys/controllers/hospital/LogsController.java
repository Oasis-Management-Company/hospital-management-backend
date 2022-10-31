package hospitalMngtSys.controllers.hospital;

import hospitalMngtSys.entities.LogEntity;
import hospitalMngtSys.repositories.hospital.LogsRepository;
import hospitalMngtSys.services.hospital.RecordService;
import hospitalMngtSys.services.hospital.SettingsService;
import hospitalMngtSys.services.hospital.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("api/hospital/logs")
public class LogsController {

    @Autowired
    LogsRepository logsRepository;
    @Autowired
    StaffService staffService;
    @Autowired
    SettingsService settingsService;

    @GetMapping("/all/{id}")
    public List<LogEntity> getAllLogs(@PathVariable int id) throws ParseException, MessagingException, IOException {
        return logsRepository.findByHospitalID(id,staffService.sortByDate());
    }

    @GetMapping("/type/{id}/{type}")
    public List<LogEntity> logType(@PathVariable String type,@PathVariable int id){
        return logsRepository.findByHospitalIDAndType(id,type,staffService.sortByDate());
    }

    @GetMapping("/type/{id}/{type}/{drugName}")
    public List<LogEntity> drugLogs(@PathVariable String type,@PathVariable int id,@PathVariable String drugName){
        List<LogEntity> logs = logsRepository.findByHospitalIDAndType(id,type,staffService.sortByDate());
        List<LogEntity> logEntities = new ArrayList<>();
        for(int i=0;i<logs.size();i++){
            LogEntity log = logs.get(i);
            if(log.getDescription().contains(drugName)){
                logEntities.add(log);
            }
        }
        return logEntities;
    }

}
