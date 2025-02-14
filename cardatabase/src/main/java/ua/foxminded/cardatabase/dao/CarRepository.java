package ua.foxminded.cardatabase.dao;

import ua.foxminded.cardatabase.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {

    Page<Car> findByCarMakeIgnoreCase(String carMake, Pageable pageable);

    @Query("SELECT c FROM Car c WHERE " +
           "(:manufacturer IS NULL OR LOWER(c.carMake) = LOWER(:manufacturer)) AND " +
           "(:model IS NULL OR LOWER(c.carModel) = LOWER(:model)) AND " +
           "(:minYear IS NULL OR c.carYear >= :minYear) AND " +
           "(:maxYear IS NULL OR c.carYear <= :maxYear) AND " +
           "(:category IS NULL OR LOWER(c.carCategory) = LOWER(:category))")
    Page<Car> searchCars(
            @Param("manufacturer") String manufacturer,
            @Param("model") String model,
            @Param("minYear") Integer minYear,
            @Param("maxYear") Integer maxYear,
            @Param("category") String category,
            Pageable pageable);
}
