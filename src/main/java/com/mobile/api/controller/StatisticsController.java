package com.mobile.api.controller;

import com.mobile.api.controller.base.BaseController;
import com.mobile.api.dto.ApiMessageDto;
import com.mobile.api.dto.bill.BillDetailStatisticsDto;
import com.mobile.api.dto.category.CategoryDetailStatisticsDto;
import com.mobile.api.model.criteria.BillCriteria;
import com.mobile.api.model.criteria.CategoryCriteria;
import com.mobile.api.service.BillStatisticsService;
import com.mobile.api.service.CategoryStatisticsService;
import com.mobile.api.utils.ApiMessageUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/api/v1/statistics")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class StatisticsController extends BaseController {
    @Autowired
    private BillStatisticsService billStatisticsService;
    @Autowired
    private CategoryStatisticsService categoryStatisticsService;

    @GetMapping(value = "/bill-detail", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<BillDetailStatisticsDto> getBillDetailStatistics(@Valid @ModelAttribute BillCriteria billCriteria) {
        billCriteria.setIsIncludedReport(true);
        return ApiMessageUtils.success(
                billStatisticsService.getDetailStatistics(billCriteria),
                "Bill detail statistics"
        );
    }

    @GetMapping(value = "/category-detail", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<CategoryDetailStatisticsDto> getCategoryDetailStatistics(
            @Valid @ModelAttribute CategoryCriteria categoryCriteria,
            @RequestParam(name = "startDate") Instant startDate,
            @RequestParam(name = "endDate") Instant endDate,
            @RequestParam(name = "periodType") Integer periodType
    ) {
        return ApiMessageUtils.success(
                categoryStatisticsService.getDetailStatistics(categoryCriteria, startDate, endDate, periodType),
                "Category detail statistics"
        );
    }
}
