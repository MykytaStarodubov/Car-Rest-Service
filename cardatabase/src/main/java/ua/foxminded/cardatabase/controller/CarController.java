package ua.foxminded.cardatabase.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @Operation(summary = "Create a new car")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Car created successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Car.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content)
    })
    @PostMapping("/manufacturers/{manufacturer}/models/{model}/{year}")
    public ResponseEntity<Car> createCar(@PathVariable String manufacturer, @PathVariable String model,
                                         @PathVariable int year, @RequestBody Car car) {
        car.setCarMake(manufacturer);
        car.setCarModel(model);
        car.setCarYear(year);
        return new ResponseEntity<>(carService.createCar(car), HttpStatus.CREATED);
    }

    @Operation(summary = "Search cars based on filters")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of cars found",
                    content = {@Content(mediaType = "application/json")})
    })
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

    @Operation(summary = "Get a car by Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found car",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Car.class))}),
            @ApiResponse(responseCode = "404", description = "Car not found",
                    content = @Content)
    })
    @GetMapping("/cars/{id}")
    public ResponseEntity<Car> getCarById(@PathVariable Long id) {
        return carService.getCarById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Car not found"));
    }

    @Operation(summary = "Update an existing car")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Car updated successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Car.class))}),
            @ApiResponse(responseCode = "404", description = "Car not found",
                    content = @Content)
    })
    @PutMapping("/cars/{id}")
    public ResponseEntity<Car> updateCar(@PathVariable Long id, @RequestBody Car car) {
        try {
            return ResponseEntity.ok(carService.updateCar(id, car));
        } catch (CarNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @Operation(summary = "Delete a car by Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Car deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Car not found",
                    content = @Content)
    })
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
