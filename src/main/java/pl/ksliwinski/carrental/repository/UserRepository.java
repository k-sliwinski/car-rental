package pl.ksliwinski.carrental.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.ksliwinski.carrental.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
