package capstone.Runtogether.dto;

import capstone.Runtogether.domain.Role;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter @Builder
public class MemberDto{
    private String email;
    private String pwd;
    private String name;
    private Character gender;
    private Role role;

}
