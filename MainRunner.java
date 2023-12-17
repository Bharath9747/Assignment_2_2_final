package Java_Application;



import Java_Application.service.impl.CandidateServiceImpl;
import Java_Application.service.impl.ChartPdfServiceImpl;
import Java_Application.service.impl.DatabaseServicesImpl;


public class MainRunner {

    public static void main(String[] args) {
        CandidateServiceImpl candidateService = new CandidateServiceImpl();
        DatabaseServicesImpl databaseServices = new DatabaseServicesImpl();
        ChartPdfServiceImpl chartPdfService = new ChartPdfServiceImpl();
        String excelFileName = "C:\\Users\\bharath.m\\IdeaProjects\\Assignment_2\\src\\main\\resources\\data\\FinalAccolite.xlsx";
        String pdfFile = "C:\\Users\\bharath.m\\IdeaProjects\\Assignment_2\\src\\main\\resources\\pdf\\MonthWiseReport.pdf";
        String pdfFolder = "C:\\Users\\bharath.m\\IdeaProjects\\Assignment_2\\src\\main\\resources\\pdf\\";
        candidateService.readDataFromExcel(excelFileName);
        candidateService.topPanels();
        databaseServices.createConnection();
        databaseServices.addRecords();
        databaseServices.maxNumberOfInterviews();
        databaseServices.minNumberOfInterviews();
        databaseServices.topSkillsByMonthAndView();
        databaseServices.topSkillsByTime();
        chartPdfService.createMonthChart();
        chartPdfService.createMonthWiseCharts();
        System.out.println("Chart Generated");
        chartPdfService.addAllImagesToPdfMonthWise(pdfFile);
        chartPdfService.addAllImagesToPdfMonth(pdfFolder);
        System.out.println("PDF Generated");
    }
}