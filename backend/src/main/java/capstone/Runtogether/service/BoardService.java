package capstone.Runtogether.service;

import capstone.Runtogether.dto.BoardDto;
import capstone.Runtogether.entity.Board;
import capstone.Runtogether.entity.Challenge;
import capstone.Runtogether.repository.BoardRepository;
import capstone.Runtogether.repository.ChallengeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    private final ChallengeRepository challengeRepository;

    @Autowired
    public BoardService(BoardRepository boardRepository, ChallengeRepository challengeRepository) {
        this.boardRepository = boardRepository;
        this.challengeRepository = challengeRepository;
    }

    //목록 조회
    public List<Board> list() {
        return boardRepository.findAll(Sort.by(Sort.Direction.DESC, "boardId"));
    }

    //글 작성
    public Optional<Board> write(BoardDto boardDto) {
        Board board = new Board(boardDto);
        return Optional.ofNullable(boardRepository.save(board));
    }


    //게시글 읽기
    public Board getArticle(Long boardId) {
        try {
            if (boardRepository.findById(boardId).isPresent()) {
                Board article = boardRepository.findById(boardId).get();
                article.setViews(article.getViews() + 1);
                return article;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //파일 이름 저장
    public String saveImageInServer(MultipartFile multipartFile) {
        //UUID 설정
        String fileName = multipartFile.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();
        String saveFileName = File.separator + uuid + "_" + fileName;
        //컴퓨터에 따라 수정
        Path savePath = Path.of("src/main/resources/static/img/board/" + saveFileName);

        try {
            //파일저장
            multipartFile.transferTo(savePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return saveFileName;

    }

    //챌린지로 이동
    public Long moveToChallenge(Long boardId, String state) {
        try {
            if (boardRepository.findById(boardId).isPresent()) {
                Board board = boardRepository.findById(boardId).get();
                Challenge challenge = new Challenge(board);
                challenge.setState(state);
                return challengeRepository.save(challenge).getChallengeId();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    //게시글 삭제
    public void delete(Long boardId) {
        boardRepository.deleteById(boardId);
    }

    //서버 이미지 삭제
    public void deleteImage(String fileName) {
        String path = "D:\\yeonjin\\study\\Run-Together\\backend\\src\\main\\resources\\static\\img\\board";

        File targetFile = new File(path + fileName);

        if(targetFile.exists()){
            boolean delete = targetFile.delete();
        }
    }

}
