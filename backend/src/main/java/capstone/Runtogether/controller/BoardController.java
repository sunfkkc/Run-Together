package capstone.Runtogether.controller;

import capstone.Runtogether.dto.BoardDto;
import capstone.Runtogether.dto.ChallengeDto;
import capstone.Runtogether.entity.Board;
import capstone.Runtogether.model.Response;
import capstone.Runtogether.model.ResponseMessage;
import capstone.Runtogether.model.StatusCode;
import capstone.Runtogether.service.BoardService;
import capstone.Runtogether.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/board")
public class BoardController {
    private final JwtTokenProvider jwtTokenProvider;
    private final BoardService boardService;

    //게시글 조회
    @GetMapping("/list")
    public ResponseEntity<Response<Object>> list(){
        List<Board> boardList = boardService.list();
        return new ResponseEntity<>(new Response<>(StatusCode.OK,"게시물 조회 성공",boardList),HttpStatus.OK);
    }

    //게시글 작성
    @PostMapping("/write")
    public ResponseEntity<Response<Object>> write(@RequestParam BoardDto boardDto, HttpServletRequest request) {
        String accessToken = request.getHeader("auth");
        if (jwtTokenProvider.validateToken(accessToken)) {
            boardService.write(boardDto);
            return new ResponseEntity<>(new Response<>(StatusCode.OK, "게시물 추가 성공"), HttpStatus.OK);
        }else {
            return new ResponseEntity<>(new Response<>(StatusCode.FORBIDDEN,ResponseMessage.UNAUTHORIZED),HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("{boardId}")
    //게시글 읽기
    public ResponseEntity<Response<Object>> read(@PathVariable long boardId){
        Board article = boardService.getArticle(boardId);
        return new ResponseEntity<>(new Response<>(StatusCode.OK, "게시글 불러오기 성공",article), HttpStatus.OK);
    }


/*    @PostMapping("/image")
    public ResponseEntity<Response<Object>> imageUpload(@RequestParam("file")MultipartFile multipartFile) throws IOException {
        File targetFile = new File("src/main/resources/static/img/"+multipartFile.getOriginalFilename());

        try {
            InputStream fileStream = multipartFile.getInputStream();

        } catch (IOException e) {
            targetFile.delete();
            e.printStackTrace();
        }
        return null;
    }*/
}
