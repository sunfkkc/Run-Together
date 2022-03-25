package capstone.Runtogether.domain;

import capstone.Runtogether.dto.MemberDto;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@NoArgsConstructor(access=AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter @Builder
public class Member implements UserDetails {

    @Id
    private String email;
    private String pwd;
    private String name;
    private Character gender;
    private Role role;

    public Member(MemberDto memberDto){
        email = memberDto.getEmail();
        //pwd = "{noop}"+memberDto.getPwd();
        pwd = memberDto.getPwd();
        name = memberDto.getName();
        gender = memberDto.getGender();
        role = Role.USER;

    }
    public void setEmail(String email) {
        this.email = email;
    }

    public void setPwd(String pw) {
        this.pwd = pwd;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGender(Character gender) {
        this.gender = gender;
    }

    public void setRole(Role role){this.role = role;}

    //Spring Security
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.toString());
        ArrayList<GrantedAuthority> auth = new ArrayList<>(); //List인 이유 : 여러개의 권한을 가질 수 있다
        auth.add(authority);
        return auth;
    }

    @Override
    public String getPassword() {
        return pwd;
    }

    @Override
    public String getUsername() {
        return email;
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
