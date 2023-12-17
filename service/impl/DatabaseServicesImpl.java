package Java_Application.service.impl;

import Java_Application.model.CanditateDetails;
import Java_Application.service.DatabaseServices;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.*;
import java.util.Map;

import static Java_Application.repo.CandidateRepo.canditateDetailsHashMap;

public class DatabaseServicesImpl implements DatabaseServices {

    BasicDataSource dataSource = null;

    @Override
    public void topSkillsByMonthAndView() {
        System.out.println("Top 3 Skills in October and November");
        try {
            String sql = "create or replace view  TopSkills as select skill,count(*) as SkillCount from Interview_Status where Month(InterviewDate) in (10,11) group by Skill Order by skillcount desc limit 3;";
            Statement st = dataSource.getConnection().createStatement();
            st.executeUpdate(sql);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        String sqlQuery = "select * from TopSkills";

        try (PreparedStatement preparedStatement = dataSource.getConnection().prepareStatement(sqlQuery);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String teamName = resultSet.getString("Skill");
                int interviewCount = resultSet.getInt("SkillCount");

                System.out.println("Skill : " + teamName);
                System.out.println("Count: " + interviewCount);
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        System.out.println();
    }

    @Override
    public void topSkillsByTime() {
        System.out.println("Top 3 Skills in Peak Time");
        String sqlQuery = "select InterviewTime,count(*) as InterviewTimeCount from Interview_Status  group by InterviewTime Order by InterviewTimeCount desc limit 1;";
        String time="";
        try (PreparedStatement preparedStatement = dataSource.getConnection().prepareStatement(sqlQuery);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                time = resultSet.getString("InterviewTime");
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        System.out.println("Peak Time : "+time);

        String sqlQuery1 = "select skill,count(*) as SkillCount from Interview_Status where InterviewTime = '"+time+"' group by Skill Order by skillcount desc limit 3;";

        try (PreparedStatement preparedStatement = dataSource.getConnection().prepareStatement(sqlQuery1);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String teamName = resultSet.getString("Skill");
                int interviewCount = resultSet.getInt("SkillCount");

                System.out.println("Skill : " + teamName);
                System.out.println("Count: " + interviewCount);
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        System.out.println();
    }


    @Override
    public void maxNumberOfInterviews() {
        System.out.println("Maximum number of interviews , TeamName in October and November");
        String sqlQuery = "select TeamName , count(*) as InterviewCount from Interview_Status where month(InterviewDate) in (10,11)\n" +
                "group by TeamName order by InterviewCount desc limit 1;";

        try (PreparedStatement preparedStatement = dataSource.getConnection().prepareStatement(sqlQuery);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            if (resultSet.next()) {
                String teamName = resultSet.getString("TeamName");
                int interviewCount = resultSet.getInt("InterviewCount");

                System.out.println("Team with the most interviews: " + teamName);
                System.out.println("Interview count: " + interviewCount);
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        System.out.println();
    }
    public void minNumberOfInterviews() {
        System.out.println("Minimum number of interviews , TeamName in October and November");
        String sqlQuery = "select TeamName , count(*) as InterviewCount from Interview_Status where month(InterviewDate) in (10,11)\n" +
                "group by TeamName order by InterviewCount  limit 1;";

        try (PreparedStatement preparedStatement = dataSource.getConnection().prepareStatement(sqlQuery);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            if (resultSet.next()) {
                String teamName = resultSet.getString("TeamName");
                int interviewCount = resultSet.getInt("InterviewCount");

                System.out.println("Team with the mininimum interviews: " + teamName);
                System.out.println("Interview count: " + interviewCount);
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        System.out.println();
    }
    @Override
    public void createConnection() {
        try {
            dataSource = new BasicDataSource();
            dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");

            dataSource.setUrl("jdbc:mysql://localhost:3306/Accolite");
            dataSource.setUsername("root");
            dataSource.setPassword("1234");
            System.out.println("Connection Established");
        }
        catch (Exception e){
            e.printStackTrace();
        }
        System.out.println();
    }

    @Override
    public void addRecords() {
        try {
            Statement truncateStatement = dataSource.getConnection().createStatement();
            String truncateQuery = "Truncate table Interview_Status";
            truncateStatement.executeUpdate(truncateQuery);
            canditateDetailsHashMap.entrySet().parallelStream().forEach(
                    this::insert
            );
            System.out.println("Insertion Done");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println();
    }

    private void insert(Map.Entry<Integer, CanditateDetails> entry) {
        try {

            Connection con;
            con = dataSource.getConnection();
            con.setAutoCommit(false);
            String insertQuery = "INSERT INTO Interview_Status (Id, Name, Skill, InterviewDate, InterviewTime, TeamName, PanelName, InterviewRound, PreferredLocation, WorkLocation) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement insertStatement;
            insertStatement = con.prepareStatement(insertQuery);
            CanditateDetails candidate = entry.getValue();
            insertStatement.setInt(1, entry.getKey());
            insertStatement.setString(2, candidate.getName());
            insertStatement.setString(3, candidate.getSkill());
            insertStatement.setDate(4, new java.sql.Date(candidate.getInterviewDetailsList().getInterviewDate().getTime()));
            insertStatement.setTime(5, Time.valueOf(candidate.getInterviewDetailsList().getInterviewTime()));
            insertStatement.setString(6, candidate.getInterviewDetailsList().getTeamName());
            insertStatement.setString(7, candidate.getInterviewDetailsList().getPanelName());
            insertStatement.setString(8, candidate.getInterviewDetailsList().getInterviewRound());
            insertStatement.setString(9, candidate.getWorkLocation().getPreferredLocation());
            insertStatement.setString(10, candidate.getWorkLocation().getWorkLocation());
            insertStatement.executeUpdate();

        con.commit();
        con.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
