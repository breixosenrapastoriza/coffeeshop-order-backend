package com.coffeeshop.coffeeshop_order_backend;

import com.coffeeshop.coffeeshop_order_backend.dto.ProductDto;
import com.coffeeshop.coffeeshop_order_backend.model.*;
import com.coffeeshop.coffeeshop_order_backend.model.enums.CoffeeType;
import com.coffeeshop.coffeeshop_order_backend.model.enums.Fruit;
import com.coffeeshop.coffeeshop_order_backend.model.enums.OrderStatus;
import com.coffeeshop.coffeeshop_order_backend.model.enums.TeaType;
import com.coffeeshop.coffeeshop_order_backend.service.OrderItemService;
import com.coffeeshop.coffeeshop_order_backend.service.OrderService;
import com.coffeeshop.coffeeshop_order_backend.service.ProductService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class CoffeeshopOrderBackendApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(CoffeeshopOrderBackendApplication.class, args);

        // Get ProductService from Spring context
        ProductService productService = context.getBean(ProductService.class);
		OrderService orderService = context.getBean(OrderService.class);
		OrderItemService orderItemService = context.getBean(OrderItemService.class);

        // Create some test products
        Product coffee1 = Coffee.builder()
                .name("Latte")
                .description("Espresso with steamed milk")
                .price(3.5)
                .coffeeType(CoffeeType.LATTE)
                .build();

        Product coffee2 = Coffee.builder()
                .name("Cappuccino")
                .description("Espresso with frothed milk")
                .price(4.0)
                .coffeeType(CoffeeType.CAPPUCCINO)
                .build();

        Product tea = Tea.builder()
                .name("Green Tea")
                .description("Fresh brewed green tea")
                .price(2.5)
				.teaType(TeaType.GREEN)
                .build();

        // Create smoothie examples
        Product smoothie1 = Smoothie.builder()
                .name("Tropical Smoothie")
                .description("Refreshing tropical blend")
                .price(5.5)
                .fruits(List.of(Fruit.MANGO, Fruit.PINEAPPLE, Fruit.BANANA))
                .hasYogurt(true)
                .build();

        Product smoothie2 = Smoothie.builder()
                .name("Berry Blast")
                .description("Mixed berries smoothie")
                .price(6.0)
                .fruits(List.of(Fruit.STRAWBERRY, Fruit.BLUEBERRY, Fruit.RASPBERRY))
                .hasMilk(true)
                .build();

        // Save the products
        System.out.println("\n=== Creating products ===");
        productService.save(coffee1);
        productService.save(coffee2);
        productService.save(tea);
        productService.save(smoothie1);
        productService.save(smoothie2);

        // Show saved products
        System.out.println("\n=== Saved products ===");
        System.out.println(coffee1);
        System.out.println(coffee2);
        System.out.println(tea);
        System.out.println(smoothie1);
        System.out.println(smoothie2);

        // Get all products
        System.out.println("\n=== All products ===");
        productService.findAll().forEach(System.out::println);

        // Create an order example
        System.out.println("\n=== Creating an order ===");
        Order order = Order.builder()
                .customerName("John Doe")
                .status(OrderStatus.IN_PROGRESS)
                .orderDate(LocalDateTime.now())
                .build();

        // Add items to the order
        order.addItem(coffee1, 2); // 2 Lattes
        order.addItem(coffee2, 1); // 1 Cappuccino
        order.addItem(tea, 3); // 3 Green Teas
        order.addItem(smoothie1, 1); // 1 Tropical Smoothie
        order.addItem(smoothie2, 2); // 2 Berry Blast

        // Save the order
        System.out.println("\n=== Saving order ===");
        orderService.save(order);
        System.out.println(order);

        // Update an order
        System.out.println("\n=== Updating order ===");
        order.setStatus(OrderStatus.COMPLETED);
        Order updatedOrder = orderService.save(order);
        System.out.println(updatedOrder);

        // Get all orders
        System.out.println("\n=== All orders ===");
        orderService.findAll().forEach(System.out::println);

        System.out.println("\n=== Fin de la prueba ===");
        System.out.println("Breixo Senra Pastoriza");
    }
}
