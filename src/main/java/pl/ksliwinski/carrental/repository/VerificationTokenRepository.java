package pl.ksliwinski.carrental.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.ksliwinski.carrental.model.VerificationToken;

import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByToken(String token);
}
