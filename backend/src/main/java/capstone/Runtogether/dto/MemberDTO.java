package capstone.Runtogether.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter @Builder
public class MemberDTO {
    private String email;
    private String pw;
    private String name;
    private Character gender;

}
