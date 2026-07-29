package com.beyond.surrounding.dc.repositiry;

import com.beyond.surrounding.dc.entity.EMPLOYEE; // 依實際員工實體路徑為準

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HrEmployeeRepository extends JpaRepository<EMPLOYEE, String> {

    @Query(value = """
        SELECT COUNT(1) > 0 
        FROM Employee 
        WHERE EmployeeStateId IN (
            'EmployeeState1001',
            'EmployeeState2001',
            '153041ca579ce9501482c921756c19f38b19a',
            '145226f339bf8032f417eaa92b2448c9de4ac',
            '1641522482432f91c4b1d96c508c62e0ab9c4'
        ) 
          AND IDCardNo = :idCardNo 
          AND IDCardNo <> 'F225368172'
        """, nativeQuery = true)
    boolean existsValidEmployee(@Param("idCardNo") String idCardNo);
    
    /**
     * 透過身分證後 4 碼比對，撈取符合在職狀態的員工中文姓名
     */
    @Query(value = """
        SELECT Cnname 
        FROM Employee 
        WHERE EmployeeStateId IN (
            'EmployeeState1001',
            'EmployeeState2001',
            '153041ca579ce9501482c921756c19f38b19a',
            '145226f339bf8032f417eaa92b2448c9de4ac',
            '1641522482432f91c4b1d96c508c62e0ab9c4'
        ) 
          AND substring(IDCardNo, 7, 4) = :cardNO 
          AND IDCardNo <> 'F225368172'
        """, nativeQuery = true)
    Optional<String> findCnnameByCardNo(@Param("cardNO") String cardNO);
    
}