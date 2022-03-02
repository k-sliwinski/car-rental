package pl.ksliwinski.carrental.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.ksliwinski.carrental.exception.EmailAlreadyTakenException;
import pl.ksliwinski.carrental.model.Role;
import pl.ksliwinski.carrental.model.User;
import pl.ksliwinski.carrental.model.VerificationToken;
import pl.ksliwinski.carrental.model.pojo.VerificationEmail;
import pl.ksliwinski.carrental.repository.UserRepository;
import pl.ksliwinski.carrental.repository.VerificationTokenRepository;

import javax.persistence.EntityNotFoundException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;

    @Transactional
    public User registerUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new EmailAlreadyTakenException("Email already taken!");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);

        String verificationToken = generateVerificationToken(user);
        mailService.sendMail(new VerificationEmail("Please verify your account", user.getEmail(),
                "Please click on the url link to activate your account: " + "http://localhost:8080/api/verifyAccount/" + verificationToken));
        return userRepository.save(user);
    }

    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);

        verificationTokenRepository.save(verificationToken);
        return token;
    }

    @Transactional
    public String verifyAccount(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new EntityNotFoundException("Invalid token!"));
        String email = verificationToken.getUser().getEmail();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User with email: " + email + " doesn't exist!"));
        user.setEnabled(true);
        return "Account successfully activated!";
    }
}
