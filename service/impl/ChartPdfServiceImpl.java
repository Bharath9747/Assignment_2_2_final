package Java_Application.service.impl;

import Java_Application.model.CanditateDetails;
import Java_Application.model.InterviewDetails;
import Java_Application.model.WorkLocation;
import Java_Application.service.ChartPdfServices;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TextAlignment;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static Java_Application.repo.CandidateRepo.canditateDetailsHashMap;

public class ChartPdfServiceImpl implements ChartPdfServices {
    Map<String,Long> monthCount = null;
    Map<String,Map<String,Long>> skillCountByMonth =null;
    Map<String,Map<String,Long>> workLocationByMonth = null;
    List<Map<String, Long>> interviewRoundCountList = new ArrayList<>();
    List<Map<String,Long>> workLocationCountList = new ArrayList<>();
    List<Map<String, Long>> preferedLocationCountLiist = new ArrayList<>();
    List<Map<String,Long>> skillCountList = new ArrayList<>();
    List<Map<String, Long>> teamCountList = new ArrayList<>();

    private List<InterviewDetails> getAllInterviewDetails() {
        return canditateDetailsHashMap.values().stream().map(CanditateDetails::getInterviewDetailsList).collect(Collectors.toList());
    }
    @Override
    public void createMonthChart() {
        List<InterviewDetails> interviewDetailsList = getAllInterviewDetails();
        HashMap<Integer,String> months = new HashMap<>();
        months.put(10,"October");
        months.put(11,"November");
        months.put(12,"December");
        monthCount = interviewDetailsList.stream().collect(Collectors.groupingBy(interviewDetails -> months.get(interviewDetails.getInterviewDate().getMonth()+1),Collectors.counting()));
        DefaultPieDataset dataset = new DefaultPieDataset( );
        monthCount.entrySet().forEach(
                (x)->dataset.setValue(x.getKey(),x.getValue())
        );

        JFreeChart chart = ChartFactory.createPieChart(
                "Interviews Count Per Month",
                dataset,
                true,
                true,
                false);
        int width = 640;
        int height = 480;
        File pieChart = new File( "C:\\Users\\bharath.m\\IdeaProjects\\Assignment_2\\src\\main\\resources\\img\\MonthCount.jpeg" );
        try {
            ChartUtilities.saveChartAsJPEG( pieChart , chart , width , height );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        skillCountByMonth = canditateDetailsHashMap.values().stream()
                .collect(Collectors.groupingBy(
                        canditateDetails -> months.get(canditateDetails.getInterviewDetailsList().getInterviewDate().getMonth()+1),
                        Collectors.groupingBy(CanditateDetails::getSkill,Collectors.counting())
                ));
        DefaultCategoryDataset dataset1 = new DefaultCategoryDataset( );
        dataset1.setValue(skillCountByMonth.get("October").get("JAVA"),"October","JAVA");
        dataset1.setValue(skillCountByMonth.get("October").get("ANGULAR"),"October","ANGULAR");
        dataset1.setValue(skillCountByMonth.get("October").get("PRODUCTION SUPPORT"),"October","PRODUCTION SUPPORT");
        dataset1.setValue(skillCountByMonth.get("November").get("JAVA"),"November","JAVA");
        dataset1.setValue(skillCountByMonth.get("November").get("ANGULAR"),"November","ANGULAR");
        dataset1.setValue(skillCountByMonth.get("November").get("PRODUCTION SUPPORT"),"November","PRODUCTION SUPPORT");
        dataset1.setValue(skillCountByMonth.get("December").get("JAVA"),"December","JAVA");
        dataset1.setValue(skillCountByMonth.get("December").get("ANGULAR"),"December","ANGULAR");
        dataset1.setValue(skillCountByMonth.get("December").get("PRODUCTION SUPPORT"),"December","PRODUCTION SUPPORT");


        JFreeChart barChart = ChartFactory.createBarChart(
                "Skill  STATIStICS",
                "Skills", "No of Employees",
                dataset1, PlotOrientation.VERTICAL,
                true, true, false);
        File BarChart = new File( "C:\\Users\\bharath.m\\IdeaProjects\\Assignment_2\\src\\main\\resources\\img\\SkillByMonth.jpeg" );
        try {
            ChartUtilities.saveChartAsJPEG( BarChart , barChart , width , 900 );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        DefaultCategoryDataset dataset2 = new DefaultCategoryDataset( );
        workLocationByMonth = canditateDetailsHashMap.values().stream()
                .collect(Collectors.groupingBy(
                        canditateDetails -> months.get(canditateDetails.getInterviewDetailsList().getInterviewDate().getMonth()+1),
                        Collectors.groupingBy(canditateDetails -> canditateDetails.getWorkLocation().getWorkLocation(),Collectors.counting())
                ));
        dataset2.setValue(workLocationByMonth.get("October").get("BANGALORE"),"October","BANGALORE");
        dataset2.setValue(workLocationByMonth.get("October").get("HYDERABAD"),"October","HYDERABAD");
        dataset2.setValue(workLocationByMonth.get("October").get("CHENNAI"),"October","CHENNAI");
        dataset2.setValue(workLocationByMonth.get("November").get("BANGALORE"),"November","BANGALORE");
        dataset2.setValue(workLocationByMonth.get("November").get("HYDERABAD"),"November","HYDERABAD");
        dataset2.setValue(workLocationByMonth.get("November").get("CHENNAI"),"November","CHENNAI");
        dataset2.setValue(workLocationByMonth.get("December").get("BANGALORE"),"December","BANGALORE");
        dataset2.setValue(workLocationByMonth.get("December").get("HYDERABAD"),"December","HYDERABAD");
        dataset2.setValue(workLocationByMonth.get("December").get("CHENNAI"),"December","CHENNAI");

        JFreeChart barChart2 = ChartFactory.createBarChart(
                "Work Location  STATIStICS",
                "Location", "No of Employees",
                dataset2, PlotOrientation.VERTICAL,
                true, true, false);
        File BarChart2 = new File( "C:\\Users\\bharath.m\\IdeaProjects\\Assignment_2\\src\\main\\resources\\img\\WorkLocation.jpeg" );
        try {
            ChartUtilities.saveChartAsJPEG( BarChart2 , barChart2 , width , 900 );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createMonthWiseCharts() {
        for (int i=10;i<=12;i++) {
            List<InterviewDetails> interviewDetailsList = getAllInterviewDetails();

            int finalI = i-1;
            Map<String, Long> interviewRoundCount = interviewDetailsList.stream().filter(interviewDetails -> interviewDetails.getInterviewDate().getMonth()== finalI).collect(Collectors.groupingBy(interviewDetails -> interviewDetails.getInterviewRound(), Collectors.counting()));

            Map<String, Long> workLocationCount = canditateDetailsHashMap.values().stream().filter(canditateDetails -> canditateDetails.getInterviewDetailsList().getInterviewDate().getMonth()==finalI).map(CanditateDetails::getWorkLocation).map(WorkLocation::getWorkLocation).collect(Collectors.toList()).stream().collect(Collectors.groupingBy(workLocation->workLocation,Collectors.counting()));
            Map<String, Long> preferedLocationCount =canditateDetailsHashMap.values().stream().filter(canditateDetails -> canditateDetails.getInterviewDetailsList().getInterviewDate().getMonth()==finalI).map(CanditateDetails::getWorkLocation).map(WorkLocation::getPreferredLocation).collect(Collectors.toList()).stream().collect(Collectors.groupingBy(preferedLocation->preferedLocation,Collectors.counting()));
            Map<String, Long> skillCount =canditateDetailsHashMap.values().stream().filter(canditateDetails -> canditateDetails.getInterviewDetailsList().getInterviewDate().getMonth()==finalI).map(CanditateDetails::getSkill).collect(Collectors.toList()).stream().collect(Collectors.groupingBy(skill->skill,Collectors.counting()));
            Map<String, Long> teamCount = interviewDetailsList.stream().filter(interviewDetails -> interviewDetails.getInterviewDate().getMonth()== finalI).collect(Collectors.groupingBy(interviewDetails -> interviewDetails.getTeamName(), Collectors.counting()));

            DefaultPieDataset dataset = new DefaultPieDataset( );
            interviewRoundCount.entrySet().forEach(
                    (x)->dataset.setValue(x.getKey(),x.getValue())
            );

            JFreeChart chart = ChartFactory.createPieChart(
                    "Interviews Rounds Count",
                    dataset,
                    true,
                    true,
                    false);
            int width = 640;
            int height = 480;
            File pieChart = new File( "C:\\Users\\bharath.m\\IdeaProjects\\Assignment_2\\src\\main\\resources\\img\\InterviewRoundCount"+i+".jpeg" );
            try {
                ChartUtilities.saveChartAsJPEG( pieChart , chart , width , height );
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            interviewRoundCountList.add(interviewRoundCount);

            List<String > locations = new ArrayList<>(Arrays.asList("CHENNAI","BANGALORE","HYDERABAD"));
            DefaultPieDataset dataset1 = new DefaultPieDataset( );
            final int[] s = {0};
            workLocationCount.entrySet().forEach(
                    (x)->   {if(locations.contains(x.getKey()))
                        dataset1.setValue(x.getKey(),x.getValue());
                    else
                        s[0] = s[0] +1;
                    }
            );
            dataset1.setValue("Others",s[0]);

            JFreeChart chart1 = ChartFactory.createPieChart(
                    "Work Location Count",
                    dataset1,
                    true,
                    true,
                    false);
            File pieChart1 = new File( "C:\\Users\\bharath.m\\IdeaProjects\\Assignment_2\\src\\main\\resources\\img\\WorkLocation"+i+".jpeg" );
            try {
                ChartUtilities.saveChartAsJPEG( pieChart1 , chart1 , width , height );
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            workLocationCountList.add(workLocationCount);
            DefaultPieDataset dataset2 = new DefaultPieDataset( );
            s[0]=0;
            preferedLocationCount.entrySet().forEach(
                    (x)->   {if(locations.contains(x.getKey()))
                        dataset2.setValue(x.getKey(),x.getValue());
                    else
                        s[0] = s[0] +1;
                    }
            );
            dataset2.setValue("Others",s[0]);
            preferedLocationCountLiist.add(preferedLocationCount);

            JFreeChart chart2 = ChartFactory.createPieChart(
                    "Preferred Location Count",
                    dataset2,
                    true,
                    true,
                    false);
            File pieChart2 = new File( "C:\\Users\\bharath.m\\IdeaProjects\\Assignment_2\\src\\main\\resources\\img\\PreferredLocation"+i+".jpeg" );
            try {
                ChartUtilities.saveChartAsJPEG( pieChart2 , chart2 , width , height );
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            List<String > skills = new ArrayList<>(Arrays.asList("JAVA","ANGULAR","QA"));
            DefaultPieDataset dataset3 = new DefaultPieDataset( );
            s[0]=0;
            skillCount.entrySet().forEach(
                    (x)->   {if(skills.contains(x.getKey()))
                        dataset3.setValue(x.getKey(),x.getValue());
                    else
                        s[0] = s[0] +1;
                    }
            );
            dataset3.setValue("Others",s[0]);
            skillCountList.add(skillCount);
            JFreeChart chart3 = ChartFactory.createPieChart(
                    "Skills Count",
                    dataset3,
                    true,
                    true,
                    false);
            File pieChart3 = new File( "C:\\Users\\bharath.m\\IdeaProjects\\Assignment_2\\src\\main\\resources\\img\\Skill"+i+".jpeg" );
            try {
                ChartUtilities.saveChartAsJPEG( pieChart3 , chart3 , width , height );
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            DefaultPieDataset dataset4 = new DefaultPieDataset( );
            s[0]=0;
            teamCount.entrySet().forEach(
                    (x)->   {if(x.getKey().equals("BENCH"))
                        dataset4.setValue(x.getKey(),x.getValue());
                    else
                        s[0] = s[0] +1;
                    }
            );
            dataset4.setValue("Others",s[0]);
            teamCountList.add(teamCount);
            JFreeChart chart4 = ChartFactory.createPieChart(
                    "Team Count",
                    dataset4,
                    true,
                    true,
                    false);
            File pieChart4 = new File( "C:\\Users\\bharath.m\\IdeaProjects\\Assignment_2\\src\\main\\resources\\img\\TeamCount"+i+".jpeg" );
            try {
                ChartUtilities.saveChartAsJPEG( pieChart4 , chart4 , width , height );
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }

    @Override
    public void addAllImagesToPdfMonthWise(String fileName) {
        try {
            File file = new File(fileName);
            file.createNewFile();
            PdfWriter writer = new PdfWriter(fileName);
            PdfDocument document = new PdfDocument(writer);
            document.addNewPage();
            Document doc = new Document(document);

            document.setDefaultPageSize(PageSize.A4);
            doc.add(new Paragraph()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(50)
                    .setBold()
                    .add(new Text("Month Wise Report"))
            );
            doc.add(
                    new Paragraph()
                            .setTextAlignment(TextAlignment.RIGHT)
                            .setFontSize(30)
                            .add("By Bharath")
            );
            doc.add(
                    new Image(ImageDataFactory.create("C:\\Users\\bharath.m\\IdeaProjects\\Assignment_2\\src\\main\\resources\\img\\MonthCount.jpeg")).setTextAlignment(TextAlignment.CENTER)
            );
            float colwid1[] = {260, 260};
            com.itextpdf.layout.element.Table t1 = new com.itextpdf.layout.element.Table(colwid1).setFontSize(15f);
            t1.addCell(new com.itextpdf.layout.element.Cell().add("Month"));
            t1.addCell(new com.itextpdf.layout.element.Cell().add("Interview Count"));
            monthCount.entrySet().forEach(
                    (x)-> {
                        t1.addCell(new com.itextpdf.layout.element.Cell().add(x.getKey()));
                        t1.addCell(new com.itextpdf.layout.element.Cell().add(x.getValue()+""));
                    }
            );
            doc.add(new Paragraph("\n"));
            doc.add(new Paragraph("\n"));
            doc.add(new Paragraph("\n"));
            doc.add(t1);
            doc.add(new Paragraph("\n"));
            doc.add(new Paragraph("\n"));
            doc.add(
                    new Image(ImageDataFactory.create("C:\\Users\\bharath.m\\IdeaProjects\\Assignment_2\\src\\main\\resources\\img\\SkillByMonth.jpeg")).setTextAlignment(TextAlignment.CENTER)
            );
            Map<String, Map<String, Long>> sortedSkillCountByMonth = skillCountByMonth.entrySet().stream()
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            entry -> entry.getValue().entrySet().stream()
                                    .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                                    .collect(Collectors.toMap(
                                            Map.Entry::getKey,
                                            Map.Entry::getValue,
                                            (e1, e2) -> e1,
                                            LinkedHashMap::new
                                    )),
                            (e1, e2) -> e1,
                            LinkedHashMap::new
                    ));

            com.itextpdf.layout.element.Table t2 = new com.itextpdf.layout.element.Table(colwid1).setFontSize(15f);
            t2.addCell(new com.itextpdf.layout.element.Cell().add("Skill Count"));
            t2.addCell(new com.itextpdf.layout.element.Cell().add("Skill Count"));
            doc.add(new Paragraph("\n"));
            sortedSkillCountByMonth.entrySet().forEach(
                    (x)->{
                        t2.addCell(new com.itextpdf.layout.element.Cell().add("Month "+x.getKey()).setBold());
                        t2.addCell(new com.itextpdf.layout.element.Cell().add(""));
                        x.getValue().entrySet().forEach(
                                (y)->{
                                    t2.addCell(new com.itextpdf.layout.element.Cell().add(y.getKey()+" "+y.getValue()));

                                }

                        );
                        t2.addCell(new com.itextpdf.layout.element.Cell().add(""));
                    }
            );
            t2.addCell(new com.itextpdf.layout.element.Cell().add(""));
            doc.add(t2);
            doc.add(new Paragraph()
                    .setRotationAngle(Math.PI/4)
                    .setFontSize(30)
                    .setBold()
                    .add(new Text("Month Wise Report"))
            );
            doc.add(
                    new Image(ImageDataFactory.create("C:\\Users\\bharath.m\\IdeaProjects\\Assignment_2\\src\\main\\resources\\img\\WorkLocation.jpeg")).setTextAlignment(TextAlignment.CENTER)
            );
            Map<String, Map<String, Long>> sortedworkLocationByMonth = workLocationByMonth.entrySet().stream()
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            entry -> entry.getValue().entrySet().stream()
                                    .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                                    .collect(Collectors.toMap(
                                            Map.Entry::getKey,
                                            Map.Entry::getValue,
                                            (e1, e2) -> e1,
                                            LinkedHashMap::new
                                    )),
                            (e1, e2) -> e1,
                            LinkedHashMap::new
                    ));
            com.itextpdf.layout.element.Table t3 = new com.itextpdf.layout.element.Table(colwid1).setFontSize(15f);
            t3.addCell(new com.itextpdf.layout.element.Cell().add("Location Count"));
            t3.addCell(new com.itextpdf.layout.element.Cell().add("Location Count"));
            sortedworkLocationByMonth.entrySet().forEach(
                    (x)->{
                        t3.addCell(new com.itextpdf.layout.element.Cell().add("Month "+x.getKey()).setBold());
                        t3.addCell(new com.itextpdf.layout.element.Cell().add(""));
                        x.getValue().entrySet().forEach(
                                (y)->{
                                    t3.addCell(new com.itextpdf.layout.element.Cell().add(y.getKey()+" "+y.getValue()));
                                }

                        );
                        t3.addCell(new com.itextpdf.layout.element.Cell().add(""));
                    }
            );

            doc.add(new Paragraph("\n"));
            t3.addCell(new com.itextpdf.layout.element.Cell().add(""));
            doc.add(t3);
            doc.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addAllImagesToPdfMonth(String pdfFolder) {
        List<String> months = new ArrayList<>(Arrays.asList("October","November","December"));
        try {
            for (int i=0;i<=2;i++) {
                String fileName = pdfFolder +"\\"+ months.get(i)+".pdf";
                File file = new File(fileName);
                file.createNewFile();
                PdfWriter writer = new PdfWriter(fileName);
                PdfDocument document = new PdfDocument(writer);
                document.addNewPage();
                Document doc = new Document(document);

                document.setDefaultPageSize(PageSize.A4);

                doc.add(new Paragraph("\n"));
                doc.add(new Paragraph("\n"));
                doc.add(new Paragraph("\n"));
                doc.add(new Paragraph("\n"));
                doc.add(new Paragraph("\n"));
                doc.add(new Paragraph("\n"));
                doc.add(new Paragraph()
                        .setTextAlignment(TextAlignment.CENTER)
                        .setFontSize(50)
                        .setBold()
                        .add(new Text(months.get(i)+" Report"))
                );
                doc.add(
                        new Paragraph()
                                .setTextAlignment(TextAlignment.RIGHT)
                                .setFontSize(30)
                                .add("By Bharath")
                );
                doc.add(new Paragraph("\n"));
                doc.add(new Paragraph("\n"));
                doc.add(new Paragraph("\n"));
                doc.add(new Paragraph("\n"));
                doc.add(new Paragraph("\n"));
                doc.add(new Paragraph("\n"));
                doc.add(
                        new Image(ImageDataFactory.create("C:\\Users\\bharath.m\\IdeaProjects\\Assignment_2\\src\\main\\resources\\img\\InterviewRoundCount"+(i+10)+".jpeg")).setTextAlignment(TextAlignment.CENTER)
                );

                Map<String,Long> temp   = interviewRoundCountList.get(i).entrySet().stream().sorted(Map.Entry.<String,Long>comparingByValue().reversed()).collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1,e2)->e1,
                        LinkedHashMap::new
                )) ;
                doc.add(new Paragraph("\n"));
                doc.add(new Paragraph("\n"));
                doc.add(new Paragraph("\n"));doc.add(new Paragraph("\n"));
                float colwid1[] = {260, 260};
                com.itextpdf.layout.element.Table t1 = new com.itextpdf.layout.element.Table(colwid1).setFontSize(15f);
                t1.addCell(new com.itextpdf.layout.element.Cell().add("Interview Round").setBold());
                t1.addCell(new com.itextpdf.layout.element.Cell().add("Count").setBold());
                temp.entrySet().forEach(
                        (x)-> {
                            t1.addCell(new com.itextpdf.layout.element.Cell().add(x.getKey()));
                            t1.addCell(new com.itextpdf.layout.element.Cell().add(x.getValue()+""));
                        }
                );
                doc.add(t1);
                doc.add(new Paragraph("\n"));
                temp   = workLocationCountList.get(i).entrySet().stream().sorted(Map.Entry.<String,Long>comparingByValue().reversed()).collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1,e2)->e1,
                        LinkedHashMap::new
                )) ;

                doc.add(
                        new Image(ImageDataFactory.create("C:\\Users\\bharath.m\\IdeaProjects\\Assignment_2\\src\\main\\resources\\img\\WorkLocation"+(i+10)+".jpeg")).setTextAlignment(TextAlignment.CENTER)
                );
                doc.add(new Paragraph("\n"));
                doc.add(new Paragraph("\n"));
                com.itextpdf.layout.element.Table t2 = new com.itextpdf.layout.element.Table(colwid1).setFontSize(15f);
                t2.addCell(new com.itextpdf.layout.element.Cell().add("Location").setBold());
                t2.addCell(new com.itextpdf.layout.element.Cell().add("Count").setBold());
                temp.entrySet().forEach(
                        (x)-> {
                            t2.addCell(new com.itextpdf.layout.element.Cell().add(x.getKey()));
                            t2.addCell(new com.itextpdf.layout.element.Cell().add(x.getValue()+""));
                        }
                );
                doc.add(t2);
                doc.add(new Paragraph("\n"));
                doc.add(new Paragraph("\n"));
                temp   = preferedLocationCountLiist.get(i).entrySet().stream().sorted(Map.Entry.<String,Long>comparingByValue().reversed()).collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1,e2)->e1,
                        LinkedHashMap::new
                )) ;
                doc.add(
                        new Image(ImageDataFactory.create("C:\\Users\\bharath.m\\IdeaProjects\\Assignment_2\\src\\main\\resources\\img\\PreferredLocation"+(i+10)+".jpeg")).setTextAlignment(TextAlignment.CENTER)
                );
                doc.add(new Paragraph("\n"));
                doc.add(new Paragraph("\n"));
                com.itextpdf.layout.element.Table t3 = new com.itextpdf.layout.element.Table(colwid1).setFontSize(15f);
                t3.addCell(new com.itextpdf.layout.element.Cell().add("Location").setBold());
                t3.addCell(new com.itextpdf.layout.element.Cell().add("Count").setBold());
                temp.entrySet().forEach(
                        (x)-> {
                            t3.addCell(new com.itextpdf.layout.element.Cell().add(x.getKey()));
                            t3.addCell(new com.itextpdf.layout.element.Cell().add(x.getValue()+""));
                        }
                );
                doc.add(t3);
                doc.add(new Paragraph("\n"));
                doc.add(new Paragraph("\n"));
                temp   = skillCountList.get(i).entrySet().stream().sorted(Map.Entry.<String,Long>comparingByValue().reversed()).collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1,e2)->e1,
                        LinkedHashMap::new
                )) ;
                doc.add(
                        new Image(ImageDataFactory.create("C:\\Users\\bharath.m\\IdeaProjects\\Assignment_2\\src\\main\\resources\\img\\Skill"+(i+10)+".jpeg")).setTextAlignment(TextAlignment.CENTER)
                );
                doc.add(new Paragraph("\n"));
                doc.add(new Paragraph("\n"));
                com.itextpdf.layout.element.Table t4 = new com.itextpdf.layout.element.Table(colwid1).setFontSize(15f);
                t4.addCell(new com.itextpdf.layout.element.Cell().add("Skill").setBold());
                t4.addCell(new com.itextpdf.layout.element.Cell().add("Count").setBold());
                temp.entrySet().forEach(
                        (x)-> {
                            t4.addCell(new com.itextpdf.layout.element.Cell().add(x.getKey()));
                            t4.addCell(new com.itextpdf.layout.element.Cell().add(x.getValue()+""));
                        }
                );
                doc.add(t4);
                doc.add(new Paragraph("\n"));
                doc.add(new Paragraph("\n"));
                temp   = teamCountList.get(i).entrySet().stream().sorted(Map.Entry.<String,Long>comparingByValue().reversed()).collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1,e2)->e1,
                        LinkedHashMap::new
                )) ;
                doc.add(
                        new Image(ImageDataFactory.create("C:\\Users\\bharath.m\\IdeaProjects\\Assignment_2\\src\\main\\resources\\img\\TeamCount"+(i+10)+".jpeg")).setTextAlignment(TextAlignment.CENTER)
                );
                doc.add(new Paragraph("\n"));
                doc.add(new Paragraph("\n"));
                com.itextpdf.layout.element.Table t5 = new com.itextpdf.layout.element.Table(colwid1).setFontSize(15f);
                t5.addCell(new com.itextpdf.layout.element.Cell().add("Team").setBold());
                t5.addCell(new com.itextpdf.layout.element.Cell().add("Count").setBold());
                temp.entrySet().forEach(
                        (x)-> {
                            t5.addCell(new com.itextpdf.layout.element.Cell().add(x.getKey()));
                            t5.addCell(new com.itextpdf.layout.element.Cell().add(x.getValue()+""));
                        }
                );
                doc.add(t5);
                doc.close();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
