package com.foodbites.truck_bites;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal de la aplicación Truck_bites.
 * Inicia el contexto de Spring Boot y escanea los componentes en el paquete com.foodbites.truck_bites.
 */
@SpringBootApplication
public class TruckBitesApplication {

    public static void main(String[] args) {
        SpringApplication.run(TruckBitesApplication.class, args);
    }
}
