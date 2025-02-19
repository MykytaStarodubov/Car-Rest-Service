package ua.foxminded.cardatabase.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ua.foxminded.cardatabase.model.Car;
import ua.foxminded.cardatabase.service.CarService;
import ua.foxminded.cardatabase.exception.CarNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/api/v1")
public class CarController {

    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @PostMapping("/manufacturers/{manufacturer}/models/{model}/{year}")
    public ResponseEntity<Car> createCar(@PathVariable String manufacturer, @PathVariable String model,
                                         @PathVariable int year, @RequestBody Car car) {
        car.setCarMake(manufacturer);
        car.setCarModel(model);
        car.setCarYear(year);
        return new ResponseEntity<>(carService.createCar(car), HttpStatus.CREATED);
    }

    @GetMapping("/cars")
    public ResponseEntity<Page<Car>> searchCars(
            @RequestParam(required = false) String manufacturer,
            @RequestParam(required = false) String model,
            @RequestParam(required = false) Integer minYear,
            @RequestParam(required = false) Integer maxYear,
            @RequestParam(required = false) String category,
            Pageable pageable) {
        return ResponseEntity.ok(carService.searchCars(manufacturer, model, minYear, maxYear, category, pageable));
    }

    @GetMapping("/cars/{id}")
    public ResponseEntity<Car> getCarById(@PathVariable Long id) {
        return carService.getCarById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Car not found"));
    }

    @PutMapping("/cars/{id}")
    public ResponseEntity<Car> updateCar(@PathVariable Long id, @RequestBody Car car) {
        try {
            return ResponseEntity.ok(carService.updateCar(id, car));
        } catch (CarNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @DeleteMapping("/cars/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable Long id) {
        try {
            carService.deleteCar(id);
            return ResponseEntity.noContent().build();
        } catch (CarNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
