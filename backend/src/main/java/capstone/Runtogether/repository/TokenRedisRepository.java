package capstone.Runtogether.repository;

import capstone.Runtogether.domain.Member;
import org.springframework.data.repository.CrudRepository;

public interface TokenRedisRepository extends CrudRepository<Member, String> {
}
