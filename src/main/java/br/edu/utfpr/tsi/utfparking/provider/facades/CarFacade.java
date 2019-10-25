package br.edu.utfpr.tsi.utfparking.provider.facades;

import br.edu.utfpr.tsi.utfparking.models.entities.Car;
import br.edu.utfpr.tsi.utfparking.models.entities.User;
import br.edu.utfpr.tsi.utfparking.web.content.InputUser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CarFacade {

    public Car createCarByInputUser(InputUser dto, User user) {
        return Car.builder()
                .user(user)
                .plate(dto.getCarPlate())
                .model(dto.getCarModel())
                .build();
    }
}
