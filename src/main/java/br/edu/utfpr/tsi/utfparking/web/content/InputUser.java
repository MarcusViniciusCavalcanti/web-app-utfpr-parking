package br.edu.utfpr.tsi.utfparking.web.content;

import br.edu.utfpr.tsi.utfparking.models.entities.Role;
import br.edu.utfpr.tsi.utfparking.models.entities.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Data
public class InputUser {

    private Long id;

    @NotBlank
    @Size(min = 5, max = 255)
    private String name;

    @NotBlank
    private String type;

    @NotBlank
    @Size(min = 5, max = 200)
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String newPassword;

    @NotEmpty
    private List<Long> authorities;

    private boolean accountNonExpired;

    private boolean accountNonLocked;

    private boolean credentialsNonExpired;

    private boolean enabled;

    private String carPlate;

    private String carModel;

    public InputUser(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.username = user.getAccessCard().getUsername();
        this.type = user.getType();

        // Desconhecido
        this.carModel = user.getCar() != null ? user.getCar().getModel() : "Ausente";
        this.carPlate = user.getCar() != null && !user.getCar().getPlate().equals(user.getId().toString()) ? user.getCar().getPlate() : "Ausente";

        this.accountNonExpired = user.getAccessCard().isAccountNonExpired();
        this.accountNonLocked = user.getAccessCard().isAccountNonLocked();
        this.credentialsNonExpired = user.getAccessCard().isCredentialsNonExpired();
        this.enabled = user.getAccessCard().isEnabled();
        this.authorities = user.getAccessCard().getRoles().stream()
                .map(Role::getId)
                .collect(Collectors.toList());
    }
}
