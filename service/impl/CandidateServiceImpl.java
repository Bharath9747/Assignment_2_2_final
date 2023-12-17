package Java_Application.service.impl;


import Java_Application.model.CanditateDetails;
import Java_Application.model.InterviewDetails;
import Java_Application.model.WorkLocation;
import Java_Application.service.CandidateServices;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TextAlignment;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.*;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static Java_Application.repo.CandidateRepo.canditateDetailsHashMap;

public class CandidateServiceImpl implements CandidateServices {
    AtomicInteger atomicInteger = new AtomicInteger(0001);

    @Override
    public void readDataFromExcel(String excelFileName)  {
        try {
            FileInputStream inputStream = new FileInputStream(excelFileName);
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            int rowCount = sheet.getPhysicalNumberOfRows();
            for (int i = 1; i < rowCount; i++) {
                Row row = sheet.getRow(i);
                String stringdate = String.valueOf(row.getCell(0));
                    Date date = parseDate(stringdate);

                    Cell cell = row.getCell(6);

                        String name = String.valueOf(row.getCell(9)).toUpperCase().trim();
                        String skill = String.valueOf(row.getCell(5)).toUpperCase().trim();
                        double excelTimeValue = cell.getNumericCellValue();
                        LocalTime time = parseTime(excelTimeValue);
                        String teamName = String.valueOf(row.getCell(2)).toUpperCase().trim();
                        String panelName = String.valueOf(row.getCell(3)).toUpperCase().trim();
                        String interviewRound = String.valueOf(row.getCell(4));
                        String preferredLocation = String.valueOf(row.getCell(8)).toUpperCase().trim();
                        String workLocation = String.valueOf(row.getCell(7)).toUpperCase().trim();
                            int id = atomicInteger.getAndIncrement();
                            WorkLocation workLocation1 = new WorkLocation(preferredLocation, workLocation);
                            InterviewDetails interviewDetails = new InterviewDetails(date, time, teamName, panelName, interviewRound);
                            CanditateDetails canditateDetails = new CanditateDetails(id, name, skill, interviewDetails, workLocation1);
                            canditateDetailsHashMap.put(id, canditateDetails);


            }
            workbook.close();
            inputStream.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void topPanels() {
        System.out.println("Top 3 Panels");
        List<InterviewDetails> interviewDetailsList = getAllInterviewDetails();
        Map<String,Long> panelCount = interviewDetailsList.stream().filter(data->isMonth(data.getInterviewDate())).collect(Collectors.groupingBy(InterviewDetails::getPanelName,Collectors.counting()));
        panelCount.entrySet().stream().sorted(Map.Entry.<String ,Long>comparingByValue().reversed()).limit(3).forEach(
                (x)-> System.out.println(x.getKey()+" "+x.getValue())
        );
        System.out.println();
    }


    private boolean isMonth(Date interviewDate) {
        int month = interviewDate.getMonth()+1;
        if(month==10 || month==11)
            return true;
        return false;
    }


    private List<InterviewDetails> getAllInterviewDetails() {
        return canditateDetailsHashMap.values().stream().map(CanditateDetails::getInterviewDetailsList).collect(Collectors.toList());
    }




    private LocalTime parseTime(double s) throws ParseException {
        ;
        long javaTimeValue = Math.round((s-25569)*86400*1000);

        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(javaTimeValue), ZoneId.of("Asia/Kolkata"));
        LocalTime localTime = localDateTime.toLocalTime().minusHours(5).minusMinutes(21).minusSeconds(10);
        return localTime;
    }

    private Date parseDate(String s) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy");
        return sdf.parse(s);
    }



}
