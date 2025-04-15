package com.pizzademo.controller;

import com.pizzademo.dto.OrderDTO;
import com.pizzademo.model.Order;
import com.pizzademo.model.OrderItem;
import com.pizzademo.model.User;
import com.pizzademo.repository.OrderItemRepository;
import com.pizzademo.repository.OrderRepository;
import com.pizzademo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class OrderController {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        List<OrderDTO> orders = orderRepository.findAll().stream()
                .map(OrderDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderDTO>> getUserOrders(@PathVariable Long userId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = auth.getName();
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Ensure users can only see their own orders (unless they're admin)
        if (!user.getId().equals(userId) && !auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return ResponseEntity.forbidden().build();
        }

        List<OrderDTO> orders = orderRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(OrderDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = auth.getName();
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        // Ensure users can only see their own orders (unless they're admin)
        if (!order.getUser().getId().equals(user.getId()) && !auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return ResponseEntity.forbidden().build();
        }

        return ResponseEntity.ok(OrderDTO.fromEntity(order));
    }

    @PostMapping
    public ResponseEntity<?> createOrder(@Valid @RequestBody OrderDTO orderDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = auth.getName();
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Ensure users can only create orders for themselves
        if (!user.getId().equals(orderDTO.getUserId())) {
            return ResponseEntity.forbidden().build();
        }

        Order order = orderDTO.toEntity();
        order.setUser(user);
        order.setStatus(Order.OrderStatus.PENDING);

        Order savedOrder = orderRepository.save(order);

        // Save order items
        orderDTO.getItems().forEach(itemDTO -> {
            OrderItem item = itemDTO.toEntity();
            item.setOrder(savedOrder);
            orderItemRepository.save(item);
        });

        return ResponseEntity.ok(OrderDTO.fromEntity(savedOrder));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam Order.OrderStatus status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        order.setStatus(status);
        Order updatedOrder = orderRepository.save(order);
        return ResponseEntity.ok(OrderDTO.fromEntity(updatedOrder));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteOrder(@PathVariable Long id) {
        if (!orderRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        orderRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
