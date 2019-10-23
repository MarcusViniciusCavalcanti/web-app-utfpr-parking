package br.edu.utfpr.tsi.utfparking.service;

import br.edu.utfpr.tsi.utfparking.data.CarRepository;
import br.edu.utfpr.tsi.utfparking.models.entities.Car;
import br.edu.utfpr.tsi.utfparking.utils.ToolsTest;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CarService.class)
@ActiveProfiles("test")
public class CarServiceTest {

    @MockBean
    private CarRepository carRepository;

    @Autowired
    private CarService carService;

    @Test
    public void shout_return_car_by_plate() {
        var car1 = Car.builder()
                .lastAccess(LocalDateTime.now())
                .model("model")
                .plate("ABC1234")
                .id(1L)
                .user(ToolsTest.createUser(ToolsTest.createUserWithAccessCardUser(), "Driver"))
                .build();

        var car2 = Car.builder()
                .lastAccess(LocalDateTime.now())
                .model("model")
                .plate("ABC1235")
                .id(2L)
                .user(ToolsTest.createUser(ToolsTest.createUserWithAccessCardUser(), "Driver"))
                .build();

        Mockito.when(carRepository.findAllByPlateIn(List.of("ABC1234"))).thenReturn(List.of(car1, car2));

        var result = carService.getCarByPlates(List.of("ABC1234"));

        var resultCar1 = result.get(0);
        var resultCar2 = result.get(1);

        assertThat(resultCar1.getPlate(), Matchers.is("ABC1234"));
        assertThat(resultCar1.getId(), Matchers.is(1L));

        assertThat(resultCar2.getPlate(), Matchers.is("ABC1235"));
        assertThat(resultCar2.getId(), Matchers.is(2L));
    }

}
