package capstone.Runtogether.repository;

import capstone.Runtogether.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board,Long> {
    Board save(Board board);
    List<Board> findAll();
    Optional<Board> findById(Long boardId);
    void deleteById(Long boardId);
}
