package capstone.Runtogether.controller;

import capstone.Runtogether.entity.Member;
import capstone.Runtogether.entity.Record;
import capstone.Runtogether.entity.Running;
import capstone.Runtogether.model.Response;
import capstone.Runtogether.model.ResponseMessage;
import capstone.Runtogether.model.StatusCode;
import capstone.Runtogether.repository.RecordTempRepository;
import capstone.Runtogether.service.MemberService;
import capstone.Runtogether.service.RecordService;
import capstone.Runtogether.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/running")
public class RunningController {

    private final RecordService recordService;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;
    private final RecordTempRepository recordTempRepository;

    @GetMapping("/{memberId}")
    public ResponseEntity<Response<Object>> getRunningRecord(@PathVariable long memberId) {
        List<Running> memberRunningRecords = recordService.findRunningByMemberId(memberId);
        return new ResponseEntity<>(new
                Response<>(StatusCode.OK, ResponseMessage.RUNNING_LIST_SUCCESS, memberRunningRecords), HttpStatus.OK);
    }

    @GetMapping("/temp")
    public ResponseEntity<Response<Object>> getTempRecord(HttpServletRequest request) {
        String accessToken = request.getHeader("auth");

        if (jwtTokenProvider.validateToken(accessToken)) {
            Member loginMember = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            List<Record> records = recordTempRepository.findRecordByMemberId(loginMember.getMemberId());
            return new ResponseEntity<>(new Response<>(StatusCode.OK, ResponseMessage.RECORD_REDIS_LIST_SUCCESS, records), HttpStatus.OK);

        }else {
            return new ResponseEntity<>(new Response<>(StatusCode.FORBIDDEN,ResponseMessage.UNAUTHORIZED),HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping
    public ResponseEntity<Response<Object>> saveRecord(@RequestBody Map<String, Object> runningInfo, HttpServletRequest request){
        String accessToken = request.getHeader("auth");
        if(jwtTokenProvider.validateToken(accessToken)){
            Member loginMember = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();


        }
        return null;
    }

}
