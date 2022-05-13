package capstone.Runtogether.dto;

import capstone.Runtogether.entity.Role;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter @Builder
public class MemberDto{
    private long memberId;
    private String email;
    private String pwd;
    private String name;
    private Character gender;
    private Role role;
}
