package capstone.Runtogether.dto;

import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ChallengeDto {

    private String title;

    private String contents;

    private int views;

    private String ImageFileName;

    private String state;

}
