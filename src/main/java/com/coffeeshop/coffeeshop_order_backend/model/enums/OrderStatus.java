package com.coffeeshop.coffeeshop_order_backend.model.enums;

public enum OrderStatus {
    IN_QUEUE,       // En cola
    IN_PROGRESS,    // Preparándose
    READY,          // Listo para recoger
    COMPLETED,      // Recogido/Entregado
    CANCELLED       // Cancelado
}
