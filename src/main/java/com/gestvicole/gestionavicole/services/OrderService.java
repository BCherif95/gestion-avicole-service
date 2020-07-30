package com.gestvicole.gestionavicole.services;

import com.gestvicole.gestionavicole.components.ParameterComponent;
import com.gestvicole.gestionavicole.entities.Order;
import com.gestvicole.gestionavicole.entities.Production;
import com.gestvicole.gestionavicole.repositories.OrderRepository;
import com.gestvicole.gestionavicole.repositories.ProductionRepository;
import com.gestvicole.gestionavicole.utils.Enumeration;
import com.gestvicole.gestionavicole.utils.ResponseBody;
import org.springframework.stereotype.Service;

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

    public ResponseBody findAll() {
        try {
            return ResponseBody.with(orderRepository.findAll(),"Liste de commandes disponibles");
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

    public ResponseBody create(Order order) {
        try {
            if (order.getQuantity() == 0) {
                return ResponseBody.error("La quantité ne peut etre vide");
            }
            Production production = productionRepository.findById(order.getProduction().getId()).get();
            Integer newQte = production.getCommercialProductions() - order.getQuantity();
            production.setCommercialProductions(newQte);
            productionRepository.save(production);
            order.setNumber(parameterComponent.generateOrderNumber());
            order.setAmount(order.getQuantity()*order.getUnitPrice());
            orderRepository.save(order);
            parameterComponent.updateOrderNumber();
            return ResponseBody.with(order,"Ajouter avec succes");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBody.error("Une erreur est survenue");
        }
    }

    public ResponseBody edit(Order order) {
        try {
            if (orderRepository.findById(order.getId()).isPresent() && order.getQuantity() != 0) {
                Optional<Production> production = productionRepository.findById(order.getProduction().getId());
                if (production.isPresent()){
                    Production prod = production.get();
                    Integer newQte = prod.getCommercialProductions() - order.getQuantity();
                    prod.setCommercialProductions(newQte);
                    productionRepository.save(prod);
                }
                order.setAmount(order.getQuantity()*order.getUnitPrice());
                orderRepository.save(order);
                return ResponseBody.with(order,"Modifier avec succes");
            }
            return ResponseBody.error("Une erreur est survenue");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBody.error("Une erreur est survenue");
        }
    }
}
