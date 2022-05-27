package capstone.Runtogether.entity;

import capstone.Runtogether.dto.BoardDto;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id", nullable = false)
    private Long boardId;

    private String title;

    private String contents;

    @Column(name = "register_date")
    private LocalDateTime registerDate;

    private int views;

    @Column(name = "image_file_name")
    private String ImageFileName;

    public Board(BoardDto boardDto) {
        this.title = boardDto.getTitle();
        this.contents = boardDto.getContents();
        this.registerDate = LocalDateTime.now();
        this.views = 0;
        this.ImageFileName = boardDto.getImageFileName();
    }

}
