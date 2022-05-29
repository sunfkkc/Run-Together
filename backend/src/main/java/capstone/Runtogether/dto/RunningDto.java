package capstone.Runtogether.dto;

import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class RunningDto {

    private int time;

    private double distance;

    private int speed;

    private String polyline;


}
