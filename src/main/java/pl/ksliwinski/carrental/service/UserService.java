package pl.ksliwinski.carrental.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.ksliwinski.carrental.model.User;
import pl.ksliwinski.carrental.repository.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User with provided email: " + email + " doesn't exist"));
    }

    public List<User> getAll(int page, int size, Sort.Direction direction, String by) {
        return userRepository.findAll(PageRequest.of(page, size, Sort.by(direction, by))).getContent();
    }

    public void deleteById(Long id) {
        //TODO handle deleting user who rents car
        userRepository.deleteById(id);
    }

    @Transactional
    public User update(Long id, User user) {
        User userToUpdate = findById(id);
        userToUpdate.setFirstName(user.getFirstName());
        userToUpdate.setLastName(user.getLastName());
        //TODO check if user with email already exist
        userToUpdate.setEmail(user.getEmail());

        return userToUpdate;
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with provided id: " + id + " doesn't exist"));
    }
}
