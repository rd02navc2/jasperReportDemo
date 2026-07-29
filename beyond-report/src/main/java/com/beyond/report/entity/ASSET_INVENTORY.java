package com.beyond.report.entity;

import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.*;

@Entity
@Table(name = "ASSET_INVENTORY")
@IdClass(ASSET_INVENTORY_ComposeKey.class) // <--- 1. 必須明確綁定主鍵類別
public class ASSET_INVENTORY implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "inventory_date")
    private String inventory_date; // <--- 2. 與 ComposeKey 欄位名一致

    @Id
    @Column(name = "self_no")
    private String self_no;        // <--- 2. 與 ComposeKey 欄位名一致

    private String model_no;
    private String type;
    private String prod_desc;
    private String owner_id;
    private String owner_name;
    private String title;
    private String dept_name;
    private String confirm_id;
    private Date confirm_date;

    // ⚠️ 3. 跨表 Join 欄位必須標註 @Transient，否則 JPA 會以為是 ASSET_INVENTORY 的實體欄位
    @Transient
    private String maintain_hist;

    @Transient
    private String memo_hist;

    @Transient
    private String type_name;

    @Transient
    private String generate_type;

    @Transient
    private Date close_date;

    // --- Getters and Setters 保持不變 ---
    public String getInventory_date() { return inventory_date; }
    public void setInventory_date(String inventory_date) { this.inventory_date = inventory_date; }
    public String getSelf_no() { return self_no; }
    public void setSelf_no(String self_no) { this.self_no = self_no; }
    public String getModel_no() { return model_no; }
    public void setModel_no(String model_no) { this.model_no = model_no; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getProd_desc() { return prod_desc; }
    public void setProd_desc(String prod_desc) { this.prod_desc = prod_desc; }
    public String getOwner_id() { return owner_id; }
    public void setOwner_id(String owner_id) { this.owner_id = owner_id; }
    public String getOwner_name() { return owner_name; }
    public void setOwner_name(String owner_name) { this.owner_name = owner_name; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDept_name() { return dept_name; }
    public void setDept_name(String dept_name) { this.dept_name = dept_name; }
    public String getConfirm_id() { return confirm_id; }
    public void setConfirm_id(String confirm_id) { this.confirm_id = confirm_id; }
    public Date getConfirm_date() { return confirm_date; }
    public void setConfirm_date(Date confirm_date) { this.confirm_date = confirm_date; }
    public String getMaintain_hist() { return maintain_hist; }
    public void setMaintain_hist(String maintain_hist) { this.maintain_hist = maintain_hist; }
    public String getMemo_hist() { return memo_hist; }
    public void setMemo_hist(String memo_hist) { this.memo_hist = memo_hist; }
    public String getType_name() { return type_name; }
    public void setType_name(String type_name) { this.type_name = type_name; }
    public String getGenerate_type() { return generate_type; }
    public void setGenerate_type(String generate_type) { this.generate_type = generate_type; }
    public Date getClose_date() { return close_date; }
    public void setClose_date(Date close_date) { this.close_date = close_date; }
}