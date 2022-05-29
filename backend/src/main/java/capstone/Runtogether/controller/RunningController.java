package capstone.Runtogether.controller;

import capstone.Runtogether.dto.RecordForm;
import capstone.Runtogether.dto.RunningDto;
import capstone.Runtogether.entity.Member;
import capstone.Runtogether.entity.Record;
import capstone.Runtogether.entity.Running;
import capstone.Runtogether.model.Response;
import capstone.Runtogether.model.ResponseMessage;
import capstone.Runtogether.model.StatusCode;
import capstone.Runtogether.repository.RecordTempRepository;
import capstone.Runtogether.service.MemberService;
import capstone.Runtogether.service.RecordService;
import capstone.Runtogether.service.RunningService;
import capstone.Runtogether.service.UserDetailServiceImpl;
import capstone.Runtogether.util.CookieUtil;
import capstone.Runtogether.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
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
    private final RunningService runningService;
    private final CookieUtil cookieUtil;
    private final UserDetailServiceImpl userDetailService;


    //러닝 데이터 저장
    @PostMapping("/complete")
    public ResponseEntity<Response<Object>> saveRunning(@RequestBody RunningDto runningDto, HttpServletRequest request) {
        //요청한 사용자 확인
        Cookie auth = cookieUtil.getCookie(request, "auth");
        String accessToken = auth.getValue();
        String email = jwtTokenProvider.getEmailFromJwt(accessToken);
        Member member = userDetailService.loadUserByUsername(email);

        //러닝 데이터 저장 요청
        Running completeRunning = runningService.completeRunning(runningDto, member.getMemberId());

        if (completeRunning.getMemberId().equals(member.getMemberId())) {
            return new ResponseEntity<>(new Response<>(StatusCode.CREATED, "러닝데이터 저장 완료", completeRunning.getRunningId()), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(new Response<>(StatusCode.INTERNAL_SERVER_ERROR, "데이터 저장 실패"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/record/{runningId}")
    public ResponseEntity<Response<Object>> saveRecord(@RequestBody RecordForm form, @PathVariable Long runningId) {
        Long saveRecord = recordService.saveRecord(form, runningId);
        if (saveRecord != null) {
            return new ResponseEntity<>(new Response<>(StatusCode.CREATED, "러닝데이터 저장 완료"), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(new Response<>(StatusCode.INTERNAL_SERVER_ERROR, "데이터 저장 실패"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //멤버의 러닝 조회
    @GetMapping("")
    public ResponseEntity<Response<Object>> getRunning(@RequestParam("memberName") String memberName) {
        try {
            Member findMember = memberService.getMember(memberName);

            List<Running> allRunning = runningService.findAllRunning(findMember.getMemberId());

            return new ResponseEntity<>(new
                    Response<>(StatusCode.OK, ResponseMessage.RUNNING_LIST_SUCCESS, allRunning), HttpStatus.OK);
        } catch (NullPointerException e) {
            log.error("memberName is not present");
        }
        return null;
    }

    //레코드 조회
    @GetMapping("/record")
    public ResponseEntity<Response<Object>> getRecord(@RequestParam("Id") Long runningId) {
        try {

            List<Record> allRecord = recordService.findAllRecord(runningId);

            return new ResponseEntity<>(new
                    Response<>(StatusCode.OK, ResponseMessage.RUNNING_LIST_SUCCESS, allRecord), HttpStatus.OK);
        } catch (NullPointerException e) {
            log.error("memberName is not present");
        }
        return null;
    }

}
