package capstone.Runtogether.controller;

import capstone.Runtogether.dto.BoardDto;
import capstone.Runtogether.entity.Board;
import capstone.Runtogether.model.Response;
import capstone.Runtogether.model.ResponseMessage;
import capstone.Runtogether.model.StatusCode;
import capstone.Runtogether.service.BoardService;
import capstone.Runtogether.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
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
    public ResponseEntity<Response<Object>> list() {
        List<Board> boardList = boardService.list();
        return new ResponseEntity<>(new Response<>(StatusCode.OK, "게시물 조회 성공", boardList), HttpStatus.OK);
    }

    //게시글 작성
    @PostMapping("/write")
    public ResponseEntity<Response<Object>> write(@RequestPart("boardDto") BoardDto boardDto, @RequestPart(value = "file", required = false) MultipartFile file, HttpServletRequest request) {
        boardDto.setImageFileName(boardService.saveImageInServer(file));
        boardService.write(boardDto);
        return new ResponseEntity<>(new Response<>(StatusCode.OK, "게시물 추가 성공"), HttpStatus.OK);

    }

    @GetMapping("/{boardId}")
    //게시글 읽기
    public ResponseEntity<Response<Object>> read(@PathVariable long boardId) {
        Board article = boardService.getArticle(boardId);
        return new ResponseEntity<>(new Response<>(StatusCode.OK, "게시글 불러오기 성공", article), HttpStatus.OK);
    }

    @PostMapping("/admin/approve/{boardId}")
    public ResponseEntity<Response<Object>> approve(@PathVariable long boardId, @RequestParam("state") String state) {
        Long challengeId = boardService.moveToChallenge(boardId, state);
        if (challengeId != null) {
            return new ResponseEntity<>(new Response<>(StatusCode.CREATED, "승인 완료"), HttpStatus.CREATED);

        }else {
            return new ResponseEntity<>(new Response<>(StatusCode.INTERNAL_SERVER_ERROR, "승인 실패"), HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }

}
