package com.gestvicole.gestionavicole.services;

import com.gestvicole.gestionavicole.entities.Stock;
import com.gestvicole.gestionavicole.repositories.StockRepository;
import com.gestvicole.gestionavicole.utils.ResponseBody;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class StockService {

    private final StockRepository stockRepository;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public ResponseBody getAll() {
        try {
            List<Stock> list = stockRepository.findAll();
            list.sort(Collections.reverseOrder());
            return ResponseBody.with(list,"La liste des produits en stocks");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBody.error("Une erreur est survenue !");
        }
    }
}
