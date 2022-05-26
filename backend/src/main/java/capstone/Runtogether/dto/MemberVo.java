package capstone.Runtogether.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class MemberVo {
    private Long memberId;
    private String name;
}
