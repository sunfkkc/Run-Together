package capstone.Runtogether.repository;

import capstone.Runtogether.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {
    Member save (Member member);
    Optional<Member> findByMemberId(Long memberId);
    Optional<Member> findByEmail(String email);
    Optional<Member> findByName(String name);
    List<Member> findAll();

}
