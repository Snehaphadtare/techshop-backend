package com.techshop.backend.service;

import com.techshop.backend.dto.PaymentRequest;
import com.techshop.backend.entity.*;
import com.techshop.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {

    @Autowired private OrderRepository orderRepository;
    @Autowired private OrderItemRepository orderItemRepository;
    @Autowired private CartItemRepository cartItemRepository;
    @Autowired private PaymentRepository paymentRepository;

    @Transactional
    public Order placeOrder(User user, PaymentRequest paymentRequest) {
        List<CartItem> cartItems = cartItemRepository.findByUser(user);

        if (cartItems.isEmpty())
            throw new RuntimeException("Cart is empty");

        // Calculate total
        double total = cartItems.stream()
                .mapToDouble(item -> {
                    Product p = item.getProduct();
                    double price = (p.getOnSale() && p.getSalePrice() != null)
                            ? p.getSalePrice() : p.getPrice();
                    return price * item.getQuantity();
                }).sum();

        // Create order
        Order order = new Order();
        order.setUser(user);
        order.setTotalAmount(total);
        order.setStatus("CONFIRMED");
        orderRepository.save(order);

        // Save order items
        for (CartItem cartItem : cartItems) {
            Product p = cartItem.getProduct();
            double price = (p.getOnSale() && p.getSalePrice() != null)
                    ? p.getSalePrice() : p.getPrice();

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(p);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPriceAtPurchase(price);
            orderItemRepository.save(orderItem);
        }

        // Save payment record
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setPaymentMethod(paymentRequest.getPaymentMethod());
        payment.setStatus(paymentRequest.getPaymentMethod().equals("COD") ? "PENDING" : "SUCCESS"); // simulated — always succeeds
        payment.setAmount(total);
        payment.setPaymentReference(paymentRequest.getPaymentReference());
        payment.setAddressLine(paymentRequest.getAddressLine());
        payment.setCity(paymentRequest.getCity());
        payment.setPincode(paymentRequest.getPincode());
        payment.setPhone(paymentRequest.getPhone());
        paymentRepository.save(payment);

        // Clear cart
        cartItemRepository.deleteByUser(user);

        return order;
    }

    public List<Order> getOrderHistory(User user) {
        return orderRepository.findByUserOrderByCreatedAtDesc(user);
    }

    public List<OrderItem> getOrderItems(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        return orderItemRepository.findByOrder(order);
    }

    public Payment getPaymentByOrderId(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        return paymentRepository.findByOrder(order)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
    }
}