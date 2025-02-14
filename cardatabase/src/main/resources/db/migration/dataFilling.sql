CREATE TABLE cars (
    car_object_id SERIAL PRIMARY KEY,
    car_make VARCHAR(100) NOT NULL,
    car_year INT NOT NULL,
    car_model VARCHAR(100) NOT NULL,
    car_category VARCHAR(50) NOT NULL
);
