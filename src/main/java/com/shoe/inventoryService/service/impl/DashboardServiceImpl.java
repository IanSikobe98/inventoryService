package com.shoe.inventoryService.service.impl;

import com.shoe.inventoryService.dto.response.DashboardResponse;
import com.shoe.inventoryService.entity.Sale;
import com.shoe.inventoryService.entity.ShoeBatch;
import com.shoe.inventoryService.mapper.ShoeBatchMapper;
import com.shoe.inventoryService.repository.SaleRepository;
import com.shoe.inventoryService.repository.ShoeBatchRepository;
import com.shoe.inventoryService.service.DashboardService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {

    private final ShoeBatchRepository shoeBatchRepository;
    private final SaleRepository saleRepository;

    public DashboardServiceImpl(ShoeBatchRepository shoeBatchRepository, SaleRepository saleRepository) {
        this.shoeBatchRepository = shoeBatchRepository;
        this.saleRepository = saleRepository;
    }

    @Override
    public DashboardResponse getDashboard() {
        List<ShoeBatch> batches = shoeBatchRepository.findAll();

        BigDecimal stockValueRetail = BigDecimal.ZERO;
        BigDecimal stockValueCost = BigDecimal.ZERO;
        long pairsInStock = 0;
        long lowStockCount = 0;
        long outOfStockCount = 0;

        for (ShoeBatch b : batches) {
            int remaining = b.getQuantityRemaining() == null ? 0 : b.getQuantityRemaining();
            stockValueRetail = stockValueRetail.add(nullSafe(b.getRetailPrice()).multiply(BigDecimal.valueOf(remaining)));
            stockValueCost = stockValueCost.add(nullSafe(b.getCostPerShoe()).multiply(BigDecimal.valueOf(remaining)));
            pairsInStock += remaining;
            if (remaining <= 0) {
                outOfStockCount++;
            } else if (remaining <= ShoeBatchMapper.LOW_STOCK_THRESHOLD) {
                lowStockCount++;
            }
        }

        LocalDate today = LocalDate.now();
        LocalDateTime todayStart = today.atStartOfDay();
        LocalDateTime todayEnd = today.plusDays(1).atStartOfDay();
        List<Sale> todaySales = saleRepository.findBetween(todayStart, todayEnd);

        BigDecimal todayTotal = todaySales.stream()
                .map(Sale::getTotalAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal todayProfit = todaySales.stream()
                .map(s -> s.getActualSellingPrice()
                        .subtract(nullSafe(s.getShoeBatch().getCostPerShoe()))
                        .multiply(BigDecimal.valueOf(s.getQuantitySold())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        LocalDate monthStart = today.withDayOfMonth(1);
        List<Sale> monthSales = saleRepository.findBetween(monthStart.atStartOfDay(), today.plusDays(1).atStartOfDay());
        BigDecimal monthTotal = monthSales.stream()
                .map(Sale::getTotalAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

        List<DashboardResponse.DailyTrendPoint> last7 = last7Days(today);
        List<DashboardResponse.TopSeller> topSellers = topSellers(today);
        List<DashboardResponse.PaymentBreakdown> paymentBreakdown = paymentBreakdown(todaySales);

        return new DashboardResponse(
                stockValueRetail,
                stockValueCost,
                pairsInStock,
                batches.size(),
                lowStockCount,
                outOfStockCount,
                todayTotal,
                todayProfit,
                monthTotal,
                last7,
                topSellers,
                paymentBreakdown
        );
    }

    private List<DashboardResponse.DailyTrendPoint> last7Days(LocalDate today) {
        LocalDate from = today.minusDays(6);
        List<Sale> sales = saleRepository.findBetween(from.atStartOfDay(), today.plusDays(1).atStartOfDay());
        Map<LocalDate, BigDecimal> byDay = sales.stream()
                .collect(Collectors.groupingBy(
                        s -> s.getCreatedAt().toLocalDate(),
                        Collectors.reducing(BigDecimal.ZERO, Sale::getTotalAmount, BigDecimal::add)
                ));

        return from.datesUntil(today.plusDays(1))
                .map(d -> new DashboardResponse.DailyTrendPoint(
                        d.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH) + " " + d,
                        byDay.getOrDefault(d, BigDecimal.ZERO)))
                .toList();
    }

    private List<DashboardResponse.TopSeller> topSellers(LocalDate today) {
        LocalDate from = today.minusDays(30);
        List<Sale> sales = saleRepository.findBetween(from.atStartOfDay(), today.plusDays(1).atStartOfDay());

        Map<String, Long> byShoe = sales.stream().collect(Collectors.groupingBy(
                s -> s.getShoeBatch().getShoeCode() + " · " + s.getShoeBatch().getType() + " · " + s.getShoeBatch().getColour(),
                Collectors.summingLong(Sale::getQuantitySold)
        ));

        return byShoe.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(5)
                .map(e -> new DashboardResponse.TopSeller(e.getKey(), e.getValue()))
                .toList();
    }

    private List<DashboardResponse.PaymentBreakdown> paymentBreakdown(List<Sale> todaySales) {
        Map<String, BigDecimal> byMode = todaySales.stream().collect(Collectors.groupingBy(
                s -> s.getPaymentMode().getName(),
                Collectors.reducing(BigDecimal.ZERO, Sale::getTotalAmount, BigDecimal::add)
        ));
        return byMode.entrySet().stream()
                .sorted(Comparator.comparing((Map.Entry<String, BigDecimal> e) -> e.getValue()).reversed())
                .map(e -> new DashboardResponse.PaymentBreakdown(e.getKey(), e.getValue()))
                .toList();
    }

    private BigDecimal nullSafe(BigDecimal v) {
        return v == null ? BigDecimal.ZERO : v;
    }
}
