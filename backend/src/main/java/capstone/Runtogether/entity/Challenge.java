package capstone.Runtogether.entity;

import capstone.Runtogether.dto.BoardDto;
import capstone.Runtogether.dto.ChallengeDto;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Challenge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "challenge_id", nullable = false)
    private Long challengeId;

    private String title;

    private String contents;

    @Column(name = "register_date")
    private Date registerDate;

    private int views;

    @Column(name = "image_file_name")
    private String ImageFileName;

    private String state;

    public Challenge(ChallengeDto challengeDto) {
        this.title = challengeDto.getTitle();
        this.contents = challengeDto.getContents();
        this.registerDate = new Date();
        this.views = challengeDto.getViews();
        this.ImageFileName = challengeDto.getImageFileName();
        this.state = challengeDto.getState();
    }
}
