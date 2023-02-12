CREATE TABLE person
(
    personId       SERIAL PRIMARY KEY,
    name           VARCHAR(20) NOT NULL,
    age            INTEGER CHECK ( age > 0 ),
    carId          INTEGER     NOT NULL,
    driverIdExists BOOLEAN     NOT NULL
);

CREATE TABLE car
(
    carId    SERIAL PRIMARY KEY,
    brand    VARCHAR(20) NOT NULL,
    model    VARCHAR(20) NOT NULL,
    price    NUMERIC CHECK ( price > 0 ),
    personId INTEGER
);


SELECT person.name,
       person.age,
       person.carId,
       person.driverIdExists,
       car.brand,
       car.model,
       car.price
FROM person
         INNER JOIN car ON person.carId = car.carId;

SELECT car.brand,
       car.model,
       car.price,
       car.personId,
       person.name,
       person.age,
       person.driverIdExists
FROM car
         INNER JOIN person ON car.personId = person.personId;


