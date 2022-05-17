package capstone.Runtogether.service;

import capstone.Runtogether.dto.BoardDto;
import capstone.Runtogether.entity.Board;
import capstone.Runtogether.model.Response;
import capstone.Runtogether.model.StatusCode;
import capstone.Runtogether.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class BoardService {
    private final BoardRepository boardRepository;

    @Autowired
    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    //글 작성
    public Optional<Board> write(BoardDto boardDto) {
        Board board = new Board(boardDto);
        return Optional.ofNullable(boardRepository.save(board));
    }

    //목록 조회
    public List<Board> list() {
        return boardRepository.findAll(Sort.by(Sort.Direction.DESC, "board_id"));
    }

    //게시글 읽기
    public Board getArticle(Long boardId){
        try {
            if(boardRepository.findById(boardId).isPresent()){
                Board article = boardRepository.findById(boardId).get();
                article.setViews(article.getViews()+1);
                return article;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //파일 이름 저장
    public String saveImageInServer(MultipartFile multipartFile){
        //UUID 설정
        String fileName = multipartFile.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();
        String saveFileName = "src/main/resources/static/img/" + File.separator + uuid + "_" + fileName;
        Path savePath = Path.of(saveFileName);

        try {
            //파일저장
            multipartFile.transferTo(savePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return saveFileName;

    }


}
