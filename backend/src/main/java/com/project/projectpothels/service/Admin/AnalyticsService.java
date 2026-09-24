package com.project.projectpothels.service.Admin;

import java.time.Month;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.projectpothels.Repository.Admin.RoadReportRepository;
import com.project.projectpothels.model.Admin.RoadReport;

@Service
public class AnalyticsService {

    @Autowired
    private RoadReportRepository repository;

    public Map<String, Object> getAnalytics() {
        List<RoadReport> reports = repository.findAll();
        Map<String, Object> result = new LinkedHashMap<>();

        long total = reports.size();
        long completed = reports.stream().filter(r -> "COMPLETED".equalsIgnoreCase(r.getStatus())).count();
        long inProgress = reports.stream().filter(r -> "IN-PROGRESS".equalsIgnoreCase(r.getStatus())).count();
        long pending = reports.stream().filter(r -> "PENDING".equalsIgnoreCase(r.getStatus())).count();

        result.put("totalReports", total);
        result.put("completed", completed);
        result.put("averageRepairTime", 0);
        result.put("successRate", total == 0 ? 0 : Math.round((completed * 100.0) / total));
        result.put("monthlyReports", monthlyReports(reports));
        result.put("repairStatus", Map.of("completed", completed, "inProgress", inProgress, "pending", pending));
        result.put("areas", areaStats(reports));
        return result;
    }

    private List<Map<String, Object>> monthlyReports(List<RoadReport> reports) {
        Map<Month, Long> counts = reports.stream()
                .filter(r -> r.getCreatedAt() != null)
                .collect(Collectors.groupingBy(r -> r.getCreatedAt().getMonth(), TreeMap::new, Collectors.counting()));

        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<Month, Long> entry : counts.entrySet()) {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("month", entry.getKey().name().substring(0, 3));
            row.put("count", entry.getValue());
            result.add(row);
        }
        return result;
    }

    private List<Map<String, Object>> areaStats(List<RoadReport> reports) {
        Map<String, List<RoadReport>> grouped = reports.stream()
                .filter(r -> r.getArea() != null)
                .collect(Collectors.groupingBy(RoadReport::getArea));

        return grouped.entrySet().stream()
                .map(entry -> {
                    List<RoadReport> rows = entry.getValue();
                    Map<String, Object> data = new LinkedHashMap<>();
                    data.put("area", entry.getKey());
                    data.put("total", rows.size());
                    data.put("completed", rows.stream().filter(r -> "COMPLETED".equalsIgnoreCase(r.getStatus())).count());
                    data.put("pending", rows.stream().filter(r -> "PENDING".equalsIgnoreCase(r.getStatus())).count());
                    return data;
                })
                .sorted(Comparator.comparingLong(x -> -((Number)x.get("total")).longValue()))
                .limit(10)
                .toList();
    }
}
