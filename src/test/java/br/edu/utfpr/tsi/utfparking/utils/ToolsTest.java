package br.edu.utfpr.tsi.utfparking.utils;

import br.edu.utfpr.tsi.utfparking.models.entities.AccessCard;
import br.edu.utfpr.tsi.utfparking.models.entities.Role;
import br.edu.utfpr.tsi.utfparking.models.entities.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Random;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ToolsTest {

    public static AccessCard createUserWithAccessCardAdmin() {
        return createAccessCard("vinicius_admin", createRole("Perfil Administrador","ADMIN" ));
    }

    public static AccessCard createUserWithAccessCardOperator() {
        return createAccessCard("vinicius_operador", createRole("Perfil Operador", "OPERATOR"));
    }

    public static AccessCard createUserWithAccessCardUser() {
        return createAccessCard("vinicius_usuario", createRole());
    }

    public static User createUser(AccessCard accessCard, String type) {
        var random = new Random();
        var user = User.builder()
                .accessCard(accessCard)
                .name("UserTest")
                .type(type)
                .numberAccess(Math.abs(random.nextInt()))
                .authorisedAccess(true)
                .build();

        accessCard.setUser(user);
        return user;
    }

    private static List<Role> createRole() {
        return  List.of(Role.builder()
                .description("Perfil usuário")
                .name("USER")
                .build());
    }

    private static List<Role> createRole(String profileName, String name) {
        var roleUser = Role.builder()
                .description("Perfil usuário")
                .name("USER")
                .build();

        var other = Role.builder()
                .description(profileName)
                .name(name)
                .build();

        return List.of(roleUser, other);
    }

    private static AccessCard createAccessCard(String username, List<Role> roles) {
        var bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return AccessCard.builder()
                .password(bCryptPasswordEncoder.encode("1234567"))
                .username(username)
                .roles(roles)
                .build();
    }
}
