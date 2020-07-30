package com.gestvicole.gestionavicole.services;

import com.gestvicole.gestionavicole.repositories.StockRepository;
import com.gestvicole.gestionavicole.utils.ResponseBody;
import org.springframework.stereotype.Service;

@Service
public class StockService {

    private final StockRepository stockRepository;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public ResponseBody getAll() {
        try {
            return ResponseBody.with(stockRepository.findAll(),"La liste des produits en stocks");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBody.error("Une erreur est survenue !");
        }
    }
}
