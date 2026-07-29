package com.beyoung.surrounding.dc.entity; 
import java.io.Serializable;
import jakarta.persistence.*; // 升級為 Spring Boot 3 的 jakarta 規範
import lombok.*;

@Entity
@Table(name = "EMPLOYEE")
@Getter
@Setter
@AllArgsConstructor
@Builder
public class EMPLOYEE implements Serializable { //  類別名建議改為標準大駝峰 Employee
	
    private static final long serialVersionUID = 1L;
    
    @Id
    @Column(name = "EMPLOYEEID")
    private String employeeId;
    
    @Column(name = "CODE")
    private String code;
    
    @Column(name = "CNNAME")
    private String cnName;
    
    @Column(name = "JOBDATE")
    private String jobDate;
    
    @Column(name = "BIRTHDATE")
    private String birthDate;
    
    @Column(name = "EMPLOYEESTATEID")
    private String employeeStateId;
    
    @Column(name = "DEPARTMENTID")
    private String departmentId;
    
    @Column(name = "MOBILEPHONE")
    private String mobilePhone;
    
    @Column(name = "EMAIL")
    private String email;
    
    @Column(name = "ADDRESS")
    private String address;
    
    @Column(name = "MAJOR")
    private String major;
    
    @Column(name = "SCNAME")
    private String scName;
    
    // 非資料庫映射欄位，Hibernate 進行 CRUD 時會自動忽略它們
    @Transient
    private String school;

    @Transient
    private String deptName;

    @Transient
    private String title;

    // 手動補回空建構子，防止 Hibernate 反射實例化失敗
    public EMPLOYEE() {
    }
}