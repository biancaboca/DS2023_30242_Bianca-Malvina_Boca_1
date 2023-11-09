package ro.tuc.ds2020.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ro.tuc.ds2020.entities.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PersonRepository extends JpaRepository<User, UUID> {

    /**
     * Example: JPA generate Query by Field
     */
    List<User> findByName(String name);

    Optional<User> findById(UUID uuid);

    User findUserById(UUID id);
    void deleteUserById(UUID id);
    Optional<User> findUserByUsername(String username);
    Optional<User> findUserByPassword(String password);
    Optional<User> findUserByUsernameAndPassword(String username, String password);
    /**
     * Example: Write Custom Query
     */
    @Query(value = "SELECT p " +
            "FROM users p " +
            "WHERE p.name = :name " +
            "AND p.role= :device ")
    Optional<User> findSeniorsByName(@Param("name") String name);

}
