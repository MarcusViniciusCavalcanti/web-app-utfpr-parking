package br.edu.utfpr.tsi.utfparking.service;

import br.edu.utfpr.tsi.utfparking.data.RecognizeRepository;
import br.edu.utfpr.tsi.utfparking.models.entities.Recognize;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RecognizeServiceTest {

    @Autowired
    private RecognizeRepository recognizeRepository;

    @Autowired
    private RecognizeService recognizeService;

    @Before
    public void initDatabase() {
        var abc1234 = Recognize.builder()
                .epochTime(LocalDateTime.now().minusSeconds(5))
                .plate("ABC1234")
                .build();
        recognizeRepository.save(abc1234);
    }

    @After
    public void resetDatabase() {
        recognizeRepository.deleteAll();
    }

    @Test
    public void should_return_boolean_when_exist_time_is_small() {
        var abc1234 = recognizeService.isVerifier("ABC1234");
        assertThat(abc1234, Matchers.is(true));
    }

}
