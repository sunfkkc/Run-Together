package capstone.Runtogether.dto;

import capstone.Runtogether.entity.Board;
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

    private String ImageFileName;

    private String state;

}
