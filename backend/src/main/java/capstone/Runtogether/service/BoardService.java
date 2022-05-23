package capstone.Runtogether.service;

import capstone.Runtogether.dto.BoardDto;
import capstone.Runtogether.entity.Board;
import capstone.Runtogether.entity.Challenge;
import capstone.Runtogether.repository.BoardRepository;
import capstone.Runtogether.repository.ChallengeRepository;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
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
    public Long moveToChallengeDB(Long boardId, String state, String imageName) {
        try {
            if (boardRepository.findById(boardId).isPresent()) {
                Board board = boardRepository.findById(boardId).get();
                Challenge challenge = new Challenge(board);
                challenge.setImageFileName(imageName);
                challenge.setState(state);
                return challengeRepository.save(challenge).getChallengeId();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public String moveToChallengeImage(Long boardId) {
        try {
            String path = "D:\\yeonjin\\study\\Run-Together\\backend\\src\\main\\resources\\static\\img\\challenge";
            File image = findImage(boardId);
            String fileName = File.separator + image.getName();
            File targetImage = new File(path + fileName);

            Files.copy(image.toPath(), targetImage.toPath());
            return fileName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //게시글 삭제
    public void delete(Long boardId) {
        boardRepository.deleteById(boardId);
    }

    //서버 이미지 삭제
    public void deleteImage(Long boardId) {
        File image = findImage(boardId);

        if (image.exists()) {
            boolean delete = image.delete();
        }
    }

    //이미지 찾기
    public File findImage(Long boardId) {
        Board targetBoard = getArticle(boardId);
        String fileName = targetBoard.getImageFileName();

        String path = "D:\\yeonjin\\study\\Run-Together\\backend\\src\\main\\resources\\static\\img\\board";
        File targetFile = new File(path + fileName);

        if (targetFile.exists()) {
            return targetFile;
        }

        return null;
    }

}
