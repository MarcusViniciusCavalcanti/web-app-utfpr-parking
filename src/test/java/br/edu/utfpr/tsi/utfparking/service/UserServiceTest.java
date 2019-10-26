package br.edu.utfpr.tsi.utfparking.service;

import br.edu.utfpr.tsi.utfparking.models.dtos.RoleDTO;
import br.edu.utfpr.tsi.utfparking.models.dtos.UserDTO;
import br.edu.utfpr.tsi.utfparking.web.constants.TypesUser;
import br.edu.utfpr.tsi.utfparking.web.content.InputUser;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = {"classpath:/sql/basic_user.sql"})
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = {"classpath:/sql/delete_basic_user.sql"})
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    public void should_save_new_user_with_role_user() {
        var inputUser = getInputUser(List.of(1L), TypesUser.STUDENTS);
        var userDTO = userService.saveNewUser(inputUser);

        checkValues(inputUser, userDTO, 1);
    }

    @Test
    public void should_save_new_user_with_role_operator() {
        var inputUser = getInputUser(List.of(1L, 3L), TypesUser.SERVICE);
        var userDTO = userService.saveNewUser(inputUser);

        checkValues(inputUser, userDTO, 2);
    }

    @Test
    public void should_save_new_user_with_role_admin() {
        var inputUser = getInputUser(List.of(1L, 2L), TypesUser.SERVICE);
        var userDTO = userService.saveNewUser(inputUser);

        checkValues(inputUser, userDTO, 2);
    }

    @Test
    public void should_save_new_user_without_car() {
        var inputUser = new InputUser();
        inputUser.setAccountNonExpired(true);
        inputUser.setAccountNonLocked(true);
        inputUser.setEnabled(true);
        inputUser.setAuthorities(List.of(1L));
        inputUser.setName("name user");
        inputUser.setUsername("username_user");
        inputUser.setPassword("1234567");
        inputUser.setType(TypesUser.STUDENTS.getDescription());

        var userDTO = userService.saveNewUser(inputUser);

        assertThat(userDTO.getName(), Matchers.is(inputUser.getName()));
        assertThat(userDTO.getId(), Matchers.notNullValue());
        assertThat(userDTO.getType(), Matchers.is(inputUser.getType()));
        assertThat(userDTO.getCar(), Matchers.nullValue());

        var accessCard = userDTO.getAccessCard();
        assertThat(accessCard.getUsername(), Matchers.is(inputUser.getUsername()));
        assertThat(accessCard.getRoles(), Matchers.hasSize(1));
        assertThat(accessCard.getRoles().stream().map(RoleDTO::getName).collect(Collectors.toList()), Matchers.hasItems("ROLE_USER"));
    }

    @Test
    public void should_return_page_users() {
        var page = PageRequest.of(0, 20);
        var content = userService.getPage(page).getContent();

        assertThat(content, Matchers.hasSize(3));
        assertThat(content.get(0).getName(), Matchers.is("Vinicius"));
        assertThat(content.get(1).getName(), Matchers.is("ViniciusAdmin"));
        assertThat(content.get(2).getName(), Matchers.is("ViniciusOperador"));
    }

    @Test
    public void should_return_page_users_when_call_search() {
        var page = PageRequest.of(0, 20);
        var content = userService.getPageBySearch("Admin", page).getContent();

        assertThat(content, Matchers.hasSize(1));
        assertThat(content.get(0).getName(), Matchers.is("ViniciusAdmin"));
    }

    private void checkValues(InputUser inputUser, UserDTO userDTO, int quantityRoles) {
        assertThat(userDTO.getName(), Matchers.is(inputUser.getName()));
        assertThat(userDTO.getId(), Matchers.notNullValue());
        assertThat(userDTO.getType(), Matchers.is(inputUser.getType()));

        var accessCard = userDTO.getAccessCard();
        assertThat(accessCard.getUsername(), Matchers.is(inputUser.getUsername()));
        assertThat(accessCard.getRoles(), Matchers.hasSize(quantityRoles));
        assertThat(accessCard.getRoles().stream().map(RoleDTO::getName).collect(Collectors.toList()), Matchers.hasItems("ROLE_USER"));

        var car = userDTO.getCar();
        assertThat(car.getId(), Matchers.notNullValue());
        assertThat(car.getModel(), Matchers.is(inputUser.getCarModel()));
        assertThat(car.getPlate(), Matchers.is(inputUser.getCarPlate()));
    }

    private InputUser getInputUser(List<Long> roles, TypesUser typesUser) {
        var inputUser = new InputUser();
        inputUser.setAccountNonExpired(true);
        inputUser.setAccountNonLocked(true);
        inputUser.setEnabled(true);
        inputUser.setCarModel("Gol");
        inputUser.setCarPlate("ABC1234");
        inputUser.setName("name user");
        inputUser.setUsername("username_user");
        inputUser.setPassword("1234567");
        inputUser.setAuthorities(roles);
        inputUser.setType(typesUser.getDescription());
        return inputUser;
    }
}
