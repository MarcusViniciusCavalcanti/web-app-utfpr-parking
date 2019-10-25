package br.edu.utfpr.tsi.utfparking.provider.facades;

import br.edu.utfpr.tsi.utfparking.models.dtos.AccessCardDTO;
import br.edu.utfpr.tsi.utfparking.models.dtos.CarDTO;
import br.edu.utfpr.tsi.utfparking.models.dtos.RoleDTO;
import br.edu.utfpr.tsi.utfparking.models.dtos.UserDTO;
import br.edu.utfpr.tsi.utfparking.models.entities.User;
import br.edu.utfpr.tsi.utfparking.web.content.InputUser;
import br.edu.utfpr.tsi.utfparking.web.content.PageUser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserFacade {

    public User createUserByUserDTO(InputUser dto) {
        return User.builder()
                .name(dto.getName())
                .type(dto.getType())
                .numberAccess(0)
                .build();
    }

    public UserDTO createUserDTOByUser(User user) {
        var accessCard = user.getAccessCard();

        var rolesDto = accessCard.getRoles().stream()
                .map(role -> RoleDTO.builder()
                        .id(role.getId())
                        .description(role.getDescription())
                        .name(role.getName())
                        .build())
                .collect(Collectors.toList());


        var accessCardDto = AccessCardDTO.builder()
                .username(accessCard.getUsername())
                .accountNonExpired(accessCard.isAccountNonExpired())
                .accountNonLocked(accessCard.isAccountNonLocked())
                .credentialsNonExpired(accessCard.isCredentialsNonExpired())
                .enabled(accessCard.isEnabled())
                .roles(rolesDto)
                .build();

        var carDTO = user.car()
                .map(car -> new CarDTO(car.getId(), car.getPlate(), car.getModel()))
                .orElse(null);

        return UserDTO.builder()
                .accessCard(accessCardDto)
                .id(user.getId())
                .name(user.getName())
                .type(user.getType())
                .car(carDTO)
                .build();
    }

    public PageUser createPageUser(User user) {
        return PageUser.builder()
                .name(user.getName())
                .accountNonExpired(user.getAccessCard().isAccountNonExpired())
                .credentialsNonExpired(user.getAccessCard().isCredentialsNonExpired())
                .accountNonLocked(user.getAccessCard().isAccountNonLocked())
                .enabled(user.getAccessCard().isEnabled())
                .username(user.getAccessCard().getUsername())
                .id(user.getId())
                .model(user.getCar().getModel())
                .plate(user.getCar().getPlate())
                .type(user.getType())
                .isAuthorisedAccess(user.isAuthorisedAccess())
                .build();
    }
}
