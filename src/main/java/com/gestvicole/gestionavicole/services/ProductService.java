package com.gestvicole.gestionavicole.services;
import com.gestvicole.gestionavicole.entities.Product;
import com.gestvicole.gestionavicole.repositories.ProductRepository;
import com.gestvicole.gestionavicole.utils.ResponseBody;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ResponseBody getAll() {
        try {
            List<Product> list = productRepository.findAll();
            list.sort(Collections.reverseOrder());
            return ResponseBody.with(list,"Liste de produits");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBody.error("Une erreur est survenue");
        }
    }

    public ResponseBody getById(Long id) {
        try {
            Optional<Product> productOptional = productRepository.findById(id);
            return productOptional.map(product -> ResponseBody.with(product, "Recuperer avec succès"))
                    .orElseGet(() -> ResponseBody.error("Ce produit n'existe pas"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBody.error("Une erreur est survenue");
        }
    }

    public ResponseBody save(Product product) {
        try {
            if (productRepository.existsByDesignation(product.getDesignation())) {
                return ResponseBody.error("Ce produit existe déjà !");
            }
            return ResponseBody.with(productRepository.save(product),"Ajouter avec succès !");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBody.error("Une erreur est survenue");
        }
    }

    public ResponseBody edit(Product product) {
        try {
            boolean isExist = productRepository.distinctByIdExistAndDescription(product.getId(), product.getDesignation()).isEmpty();
            if (!isExist) {
                return ResponseBody.error("Ce nom existe deja!");
            }
            productRepository.save(product);
            return ResponseBody.with(product, "Modifier avec succes!");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBody.error("Ce nom existe deja!");
        }
    }
}
