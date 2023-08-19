package com.example.stock.facade;

import com.example.stock.repository.RedisLockRepository;
import com.example.stock.service.StockService;
import org.springframework.stereotype.Component;

@Component
public class LettuceLockStockFacade {
    private final RedisLockRepository repository;
    private final StockService stockService;

    public LettuceLockStockFacade(RedisLockRepository repository, StockService stockService) {
        this.repository = repository;
        this.stockService = stockService;
    }

    public void decrease(Long id, Long quantity) throws InterruptedException {
        while (!repository.lock(id)) {
            Thread.sleep(100);
        }

        try {
            stockService.decrease(id, quantity);
        } finally {
            repository.unlock(id);
        }
    }
}
