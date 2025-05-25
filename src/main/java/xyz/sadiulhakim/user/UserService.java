package xyz.sadiulhakim.user;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import xyz.sadiulhakim.exception.EntityNotFoundExecption;
import xyz.sadiulhakim.role.RoleService;

@Service
public class UserService {

    private final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private static final String USER_ROLE = "ROLE_USER";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleService roleService) {
        super();
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
    }

    public void save(User user) {

        LOGGER.info("Saving user {}", user.getEmail());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setJoiningDate(LocalDateTime.now());

        // Set role to ROLE_USER by default
        var userRole = roleService.findByName(USER_ROLE);
        user.setRole(userRole);

        userRepository.save(user);
        LOGGER.info("Done saving user {}", user.getEmail());
    }

    public User findByEmail(String email) {
        LOGGER.info("Finding user by email {}", email);

        // Do not make an endPoint for this method as it displays the password
        // Do not remove the password here because that would be used by
        // AuthenticationProvider
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundExecption("User id not found with email " + email));
    }

    public User findById(long id) {

        LOGGER.info("Finding user by id {}", id);

        var user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundExecption("User id not found with id " + id));

        // Remove the password as there would be an end-point for this method
        // Do not display the password
        user.setPassword(null);
        return user;
    }

    @RateLimiter(name = "defaultRateLimiter")
    public List<User> findAll(int pageNumber, int pageSize) {
        var users = userRepository.findAll(PageRequest.of(pageNumber, pageSize)).getContent();
        users.forEach(user -> user.setPassword(null));
        return users;
    }

    public void assignRoleToUser(long userId, long roleId) {
        var role = roleService.findById(roleId);
        var user = findById(userId);

        user.setRole(role);
        save(user);
        LOGGER.info("Done assigning to {} to User {}", role.getName(), user.getEmail());
    }

    public String changePassword(long userId, ChangePasswordPojo pojo) {

        if (!Objects.equals(pojo.newPassword(), pojo.confirmPassword())) {
            return "Passwords must match!";
        }

        var user = findById(userId);
        var matches = passwordEncoder.matches(pojo.currentPassword(), user.getPassword());
        if (!matches) {
            return "Invalid credentials!";
        }

        user.setPassword(passwordEncoder.encode(pojo.newPassword()));
        save(user);
        LOGGER.info("Done changing password of User {}", user.getEmail());
        return "";
    }

    public void delete(long userId) {
        LOGGER.info("Deleting User {}", userId);
        var user = findById(userId);
        userRepository.delete(user);
        LOGGER.info("Done Deleting User {}", user.getEmail());
    }
}
