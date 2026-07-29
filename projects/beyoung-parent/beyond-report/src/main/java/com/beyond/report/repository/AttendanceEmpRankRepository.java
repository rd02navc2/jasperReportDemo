package com.beyond.report.repository;

import com.beyond.report.entity.ATTENDANCEEMPRANK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AttendanceEmpRankRepository extends JpaRepository<ATTENDANCEEMPRANK, String> {

    /**
     * 1. 按員工編號 (Code) 排序
     */
    @Query(value = """
            SELECT 
                t1.AttendanceEmployeeRankId AS ATTENDANCEEMPLOYEERANKID,
                t3.Code AS CODE,
                t3.CnName AS CNNAME,
                t3.EmployeeStateId AS EMPLOYEESTATEID,
                DATE_FORMAT(t1.date, '%Y-%m-%d') AS DATE,
                t1.WeekDay AS WEEKDAY,
                t2.shortname AS SHORTNAME,
                t2.WorkBeginTime AS WORKBEGINTIME,
                t2.WorkEndTime AS WORKENDTIME
            FROM AttendanceEmpRank t1
            INNER JOIN AttendanceRank t2 ON t1.AttendanceRankId = t2.AttendanceRankId
            INNER JOIN Employee t3 ON t1.EmployeeId = t3.EmployeeId
            WHERE t1.date BETWEEN :sFromDate AND :sEndDate
            ORDER BY 
                CASE WHEN :sSord = 'ASC' THEN t3.Code END ASC,
                CASE WHEN :sSord = 'DESC' THEN t3.Code END DESC,
                t1.date ASC
            """, nativeQuery = true)
    List<ATTENDANCEEMPRANK> findScheduleOrderByCode(
            @Param("sFromDate") String sFromDate,
            @Param("sEndDate") String sEndDate,
            @Param("sSord") String sSord
    );

    /**
     * 2. 按員工姓名 (CnName) 排序
     */
    @Query(value = """
            SELECT 
                t1.AttendanceEmployeeRankId AS ATTENDANCEEMPLOYEERANKID,
                t3.Code AS CODE,
                t3.CnName AS CNNAME,
                t3.EmployeeStateId AS EMPLOYEESTATEID,
                DATE_FORMAT(t1.date, '%Y-%m-%d') AS DATE,
                t1.WeekDay AS WEEKDAY,
                t2.shortname AS SHORTNAME,
                t2.WorkBeginTime AS WORKBEGINTIME,
                t2.WorkEndTime AS WORKENDTIME
            FROM AttendanceEmpRank t1
            INNER JOIN AttendanceRank t2 ON t1.AttendanceRankId = t2.AttendanceRankId
            INNER JOIN Employee t3 ON t1.EmployeeId = t3.EmployeeId
            WHERE t1.date BETWEEN :sFromDate AND :sEndDate
            ORDER BY 
                CASE WHEN :sSord = 'ASC' THEN t3.CnName END ASC,
                CASE WHEN :sSord = 'DESC' THEN t3.CnName END DESC,
                t1.date ASC
            """, nativeQuery = true)
    List<ATTENDANCEEMPRANK> findScheduleOrderByName(
            @Param("sFromDate") String sFromDate,
            @Param("sEndDate") String sEndDate,
            @Param("sSord") String sSord
    );

    /**
     * 3. 預設按日期 (date) 排序
     */
    @Query(value = """
            SELECT 
                t1.AttendanceEmployeeRankId AS ATTENDANCEEMPLOYEERANKID,
                t3.Code AS CODE,
                t3.CnName AS CNNAME,
                t3.EmployeeStateId AS EMPLOYEESTATEID,
                DATE_FORMAT(t1.date, '%Y-%m-%d') AS DATE,
                t1.WeekDay AS WEEKDAY,
                t2.shortname AS SHORTNAME,
                t2.WorkBeginTime AS WORKBEGINTIME,
                t2.WorkEndTime AS WORKENDTIME
            FROM AttendanceEmpRank t1
            INNER JOIN AttendanceRank t2 ON t1.AttendanceRankId = t2.AttendanceRankId
            INNER JOIN Employee t3 ON t1.EmployeeId = t3.EmployeeId
            WHERE t1.date BETWEEN :sFromDate AND :sEndDate
            ORDER BY 
                CASE WHEN :sSord = 'ASC' THEN t1.date END ASC,
                CASE WHEN :sSord = 'DESC' THEN t1.date END DESC,
                t3.Code ASC
            """, nativeQuery = true)
    List<ATTENDANCEEMPRANK> findScheduleOrderByDate(
            @Param("sFromDate") String sFromDate,
            @Param("sEndDate") String sEndDate,
            @Param("sSord") String sSord
    );
}