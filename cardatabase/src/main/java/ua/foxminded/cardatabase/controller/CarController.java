package ua.foxminded.cardatabase.controller;

import ua.foxminded.cardatabase.model.Car;
import ua.foxminded.cardatabase.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class CarController {

    private final CarService carService;

    @Autowired
    public CarController(CarService carService) {
        this.carService = carService;
    }

    @PostMapping("/manufacturers/{manufacturer}/models/{model}/{year}")
    public ResponseEntity<Car> createCar(@PathVariable String manufacturer, @PathVariable String model,
                                         @PathVariable int year, @RequestBody Car car) {
        car.setCarMake(manufacturer);
        car.setCarModel(model);
        car.setCarYear(year);
        return ResponseEntity.ok(carService.createCar(car));
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

    @PutMapping("/cars/{id}")
    public ResponseEntity<Car> updateCar(@PathVariable Long id, @RequestBody Car car) {
        return ResponseEntity.ok(carService.updateCar(id, car));
    }

    @DeleteMapping("/cars/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable Long id) {
        carService.deleteCar(id);
        return ResponseEntity.noContent().build();
    }
}
