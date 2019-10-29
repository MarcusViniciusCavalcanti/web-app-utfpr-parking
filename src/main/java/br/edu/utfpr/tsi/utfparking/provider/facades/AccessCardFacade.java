package br.edu.utfpr.tsi.utfparking.provider.facades;

import br.edu.utfpr.tsi.utfparking.models.entities.AccessCard;
import br.edu.utfpr.tsi.utfparking.models.entities.Role;
import br.edu.utfpr.tsi.utfparking.web.content.InputUser;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AccessCardFacade {

    public AccessCard createAccessCardByInputUser(InputUser dto, List<Role> roles) {
        return AccessCard.builder()
                .id(dto.getId())
                .username(dto.getUsername())
                .roles(roles)
                .accountNonExpired(dto.isAccountNonExpired())
                .credentialsNonExpired(dto.isCredentialsNonExpired())
                .enabled(dto.isEnabled())
                .accountNonLocked(dto.isAccountNonLocked())
                .build();
    }
}
