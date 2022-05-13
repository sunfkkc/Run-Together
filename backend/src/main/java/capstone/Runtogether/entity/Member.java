package capstone.Runtogether.entity;

import capstone.Runtogether.dto.MemberDto;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@NoArgsConstructor(access=AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter @Setter @Builder
public class Member implements UserDetails {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId;
    private String email;
    private String pwd;
    private String name;
    private Character gender;
    private Role role;

    public Member(MemberDto memberDto){
        this.email = memberDto.getEmail();
        //pwd = "{noop}"+memberDto.getPwd();
        this.pwd = memberDto.getPwd();
        this.name = memberDto.getName();
        this.gender = memberDto.getGender();
        this.role = Role.MEMBER;

    }

    //Spring Security
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        ArrayList<GrantedAuthority> auth = new ArrayList<>(); //List인 이유 : 여러개의 권한을 가질 수 있다
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.toString());
        auth.add(authority);
        return auth;
    }

    @Override
    public String getPassword() {
        return pwd;
    }

    @Override
    public String getUsername() {
        return name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
