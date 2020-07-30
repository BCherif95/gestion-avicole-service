package com.gestvicole.gestionavicole.services;

import com.gestvicole.gestionavicole.entities.Customer;
import com.gestvicole.gestionavicole.entities.Order;
import com.gestvicole.gestionavicole.repositories.CustomerRepository;
import com.gestvicole.gestionavicole.repositories.OrderRepository;
import com.gestvicole.gestionavicole.utils.ResponseBody;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;

    public CustomerService(CustomerRepository customerRepository, OrderRepository orderRepository) {
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
    }

    public ResponseBody findAll() {
        try {
            return ResponseBody.with(customerRepository.findAll(),"Liste de clients disponible");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBody.error("Une erreur est survenue");
        }
    }

    public ResponseBody getCustomer(Long id) {
        try {
            Optional<Customer> customer = customerRepository.findById(id);
            return customer.map(customer1 -> ResponseBody.with(customer1, "Client recuperer avec succès")).orElseGet(() -> ResponseBody.error("Ce client n'existe pas"));
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseBody.error("Une erreur est survenue");
        }
    }

    public ResponseBody create(Customer customer) {
        try {
            if (customerRepository.existsByName(customer.getName())){
                return ResponseBody.error("Ce nom existe déjà!");
            }
            customerRepository.save(customer);
            return ResponseBody.with(customer, "Ajouter avec succes!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBody.error("Ce nom existe deja!");
        }
    }

    public ResponseBody edit(Customer customer) {
       try {
           boolean isExist = customerRepository.distinctByIdAndExistByName(customer.getId(), customer.getName()).isEmpty();
           if (!isExist) {
               return ResponseBody.error("Ce nom existe deja!");
           }
           customerRepository.save(customer);
           return ResponseBody.with(customer, "Modifier avec succes!");

       } catch (Exception e) {
           e.printStackTrace();
           return ResponseBody.error("Ce nom existe deja!");
       }
    }

    public ResponseBody deleteById(Long id) {
        try {
            if (customerRepository.findById(id).isPresent()) {
                List<Order> orders = orderRepository.findAllByCustomerId(id);
                if (orders.isEmpty()) {
                    customerRepository.deleteById(id);
                    return ResponseBody.success("Suppression avec succes");
                }else {
                    return ResponseBody.error("Ce client possede des commandes donc on ne peut pas le supprimer");
                }
            }
            return ResponseBody.error("Ce client n'existe pas");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBody.error("Une erreur est servenue");
        }
    }
}
