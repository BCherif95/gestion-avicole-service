package com.gestvicole.gestionavicole.services;

import com.gestvicole.gestionavicole.entities.Stock;
import com.gestvicole.gestionavicole.entities.StockEntry;
import com.gestvicole.gestionavicole.repositories.StockEntryRepository;
import com.gestvicole.gestionavicole.repositories.StockRepository;
import com.gestvicole.gestionavicole.utils.ResponseBody;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class StockEntryService {

    private final StockEntryRepository stockEntryRepository;
    private final StockRepository stockRepository;

    public StockEntryService(StockEntryRepository stockEntryRepository, StockRepository stockRepository) {
        this.stockEntryRepository = stockEntryRepository;
        this.stockRepository = stockRepository;
    }

    public ResponseBody create(StockEntry stockEntry) {
        try {
            Optional<StockEntry> optionalStockEntry = stockEntryRepository.findByDateAndProductId(stockEntry.getDate(),stockEntry.getProduct().getId());
            if (optionalStockEntry.isPresent()) {
                StockEntry entry = optionalStockEntry.get();
                entry.setQuantityEntry(entry.getQuantityEntry() + stockEntry.getQuantityEntry());
                StockEntry newStockEntry = stockEntryRepository.saveAndFlush(entry);
                saveStock(newStockEntry);
                return ResponseBody.with(entry,"Entrée mise a jour avec succès !");
            }
            StockEntry stockEntry1 = stockEntryRepository.saveAndFlush(stockEntry);
            saveStock(stockEntry1);
            return ResponseBody.with(stockEntry1,"Entrée ajoutée avec succès !");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBody.error("Une erreur est survenue");
        }
    }

    private void saveStock(StockEntry stockEntry) {
        Optional<Stock> stockOptional = stockRepository.findByProductId(stockEntry.getProduct().getId());
        if (stockOptional.isPresent()) {
            Stock stock1 = stockOptional.get();
            List<StockEntry> stockEntries = stockEntryRepository.findAllByProductId(stock1.getProduct().getId());
            stock1.setDate(new Date());
            stock1.setQuantityEntry(stockEntries
                    .stream()
                    .map(StockEntry::getQuantityEntry)
                    .reduce(0,Integer::sum));
            stock1.setSolde(stock1.getQuantityEntry() - stock1.getQuantityOut());
            stockRepository.save(stock1);;
        }else {
            Stock stock = new Stock();
            stock.setDate(new Date());
            stock.setProduct(stockEntry.getProduct());
            stock.setQuantityEntry(stockEntry.getQuantityEntry());
            stock.setQuantityOut(0);
            stock.setSolde(stock.getQuantityEntry());
            stockRepository.save(stock);
        }

    }

    public ResponseBody findAll() {
        try {
            return ResponseBody.with(stockEntryRepository.findAll(),"La liste des entrées de stock");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBody.error("Une erreur est survenue !");
        }
    }
}
