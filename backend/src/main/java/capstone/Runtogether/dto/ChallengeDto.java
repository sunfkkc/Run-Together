package capstone.Runtogether.dto;

import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ChallengeDto {

    private Long challengeId;

    private String title;

    private String contents;

    private Date registerDate;

    private int views;

    private String ImageFileName;

    private Boolean approved;
}
