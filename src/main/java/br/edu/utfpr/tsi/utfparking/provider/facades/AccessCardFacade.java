package br.edu.utfpr.tsi.utfparking.provider.facades;

import br.edu.utfpr.tsi.utfparking.models.entities.AccessCard;
import br.edu.utfpr.tsi.utfparking.models.entities.Role;
import br.edu.utfpr.tsi.utfparking.web.content.InputUser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AccessCardFacade {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public AccessCard createAccessCardByInputUser(InputUser dto, List<Role> roles) {
        return AccessCard.builder()
                .id(dto.getId())
                .username(dto.getUsername())
                .password(bCryptPasswordEncoder.encode(dto.getPassword()))
                .roles(roles)
                .accountNonExpired(dto.isAccountNonExpired())
                .credentialsNonExpired(dto.isCredentialsNonExpired())
                .enabled(dto.isEnabled())
                .accountNonLocked(dto.isAccountNonLocked())
                .build();
    }
}
