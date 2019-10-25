package br.edu.utfpr.tsi.utfparking.service;

import br.edu.utfpr.tsi.utfparking.data.RoleRepository;
import br.edu.utfpr.tsi.utfparking.data.UserRepository;
import br.edu.utfpr.tsi.utfparking.models.dtos.UserDTO;
import br.edu.utfpr.tsi.utfparking.provider.facades.AccessCardFacade;
import br.edu.utfpr.tsi.utfparking.provider.facades.CarFacade;
import br.edu.utfpr.tsi.utfparking.provider.facades.UserFacade;
import br.edu.utfpr.tsi.utfparking.web.content.InputUser;
import br.edu.utfpr.tsi.utfparking.web.content.PageUser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final AccessCardFacade accessCardFacade;

    private final UserFacade userFacade;

    private final CarFacade carFacade;

    public UserDTO saveNewUser(InputUser inputUser) {
        var roles = roleRepository.findAllById(inputUser.getAuthorities());

        var accessCard = accessCardFacade.createAccessCardByInputUser(inputUser, roles);
        var user = userFacade.createUserByUserDTO(inputUser);

        user.setAccessCard(accessCard);

        if ((inputUser.getCarPlate() != null && !inputUser.getCarPlate().isEmpty()) ||
                (inputUser.getCarModel() != null && !inputUser.getCarModel().isEmpty())) {
            var car = carFacade.createCarByInputUser(inputUser, user);
            user.setCar(car);
        }

        var newUser = userRepository.save(user);

        return userFacade.createUserDTOByUser(newUser);
    }

    public Page<PageUser> getPage(Pageable pageable) {
        var page = userRepository.findAll(pageable);
        var users = page.getContent().stream()
                .map(userFacade::createPageUser)
                .collect(Collectors.toList());
        return new PageImpl<>(users, pageable, page.getTotalElements());
    }

    public Page<PageUser> getPageBySearch(String search, Pageable pageable) {
        var page = userRepository.findAllByNameIsContainingOrAccessCardUsernameIsContaining(search, search, pageable);
        var users = page.getContent().stream()
                .map(userFacade::createPageUser)
                .collect(Collectors.toList());
        return new PageImpl<>(users, pageable, page.getTotalElements());
    }
}
