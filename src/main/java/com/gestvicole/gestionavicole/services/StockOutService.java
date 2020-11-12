package com.gestvicole.gestionavicole.services;

import com.gestvicole.gestionavicole.entities.Stock;
import com.gestvicole.gestionavicole.entities.StockOut;
import com.gestvicole.gestionavicole.repositories.StockOutRepository;
import com.gestvicole.gestionavicole.repositories.StockRepository;
import com.gestvicole.gestionavicole.utils.Enumeration;
import com.gestvicole.gestionavicole.utils.ResponseBody;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class StockOutService {
    private final StockOutRepository stockOutRepository;
    private final StockRepository stockRepository;

    public StockOutService(StockOutRepository stockOutRepository, StockRepository stockRepository) {
        this.stockOutRepository = stockOutRepository;
        this.stockRepository = stockRepository;
    }


    public ResponseBody create(StockOut stockOut) {
        try {
            Optional<Stock> stockOptional= stockRepository.checkBalance(stockOut.getProduct().getId(),stockOut.getQuantityOut());
            if (stockOptional.isPresent()){
                Optional<StockOut> optionalStockOut = stockOutRepository.findByDateAndProductId(stockOut.getDate(),stockOut.getProduct().getId());
                if (optionalStockOut.isPresent()) {
                    StockOut stockOut1 = optionalStockOut.get();
                    stockOut1.setState(Enumeration.STOCK_OUT_STATE.WAITING);
                    stockOut1.setQuantityOut(stockOut1.getQuantityOut()+stockOut.getQuantityOut());
                    this.stockOutRepository.save(stockOut1);
                    return ResponseBody.with(stockOut1,"Ajouter avec succés !");
                }
                stockOut.setState(Enumeration.STOCK_OUT_STATE.WAITING);
                StockOut newStock = stockOutRepository.save(stockOut);
                return ResponseBody.with(newStock,"Ajouter avec succés !");
            }else {
                return ResponseBody.error("La quantité demandée est superieur à la quantité restante !");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBody.error("Une erreur est survenue !");
        }
    }

    public ResponseBody validateStockOut(StockOut stockOut) {
        Optional<Stock> stockOptional = stockRepository.findByProductId(stockOut.getProduct().getId());
        if (stockOptional.isPresent()) {
            stockOut.setState(Enumeration.STOCK_OUT_STATE.VALIDATE);
            StockOut newStockOut = stockOutRepository.save(stockOut);
            Stock stock1 = stockOptional.get();
            List<StockOut> stockOuts = stockOutRepository.findAllByProductId(stock1.getProduct().getId());
            stock1.setDate(new Date());
            /*stock1.setQuantityOut(stockOuts
                    .stream()
                    .map(StockOut::getQuantityOut)
                    .reduce(0,Integer::sum));*/
            stock1.setQuantityOut(
                    Optional.of(stockOuts
                            .stream()
                            .map(out -> out.getQuantityOut().toString())
                            .mapToDouble(Double::parseDouble)
                            .sum())
                            .orElse(0.0)
            );
            stock1.setSolde(stock1.getQuantityEntry() - stock1.getQuantityOut());
            stockRepository.save(stock1);
            return ResponseBody.with(newStockOut,"Sortie valider avec succès !");
        }else {
            return ResponseBody.error("Une erreur est survenue !!");
        }
    }

    public ResponseBody findAll() {
        try {
            return ResponseBody.with(stockOutRepository.findAll(),"La liste des sorties de stock");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBody.error("Une erreur est survenue !");
        }
    }
}
