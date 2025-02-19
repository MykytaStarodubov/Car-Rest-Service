package ua.foxminded.cardatabase;

import com.fasterxml.jackson.databind.ObjectMapper;
import ua.foxminded.cardatabase.model.Car;
import ua.foxminded.cardatabase.controller.*;
import ua.foxminded.cardatabase.service.CarService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CarController.class)
class CarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CarService carService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createCar_ShouldReturnCreatedCar() throws Exception {
        Car car = new Car(1L, "Toyota", 2021, "Corolla", "Sedan");

        Mockito.when(carService.createCar(Mockito.any(Car.class))).thenReturn(car);

        mockMvc.perform(post("/api/v1/manufacturers/Toyota/models/Corolla/2021")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(car)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.carMake").value("Toyota"))
                .andExpect(jsonPath("$.carModel").value("Corolla"))
                .andExpect(jsonPath("$.carYear").value(2021))
                .andExpect(jsonPath("$.carCategory").value("Sedan"));
    }

    @Test
    void updateCar_ShouldReturnUpdatedCar() throws Exception {
        Long carId = 1L;
        Car updatedCar = new Car(carId, "Honda", 2022, "Civic", "Sedan");

        Mockito.when(carService.updateCar(Mockito.eq(carId), Mockito.any(Car.class)))
                .thenReturn(updatedCar);

        mockMvc.perform(put("/api/v1/cars/{id}", carId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedCar)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.carMake").value("Honda"))
                .andExpect(jsonPath("$.carModel").value("Civic"))
                .andExpect(jsonPath("$.carYear").value(2022))
                .andExpect(jsonPath("$.carCategory").value("Sedan"));
    }

    @Test
    void deleteCar_ShouldReturnNoContent() throws Exception {
        Long carId = 1L;

        Mockito.doNothing().when(carService).deleteCar(carId);

        mockMvc.perform(delete("/api/v1/cars/{id}", carId))
                .andExpect(status().isNoContent());
    }
}
