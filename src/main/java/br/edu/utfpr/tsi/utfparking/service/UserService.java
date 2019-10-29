package br.edu.utfpr.tsi.utfparking.service;

import br.edu.utfpr.tsi.utfparking.data.AccessCardRepository;
import br.edu.utfpr.tsi.utfparking.data.RoleRepository;
import br.edu.utfpr.tsi.utfparking.data.UserRepository;
import br.edu.utfpr.tsi.utfparking.models.dtos.UserDTO;
import br.edu.utfpr.tsi.utfparking.models.entities.User;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final AccessCardRepository accessCardRepository;

    private final AccessCardFacade accessCardFacade;

    private final UserFacade userFacade;

    private final CarFacade carFacade;

    private final BCryptPasswordEncoder encoder;

    @Transactional
    public UserDTO saveNewUser(InputUser inputUser) {
        var user = getUser(inputUser);
        user.getAccessCard().setPassword(encoder.encode(inputUser.getPassword()));
        setCarIfExist(inputUser, user);

        var newUser = userRepository.save(user);
        return userFacade.createUserDTOByUser(newUser);
    }

    @Transactional
    public UserDTO editUser(InputUser inputUser) {
        return userRepository.findById(inputUser.getId())
                .map(user -> {
                    user.setName(inputUser.getName());
                    user.setType(inputUser.getType());

                    var car = user.car()
                            .map(mapCar -> {
                                mapCar.setPlate(inputUser.getCarPlate());
                                mapCar.setModel(inputUser.getCarModel());
                                return mapCar;
                            })
                            .orElse(new CarFacade().createCarByInputUser(inputUser, user));
                    user.setCar(car);

                    var password = inputUser.getNewPassword() == null || inputUser.getPassword().isEmpty() ? user.getAccessCard().getPassword() : encoder.encode(inputUser.getPassword());
                    var roles = roleRepository.findAllById(inputUser.getAuthorities());
                    user.getAccessCard().setPassword(password);
                    user.getAccessCard().setUsername(inputUser.getUsername());
                    user.getAccessCard().setRoles(roles);

                    return userRepository.save(user);
                })
                .map(userFacade::createUserDTOByUser)
                .orElseThrow(EntityNotFoundException::new);
    }

    public Page<PageUser> getPage(Pageable pageable) {
        var page = userRepository.findAll(pageable);
        var users = page.getContent().stream()
                .map(userFacade::createPageUser)
                .collect(Collectors.toList());
        return new PageImpl<>(users, pageable, page.getTotalElements());
    }

    public Page<PageUser> getPageBySearch(String search, Pageable pageable) {
        var page = userRepository.findAllByNameIsContainingOrAccessCardUsernameIsContainingOrCarPlateIsContaining(search, search, search, pageable);
        var users = page.getContent().stream()
                .map(userFacade::createPageUser)
                .collect(Collectors.toList());
        return new PageImpl<>(users, pageable, page.getTotalElements());
    }

    public InputUser getUserById(Long id) {
        return userRepository.findById(id)
                .map(InputUser::new)
                .orElseThrow(EntityNotFoundException::new);
    }

    private User getUser(InputUser inputUser) {
        var roles = roleRepository.findAllById(inputUser.getAuthorities());

        var accessCard = accessCardFacade.createAccessCardByInputUser(inputUser, roles);
        var user = userFacade.createUserByUserDTO(inputUser);

        user.setAccessCard(accessCard);
        return user;
    }

    private void setCarIfExist(InputUser inputUser, User user) {
        if ((inputUser.getCarPlate() != null && !inputUser.getCarPlate().isEmpty()) ||
                (inputUser.getCarModel() != null && !inputUser.getCarModel().isEmpty())) {
            var car = carFacade.createCarByInputUser(inputUser, user);
            user.setCar(car);
        }
    }
}
