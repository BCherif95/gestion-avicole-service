package com.gestvicole.gestionavicole.services;

import com.gestvicole.gestionavicole.components.ParameterComponent;
import com.gestvicole.gestionavicole.entities.Order;
import com.gestvicole.gestionavicole.entities.Production;
import com.gestvicole.gestionavicole.repositories.OrderRepository;
import com.gestvicole.gestionavicole.repositories.ProductionRepository;
import com.gestvicole.gestionavicole.utils.Enumeration;
import com.gestvicole.gestionavicole.utils.ResponseBody;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ParameterComponent parameterComponent;
    private final ProductionRepository productionRepository;

    public OrderService(OrderRepository orderRepository,
                        ParameterComponent parameterComponent, ProductionRepository productionRepository) {
        this.orderRepository = orderRepository;
        this.parameterComponent = parameterComponent;
        this.productionRepository = productionRepository;
    }

    public Integer getQuantityAvailable() {

        int qteProd = 0;
        int qteOrder = 0;
        List<Production> productions = productionRepository.findAll();
        List<Order> orders = orderRepository.findAll();

        qteProd = productions
                .stream()
                .map(Production::getCommercialProductions)
                .reduce(0, Integer::sum);
        qteOrder = orders
                .stream()
                .map(Order::getQuantity)
                .reduce(0,Integer::sum);
        int qteAvailable = qteProd - qteOrder;
        return Math.max(qteAvailable, 0);
    }

    public ResponseBody findAll() {
        try {
            List<Order> list = orderRepository.findAll();
            list.sort(Collections.reverseOrder());
            return ResponseBody.with(list,"Liste de commandes disponibles");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBody.error("Une erreur est survenue");
        }
    }

    public ResponseBody getById(Long id) {
        try {
            Optional<Order> order = orderRepository.findById(id);
            return order.map(order1 -> ResponseBody.with(order1, "Commande recuperer avec succès")).orElseGet(() -> ResponseBody.error("Cette commande n'existe pas"));
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseBody.error("Une erreur est survenue");
        }
    }

    public ResponseBody findAllOrderWaiting() {
        try {
            return ResponseBody.with(orderRepository.findAllOrderByState(Enumeration.ORDER_STATE.WAITING),"Liste de commandes en cours");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBody.error("Une erreur est survenue!");
        }
    }

    public ResponseBody toOrder(Order order) {
        try {
            if (order.getQuantity() == 0) {
                return ResponseBody.error("La quantité ne peut pas etre vide !!!");
            }
            order.setNumber(parameterComponent.generateOrderNumber());
            order.setAmount(order.getQuantity()*order.getUnitPrice());
            orderRepository.save(order);
            parameterComponent.updateOrderNumber();
            return ResponseBody.with(order,"Commander avec succes");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBody.error("Une erreur est survenue");
        }
    }

    public ResponseBody edit(Order order) {
        try {
            if (!orderRepository.findById(order.getId()).isPresent() || order.getQuantity() == 0) {
                return ResponseBody.error("Une erreur est survenue");
            }
            return ResponseBody.with(orderRepository.save(order),"Modifier avec succes");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBody.error("Une erreur est survenue");
        }
    }
}
