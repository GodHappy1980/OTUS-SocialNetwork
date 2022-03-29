package otus.highload.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import otus.highload.domain.User;
import otus.highload.domain.UserPrincipal;
import otus.highload.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByLogin(username);

        user.ifPresent(u -> u.setRoles(userRepository.findRolesByUserId(u.getId())));

        return new UserPrincipal(user.orElseThrow(() -> new UsernameNotFoundException("User with name: " + username + " not found")));
    }
}
