package capstone.Runtogether.service;

import capstone.Runtogether.dto.BoardDto;
import capstone.Runtogether.dto.ChallengeDto;
import capstone.Runtogether.entity.Board;
import capstone.Runtogether.entity.Challenge;
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
public class ChallengeService {
    private final ChallengeRepository challengeRepository;

    @Autowired
    public ChallengeService(ChallengeRepository challengeRepository) {
        this.challengeRepository = challengeRepository;
    }

    //목록 조회
    public List<Challenge> list() {
        return challengeRepository.findAll(Sort.by(Sort.Direction.DESC, "challengeId"));
    }

    //글 작성
    public Optional<Challenge> write(ChallengeDto challengeDto) {
        Challenge challenge = new Challenge(challengeDto);
        return Optional.ofNullable(challengeRepository.save(challenge));
    }


    //게시글 읽기
    public Challenge getArticle(Long challengeId){
        try {
            if(challengeRepository.findById(challengeId).isPresent()){
                Challenge article = challengeRepository.findById(challengeId).get();
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
        String saveFileName = File.separator + uuid + "_" + fileName;
        Path savePath = Path.of("src/main/resources/static/img/challenge/"+ saveFileName);

        try {
            //파일저장
            multipartFile.transferTo(savePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return saveFileName;

    }

    //게시글 삭제
    public void delete (Long challengeId){
        challengeRepository.deleteById(challengeId);
    }

    //서버 이미지 삭제
    public void deleteImage(String fileName) {
        String path = "D:\\yeonjin\\study\\Run-Together\\backend\\src\\main\\resources\\static\\img\\challenge";

        File targetFile = new File(path + fileName);

        if(targetFile.exists()){
            boolean delete = targetFile.delete();
        }
    }







}
