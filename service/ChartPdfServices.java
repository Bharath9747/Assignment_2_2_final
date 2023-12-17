package Java_Application.service;

public interface ChartPdfServices {
    void createMonthChart();

    void createMonthWiseCharts();

    void addAllImagesToPdfMonthWise(String filename);

    void addAllImagesToPdfMonth(String pdfFolder);
}
