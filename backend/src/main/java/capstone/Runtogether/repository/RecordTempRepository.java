package capstone.Runtogether.repository;


import capstone.Runtogether.entity.Record;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class RecordTempRepository {

    @Resource(name = "redisTemplate")
    private HashOperations<String, String, Record> hashOps;

/*
    public List<Record> findRecordByMemberId(Long memberId){
        //데이터를 Map 자료형으로 가져옴
        Map<String, Record> map =hashOps.entries(String.valueOf(memberId));
        List<Record> records = new ArrayList<Record>();

        for(int i=1;i<=map.size();i++){
            records.add(map.get(String.valueOf(i)));
        }

        Collections.sort(records, new Comparator<Record>() {
            @Override
            public int compare(Record o1, Record o2) {
                return (int) (o1.getAccDistance() - o2.getAccDistance());
            }
        });

        return records;
    }*/

}
