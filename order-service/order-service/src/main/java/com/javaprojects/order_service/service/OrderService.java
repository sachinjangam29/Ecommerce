package com.javaprojects.order_service.service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.javaprojects.order_service.dto.InventoryResponse;
import com.javaprojects.order_service.dto.OrderRequest;
import com.javaprojects.order_service.model.Order;
import com.javaprojects.order_service.model.OrderLineItems;
import com.javaprojects.order_service.model.OrderLineItemsDTO;
import com.javaprojects.order_service.repository.OrderRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient.Builder webCleintBuilder;
    
    public String placeOrder(OrderRequest orderRequest){
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItems =  orderRequest.getOrderLineItemsListDTO().stream()
        .map(orderLineItemsDTO -> mapToDTO(orderLineItemsDTO))
        .collect(Collectors.toList());

        order.setOrderLineItemsList(orderLineItems);

        // We need to check 1st if the order is present in the inventory then only accordingly process  the order first.

      List<String> skuCodes =  order.getOrderLineItemsList().stream()
        .map(orderLineItem -> orderLineItem.getSkuCode())
        .toList();

        InventoryResponse[] inventoryResponses = webCleintBuilder.build().get()
                        .uri("http://inventory-service/api/inventory", 
                        uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                        .retrieve()
                        .bodyToMono(InventoryResponse[].class)
                        .block();

            boolean allProductsInStock = Arrays.stream(inventoryResponses)
                                        .allMatch(inventoryResponse -> inventoryResponse.isInStock());

        if(allProductsInStock){
            orderRepository.save(order);
            return "Orders placed successfully";
        } else{
            throw new IllegalArgumentException("Product is not in the Stock, please try later");
        }
    }

    private OrderLineItems mapToDTO(OrderLineItemsDTO orderLineItemsDto){
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItems;
    }
}
