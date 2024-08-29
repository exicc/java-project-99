package hexlet.code.app.config;

import hexlet.code.app.model.User;
import hexlet.code.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class UserInitializerConfig {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserInitializerConfig(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public CommandLineRunner initializeUser() {
        return args -> {
            String email = "hexlet@example.com";
            if (!userRepository.existsByEmail(email)) {
                User user = new User();
                user.setEmail(email);
                String encodedPassword = passwordEncoder.encode("qwerty");
                user.setPasswordDigest(encodedPassword);
                userRepository.save(user);
            }
        };
    }
}
