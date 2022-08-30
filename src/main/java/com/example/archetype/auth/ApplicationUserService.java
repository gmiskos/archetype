package com.example.archetype.auth;

import com.example.archetype.controllers.users.UserDTO;
import com.example.archetype.entities.Privilege;
import com.example.archetype.entities.Role;
import com.example.archetype.entities.User;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class ApplicationUserService  implements IUserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Autowired
    public ApplicationUserService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    public ApplicationUser findByUsername(String username) {
        User userDB = getApplicationUsers().stream().filter(user -> user.getUsername().equals(username)).findFirst().get();
        ApplicationUser applicationUser = new ApplicationUser();
        applicationUser.setUsername(userDB.getUsername());
        applicationUser.setPassword(userDB.getPassword());
        applicationUser.setAccountNonExpired(userDB.isTokenExpired());
        applicationUser.setEnabled(userDB.isEnabled());
        applicationUser.setAccountNonLocked(true);
        applicationUser.setCredentialsNonExpired(true);
        applicationUser.setGrantedAuthorities((List<? extends GrantedAuthority>) getAuthorities(userDB.getRoles()));
        return applicationUser;
    }

    @Override
    public void registerUser(UserDTO userDTO) {
        User user = new User(userDTO.getId(), userDTO.getFirstName(), userDTO.getLastName(), userDTO.getEmail(), userDTO.getUsername(), userDTO.getPassword(), userDTO.isEnabled(), userDTO.isTokenExpired(), userDTO.getRoles());

        userRepository.save(user);
    }

    private Collection<? extends GrantedAuthority> getAuthorities(
            Collection<Role> roles) {

        return getGrantedAuthorities(getPrivileges(roles));
    }

    private List<String> getPrivileges(Collection<Role> roles) {

        List<String> privileges = new ArrayList<>();
        List<Privilege> collection = new ArrayList<>();
        for (Role role : roles) {
            privileges.add(role.getName());
            collection.addAll(role.getPrivileges());
        }
        for (Privilege item : collection) {
            privileges.add(item.getName());
        }
        return privileges;
    }

    private List<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String privilege : privileges) {
            authorities.add(new SimpleGrantedAuthority("ROLE_"+privilege));
        }
        return authorities;
    }

    private List<User> getApplicationUsers(){
        List<User> users = Lists.newArrayList(
            new User(1L,"George", "Miskos", "gmiskos@gmail.com", "gmiskos",
                    passwordEncoder.encode("password"), true, true,
                    Lists.newArrayList(
                            new Role(1L, "ADMIN", Lists.newArrayList(
                                    new Privilege(1L, "READ")
                                )
                            )
                    )
            ),
            new User(1L,"Athanasia", "Irakli", "airakli@gmail.com", "airakli",
                    passwordEncoder.encode("password"), true, false,
                    Lists.newArrayList(
                            new Role(1L, "ADMIN", Lists.newArrayList(
                                    new Privilege(1L, "WRITE")
                                )
                            )
                    )
            ),
            new User(1L,"Peter", "Partsanakis", "banik@gmail.com", "banik",
                    passwordEncoder.encode("password"), true, false,
                    Lists.newArrayList(
                            new Role(1L, "ADMIN", Lists.newArrayList(
                                    new Privilege(1L, "DELETE")
                                )
                            )
                    )
            )
        );
        return users;
    }
}
