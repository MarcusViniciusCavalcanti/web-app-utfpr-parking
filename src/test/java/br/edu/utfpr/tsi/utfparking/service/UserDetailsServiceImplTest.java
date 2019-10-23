package br.edu.utfpr.tsi.utfparking.service;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = {"classpath:/sql/basic_user.sql"})
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = {"classpath:/sql/delete_basic_user.sql"})
public class UserDetailsServiceImplTest {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Test
    public void should_return_user() {
        var admin = userDetailsService.loadUserByUsername("vinicius_admin");
        assertThat(admin, Matchers.is(admin));
    }

    @Test(expected = UsernameNotFoundException.class)
    public void should_return_exception_when_username_not_found() {
        userDetailsService.loadUserByUsername("not_found");
    }
}
