package com.imran.shop.shop_management.service;

import com.imran.shop.shop_management.DTO.OrderItemRequest;
import com.imran.shop.shop_management.DTO.OrderRequest;
import com.imran.shop.shop_management.DTO.OrderResponse;
import com.imran.shop.shop_management.entity.Order;
import com.imran.shop.shop_management.entity.OrderItem;
import com.imran.shop.shop_management.entity.Product;
import com.imran.shop.shop_management.exception.UserNotFoundException;
import com.imran.shop.shop_management.repository.OrderRepository;
import com.imran.shop.shop_management.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepo;

    @Autowired
    private ProductRepository productRepo;

    public OrderResponse createOrder(OrderRequest req, String username) {

        Order order = new Order();
        order.setBillDate(LocalDateTime.now());
        order.setCreatedBy(username);

        double total = 0.0;
        List<OrderItem> orderItems = new ArrayList<>();

        for (OrderItemRequest itemReq : req.items()) {

            Product product = productRepo.findById(itemReq.productId())
                    .orElseThrow(() -> new UserNotFoundException("Product not found"));

            if (product.getStock() < itemReq.qty()) {
                throw new UserNotFoundException("Insufficient stock for: " + product.getName());
            }

            // Create order item
            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setQuantity(itemReq.qty());
            item.setPrice(product.getPrice());

            orderItems.add(item);

            // calculate total
            total += product.getPrice() * itemReq.qty();

            // deduct stock
            product.setStock(product.getStock() - itemReq.qty());
            productRepo.save(product);
        }

        // apply discount if any
        if (req.discount() != null && req.discount() > 0) {
            total = total - req.discount();
        }

        order.setTotalAmount(total);
        order.setItems(orderItems);

        Order saved = orderRepo.save(order);

        return new OrderResponse(
                saved.getId(),
                saved.getTotalAmount(),
                saved.getBillDate(),
                saved.getCreatedBy()
        );
    }
}

