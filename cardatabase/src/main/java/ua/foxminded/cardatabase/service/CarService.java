package ua.foxminded.cardatabase.service;

import ua.foxminded.cardatabase.model.Car;
import ua.foxminded.cardatabase.dao.CarRepository;
import ua.foxminded.cardatabase.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CarService {

    @Autowired
    private CarRepository carRepository;

    public Car createCar(Car car) {
        return carRepository.save(car);
    }

    public Optional<Car> getCarById(Long id) {
        return carRepository.findById(id);
    }

    public Page<Car> getAllCars(Pageable pageable) {
        return carRepository.findAll(pageable);
    }

    public Car updateCar(Long id, Car updatedCar) {
        return carRepository.findById(id).map(car -> {
            car.setCarMake(updatedCar.getCarMake());
            car.setCarYear(updatedCar.getCarYear());
            car.setCarModel(updatedCar.getCarModel());
            car.setCarCategory(updatedCar.getCarCategory());
            return carRepository.save(car);
        }).orElseThrow(() -> new CarNotFoundException("Car with ID " + id + " not found"));
    }

    public void deleteCar(Long id) {
        if (!carRepository.existsById(id)) {
            throw new CarNotFoundException("Car with ID " + id + " not found");
        }
        carRepository.deleteById(id);
    }

    public Page<Car> searchCars(String manufacturer, String model, Integer minYear, Integer maxYear, String category, Pageable pageable) {
        return carRepository.searchCars(manufacturer, model, minYear, maxYear, category, pageable);
    }
}
