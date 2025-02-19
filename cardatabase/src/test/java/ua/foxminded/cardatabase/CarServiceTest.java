package ua.foxminded.cardatabase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import ua.foxminded.cardatabase.dao.CarRepository;
import ua.foxminded.cardatabase.exception.CarNotFoundException;
import ua.foxminded.cardatabase.model.Car;
import ua.foxminded.cardatabase.service.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class CarServiceTest {

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private CarService carService;

    private Car car;

    @BeforeEach
    void setUp() {
        car = new Car(1L, "Toyota", 2020, "Corolla", "Sedan");
    }

    @Test
    void createCar_ShouldReturnSavedCar() {
        when(carRepository.save(car)).thenReturn(car);
        Car savedCar = carService.createCar(car);
        assertNotNull(savedCar);
        assertEquals("Toyota", savedCar.getCarMake());
    }

    @Test
    void getCarById_ShouldReturnCar_WhenCarExists() {
        when(carRepository.findById(1L)).thenReturn(Optional.of(car));
        Optional<Car> foundCar = carService.getCarById(1L);
        assertTrue(foundCar.isPresent());
        assertEquals("Toyota", foundCar.get().getCarMake());
    }

    @Test
    void getCarById_ShouldReturnEmpty_WhenCarDoesNotExist() {
        when(carRepository.findById(1L)).thenReturn(Optional.empty());
        Optional<Car> foundCar = carService.getCarById(1L);
        assertFalse(foundCar.isPresent());
    }

    @Test
    void getAllCars_ShouldReturnPagedCars() {
        List<Car> cars = Arrays.asList(car);
        Page<Car> carPage = new PageImpl<>(cars);
        Pageable pageable = PageRequest.of(0, 10);
        when(carRepository.findAll(pageable)).thenReturn(carPage);

        Page<Car> result = carService.getAllCars(pageable);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void updateCar_ShouldUpdateCar_WhenCarExists() {
        Car updatedCar = new Car(1L, "Honda", 2021, "Civic", "Sedan");
        when(carRepository.findById(1L)).thenReturn(Optional.of(car));
        when(carRepository.save(any(Car.class))).thenReturn(updatedCar);

        Car result = carService.updateCar(1L, updatedCar);
        assertEquals("Honda", result.getCarMake());
    }

    @Test
    void updateCar_ShouldThrowException_WhenCarDoesNotExist() {
        Car updatedCar = new Car(1L, "Honda", 2021, "Civic", "Sedan");
        when(carRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CarNotFoundException.class, () -> carService.updateCar(1L, updatedCar));
    }

    @Test
    void deleteCar_ShouldDeleteCar_WhenCarExists() {
        when(carRepository.existsById(1L)).thenReturn(true);
        doNothing().when(carRepository).deleteById(1L);
        assertDoesNotThrow(() -> carService.deleteCar(1L));
    }

    @Test
    void deleteCar_ShouldThrowException_WhenCarDoesNotExist() {
        when(carRepository.existsById(1L)).thenReturn(false);
        assertThrows(CarNotFoundException.class, () -> carService.deleteCar(1L));
    }
}
