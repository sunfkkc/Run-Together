package capstone.Runtogether.controller;

import capstone.Runtogether.dto.BoardDto;
import capstone.Runtogether.dto.ImageForm;
import capstone.Runtogether.entity.Board;
import capstone.Runtogether.model.Response;
import capstone.Runtogether.model.ResponseMessage;
import capstone.Runtogether.model.StatusCode;
import capstone.Runtogether.service.BoardService;
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

    //게시글 읽기
    @GetMapping("/{boardId}")
    public ResponseEntity<Response<Object>> read(@PathVariable long boardId) {
        Board article = boardService.getArticle(boardId);

        return new ResponseEntity<>(new Response<>(StatusCode.OK, "게시글 불러오기 성공", article), HttpStatus.OK);
    }

    //이미지 불러오기
    @GetMapping("/image/{boardId}")
    public ResponseEntity<Resource> readImage(@PathVariable long boardId) {
        Board article = boardService.getArticle(boardId);
        String fileName = article.getImageFileName();
        //컴퓨터에 따라 수정
        String path = "D:\\yeonjin\\study\\Run-Together\\backend\\src\\main\\resources\\static\\img\\board";
        Resource resource = new FileSystemResource(path + fileName);

        //파일 존재x
        if (!resource.exists()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        HttpHeaders headers = new HttpHeaders();

        try {
            Path filePath = Paths.get(path + fileName);
            headers.add("Content-Type", Files.probeContentType(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }

    @PostMapping("/admin/approve/{boardId}")
    public ResponseEntity<Response<Object>> approve(@PathVariable long boardId, @RequestParam("state") String state) {
        Long challengeId = boardService.moveToChallenge(boardId, state);
        if (challengeId != null) {
            return new ResponseEntity<>(new Response<>(StatusCode.CREATED, "승인 완료"), HttpStatus.CREATED);

        } else {
            return new ResponseEntity<>(new Response<>(StatusCode.INTERNAL_SERVER_ERROR, "승인 실패"), HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }

    //게시글 삭제
    @DeleteMapping("admin/delete/{boardId}")
    public ResponseEntity<Response<Object>> boardDelete(@PathVariable long boardId) {
        Board targetBoard = boardService.getArticle(boardId);
        boardService.deleteImage(targetBoard.getImageFileName());
        boardService.delete(boardId);
        return new ResponseEntity<>(new Response<>(StatusCode.OK, "게시글 삭제"), HttpStatus.OK);

    }


}
