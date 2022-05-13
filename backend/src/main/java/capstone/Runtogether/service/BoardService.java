package capstone.Runtogether.service;

import capstone.Runtogether.dto.BoardDto;
import capstone.Runtogether.entity.Board;
import capstone.Runtogether.entity.Member;
import capstone.Runtogether.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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



}
