package capstone.Runtogether.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access=AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter @Builder
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String pw;
    private String name;
    private Character gender;


    public void setId(Long id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGender(Character gender) {
        this.gender = gender;
    }
}
