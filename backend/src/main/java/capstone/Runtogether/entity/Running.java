package capstone.Runtogether.entity;

import lombok.*;

import javax.persistence.*;
import javax.xml.stream.Location;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Running implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "running_id")
    private Long runningId;

    @Column(name = "member_id")
    private Long memberId;

   /* @Column(name = "polyline", columnDefinition = "LONGTEXT")
    private String polyline;*/

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "distance", nullable = false)
    private double distance;

    @Column(name = "time", nullable = false)
    private int time;

    private int speed;

    @Column(length = 1000)
    private String polyline;
}
