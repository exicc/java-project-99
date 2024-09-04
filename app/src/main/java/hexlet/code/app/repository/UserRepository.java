package hexlet.code.app.repository;

import hexlet.code.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Modifying
    @Query("DELETE FROM User u WHERE u.email = :email")
    @Transactional
    void deleteAllByEmailSQL(@Param("email") String email);

    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    void deleteByEmail(String email);

}
