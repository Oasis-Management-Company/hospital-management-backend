package hospitalMngtSys.services.hospital;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class RecordService {

    public Sort sortByDate(){
        Sort sortByDate = Sort.by("date").descending();
        return sortByDate;
    }
}
