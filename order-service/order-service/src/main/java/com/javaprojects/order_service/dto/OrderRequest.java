package com.javaprojects.order_service.dto;

import java.util.List;

import com.javaprojects.order_service.model.OrderLineItemsDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    private List<OrderLineItemsDTO> orderLineItemsListDTO;
}
