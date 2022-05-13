package capstone.Runtogether.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
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

    @Column(name = "member_id", nullable = true)
    private Long memberId;

    @Column(name = "polyline", columnDefinition = "LONGTEXT")
    private String polyline;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "accumulated_distance", nullable = false)
    private double accDistance;

    @Column(name = "accumulated_distance", nullable = false)
    private Long accTime;

    @Column(name = "thumbnail", length = 300)
    private String thumbnail;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "record_id")
    private List<Record> record;


}
