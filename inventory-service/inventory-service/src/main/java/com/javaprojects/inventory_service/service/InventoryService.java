package com.javaprojects.inventory_service.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.javaprojects.inventory_service.dto.InventoryResponse;
import com.javaprojects.inventory_service.repository.InventoryRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    public List<InventoryResponse> isInStock(List<String> skuCode){
    	
    	log.info("wait started");
//    	try {
//			Thread.sleep(10000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
    	log.info("wait ended");
        return inventoryRepository.findBySkuCodeIn(skuCode).stream()
        .map(inventory ->
            InventoryResponse.builder()
            .skuCode(inventory.getSkuCode())
            .isInStock(inventory.getQunatity() > 0)
            .build()
        ).toList();

    }
}
