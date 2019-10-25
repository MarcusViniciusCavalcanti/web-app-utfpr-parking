package br.edu.utfpr.tsi.utfparking.web.content;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

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

    @NotEmpty
    private List<Long> authorities;

    private boolean accountNonExpired;

    private boolean accountNonLocked;

    private boolean credentialsNonExpired;

    private boolean enabled;

    private String carPlate;

    private String carModel;
}
