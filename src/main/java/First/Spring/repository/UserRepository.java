package First.Spring.repository;

import First.Spring.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
//    @Query(value = "select First.Spring.model.Test(u.firstname,u.lastname) from " +
//            "USER u inner join Test t on t.userId=u.id where u.firstname=:firstname")
    Long countByFirstname(String firstname);

    List<User> findByFirstname(String firstname);
    List<User> findByFirstnameAndLastname(String firstname,String lastname);

    Page<User> findByFirstname(String firstname,Pageable page);

}
