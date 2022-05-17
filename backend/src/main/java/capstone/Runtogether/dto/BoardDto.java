package capstone.Runtogether.dto;

import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class BoardDto {

    private String title;

    private String contents;

    private Date registerDate;

    private int views;

    private String ImageFileName;
}
