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
                String rawPassword = "qwerty";
                String encodedPassword = passwordEncoder.encode(rawPassword);
                System.out.println("Encoded password: " + encodedPassword); // Log the encoded password
                user.setPasswordDigest(encodedPassword);
                userRepository.save(user);
            }
        };
    }
}
