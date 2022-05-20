package capstone.Runtogether.controller;

import capstone.Runtogether.dto.BoardDto;
import capstone.Runtogether.dto.ChallengeDto;
import capstone.Runtogether.dto.ImageForm;
import capstone.Runtogether.entity.Board;
import capstone.Runtogether.entity.Challenge;
import capstone.Runtogether.model.Response;
import capstone.Runtogether.model.ResponseMessage;
import capstone.Runtogether.model.StatusCode;
import capstone.Runtogether.service.ChallengeService;
import capstone.Runtogether.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/challenge")
public class ChallengeController {
    private final ChallengeService challengeService;
    private final JwtTokenProvider jwtTokenProvider;

    //게시글 조회
    @GetMapping("/list")
    public ResponseEntity<Response<Object>> list() {
        List<Challenge> challengeList = challengeService.list();
        return new ResponseEntity<>(new Response<>(StatusCode.OK, "게시물 조회 성공", challengeList), HttpStatus.OK);
    }

    //게시글 작성
    @PostMapping("/admin/write")
    public ResponseEntity<Response<Object>> write(@RequestPart("challengeDto") ChallengeDto challengeDto, @RequestPart(value = "file",required = false) MultipartFile file, HttpServletRequest request) {

        challengeDto.setImageFileName(challengeService.saveImageInServer(file));
        challengeService.write(challengeDto);
        return new ResponseEntity<>(new Response<>(StatusCode.OK, "게시물 추가 성공"), HttpStatus.OK);
        /*if (jwtTokenProvider.validateToken(accessToken)) {
            challengeDto.setImageFileName(challengeService.saveImageInServer(file));
            challengeService.write(challengeDto);
            return new ResponseEntity<>(new Response<>(StatusCode.OK, "게시물 추가 성공"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new Response<>(StatusCode.FORBIDDEN, ResponseMessage.UNAUTHORIZED), HttpStatus.FORBIDDEN);
        }*/
    }

    @GetMapping("/{challengeId}")
    //게시글 읽기
    public ResponseEntity<Response<Object>> read(@PathVariable long challengeId) {
        Challenge article = challengeService.getArticle(challengeId);
        return new ResponseEntity<>(new Response<>(StatusCode.OK, "게시글 불러오기 성공", article), HttpStatus.OK);
    }

    //이미지 불러오기
    @GetMapping("/image")
    public ResponseEntity<Resource> readImage(@RequestBody ImageForm image ) {
        //컴퓨터에 따라 수정
        String path = "D:\\yeonjin\\study\\Run-Together\\backend\\src\\main\\resources\\static\\img\\challenge";
        String fileName = image.getName();
        Resource resource = new FileSystemResource(path + fileName);

        //파일 존재x
        if(!resource.exists()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        HttpHeaders headers = new HttpHeaders();

        try {
            Path filePath = Paths.get(path+fileName);
            headers.add("Content-Type", Files.probeContentType(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(resource,headers,HttpStatus.OK);
    }

}
