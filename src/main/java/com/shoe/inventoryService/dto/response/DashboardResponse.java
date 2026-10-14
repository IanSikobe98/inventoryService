package com.shoe.inventoryService.dto.response;

import java.math.BigDecimal;
import java.util.List;

/**
 * Powers a dashboard screen: stock position plus sales performance for
 * today, this month, the last 7 days, and top sellers / payment mix.
 */
public record DashboardResponse(
        BigDecimal stockValueRetail,
        BigDecimal stockValueCost,
        long pairsInStock,
        long batchCount,
        long lowStockCount,
        long outOfStockCount,
        BigDecimal todayTotalSales,
        BigDecimal todayEstimatedProfit,
        BigDecimal monthTotalSales,
        List<DailyTrendPoint> last7Days,
        List<TopSeller> topSellers,
        List<PaymentBreakdown> todayPaymentBreakdown
) {
    public record DailyTrendPoint(String date, BigDecimal total) {
    }

    public record TopSeller(String shoeDescription, long quantitySold) {
    }

    public record PaymentBreakdown(String paymentModeName, BigDecimal total) {
    }
}
