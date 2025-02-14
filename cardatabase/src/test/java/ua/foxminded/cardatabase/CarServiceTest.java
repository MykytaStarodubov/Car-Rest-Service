package ua.foxminded.cardatabase;

import ua.foxminded.cardatabase.model.Car;
import ua.foxminded.cardatabase.dao.CarRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CarServiceTest {

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private CarService carService;

    @Test
    void testCreateCar() {
        Car car = new Car(1L, "Toyota", 2020, "Corolla", "Sedan");
        when(carRepository.save(any(Car.class))).thenReturn(car);

        Car createdCar = carService.createCar(car);
        assertEquals("Toyota", createdCar.getCarMake());
    }

    @Test
    void testGetCarById() {
        Car car = new Car(1L, "BMW", 2019, "X5", "SUV");
        when(carRepository.findById(1L)).thenReturn(Optional.of(car));

        Optional<Car> retrievedCar = carService.getCarById(1L);
        assertEquals("BMW", retrievedCar.get().getCarMake());
    }

    @Test
    void testDeleteCar() {
        carService.deleteCar(1L);
        Mockito.verify(carRepository).deleteById(1L);
    }
}
