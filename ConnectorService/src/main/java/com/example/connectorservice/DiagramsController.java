package com.example.connectorservice;

import com.example.protocol.BuildingDto;
import com.example.protocol.PriceDto;
import lombok.RequiredArgsConstructor;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class DiagramsController {

    private final ConnectorServiceController controller;

    @GetMapping("/graph/topHigh")
    public ResponseEntity<byte[]> getHighPriceDiagram() throws Exception {
        Map<String, PriceDto> data = controller.getMaxPriceForAllCountries();

        data = data.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.comparingLong(PriceDto::getPrice).reversed()))
                .limit(5)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        data.forEach((k, v) -> {
            dataset.addValue(v.getPrice(), "Cena", k);
        });


        return getResponseEntity(dataset);
    }


    @GetMapping("/graph/topMin")
    public ResponseEntity<byte[]> getMinPriceDiagram() throws Exception {
        Map<String, PriceDto> data = controller.getMinPriceForAllCountries();

        data = data.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.comparingLong(PriceDto::getPrice)))
                .limit(5)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        data.forEach((k, v) -> {
            dataset.addValue(v.getPrice(), "Cena", k);
        });


        return getResponseEntity(dataset);
    }


    @GetMapping("/graph/avgPricePerType")
    public ResponseEntity<byte[]> getPriceDiagram(@RequestParam(required = false) String sort) throws Exception {
        List<BuildingDto> data = controller.getBuildingsByType(null, null, sort);
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        data.forEach(value -> {
            dataset.addValue(value.getPrice(), "Cena", value.getType());
        });


        return getResponseEntity(dataset);
    }

    @GetMapping("/graph/test")
    public ResponseEntity<byte[]> getDiagram() throws Exception {
        // Create the graph
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(1.0, "S1", "Category 1");
        dataset.addValue(4.0, "S1", "Category 2");
        dataset.addValue(3.0, "S1", "Category 3");
        dataset.addValue(5.0, "S1", "Category 4");
        dataset.addValue(5.0, "S1", "Category 5");
        dataset.addValue(7.0, "S1", "Category 6");
        dataset.addValue(7.0, "S1", "Category 7");
        dataset.addValue(8.0, "S1", "Category 8");

        return getResponseEntity(dataset);
    }

    public static byte[] chartToByteArray(JFreeChart chart) throws Exception {
        BufferedImage chartImage = chart.createBufferedImage(500, 500);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(chartImage, "png", outputStream);
        return outputStream.toByteArray();
    }

    private ResponseEntity<byte[]> getResponseEntity(DefaultCategoryDataset dataset) throws Exception {
        JFreeChart chart = ChartFactory.createBarChart("Bar Chart Example", "Category", "Value", dataset);
        File file = new File("bar_chart_example.png");
        ImageIO.write(chart.createBufferedImage(800, 600), "png", file);


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);

        byte[] imageData = chartToByteArray(chart);

        return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
    }
}
