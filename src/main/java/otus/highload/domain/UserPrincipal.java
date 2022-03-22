package otus.highload.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

public class UserPrincipal implements UserDetails {
    static final long serialVersionUID = 1L;

    private final User user;

    private Collection<? extends GrantedAuthority> authorities;

    public UserPrincipal(User user) {
        this.user = user;
        if (user != null && user.getRoles() != null) {
            authorities = user.getRoles().stream()
                    .map(r -> new SimpleGrantedAuthority(r.getName()))
                    .collect(Collectors.toList());
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return user != null ? user.getPassword() : null;
    }

    @Override
    public String getUsername() {
        return user != null ? user.getLogin() : null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user != null ? user.isEnabled() : false;
    }
}
