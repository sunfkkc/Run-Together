package capstone.Runtogether.controller;

import capstone.Runtogether.dto.ChallengeDto;
import capstone.Runtogether.model.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/challenge")
public class ChallengeController {

    @PostMapping("/write")
    public ResponseEntity<Response<Object>> write(@RequestParam ChallengeDto challengeDto){


        return null;
    }
}
