package com.javaprojects.inventory_service;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

import com.javaprojects.inventory_service.model.Inventory;
import com.javaprojects.inventory_service.repository.InventoryRepository;

@SpringBootApplication
@EnableDiscoveryClient
public class InventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}
	@Bean
	public CommandLineRunner loadData(InventoryRepository inventoryRepository){
		return args -> {
				Inventory inventory = new Inventory();
				inventory.setSkuCode("iPhone_13");
				inventory.setQunatity(100);


				Inventory inventory2 = new Inventory();
				inventory2.setSkuCode("iPhone_13_red");
				inventory2.setQunatity(0);

				inventoryRepository.save(inventory);
				inventoryRepository.save(inventory2);
			};
	}

}
