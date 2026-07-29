package com.beyond.report.model;

import java.io.Serializable;

/**
 * 對應前端 zTree 需要的節點資料結構（欄位名稱需與 demo1.jsp 中 JS 使用的
 * treeNode.program_name / canRead ... 完全一致，故不使用駝峰命名）。
 */
public class FunctionNode implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer pId;
    private String name;
    /** 功能對應的頁面路徑；分類節點可為空字串 */
    private String program_name;
    private String canRead;
    private String canInsert;
    private String canSave;
    private String canDelete;
    private String canPrint;

    public FunctionNode() {
    }

    /** 分類節點（無實際頁面，只作分組用） */
    public static FunctionNode category(int id, int pId, String name) {
        FunctionNode n = new FunctionNode();
        n.id = id;
        n.pId = pId;
        n.name = name;
        n.program_name = "";
        return n;
    }

    /** 功能節點 */
    public static FunctionNode function(int id, int pId, String name, String programName,
                                         boolean canRead, boolean canInsert, boolean canSave,
                                         boolean canDelete, boolean canPrint) {
        FunctionNode n = new FunctionNode();
        n.id = id;
        n.pId = pId;
        n.name = name;
        n.program_name = programName;
        n.canRead = canRead ? "Y" : "N";
        n.canInsert = canInsert ? "Y" : "N";
        n.canSave = canSave ? "Y" : "N";
        n.canDelete = canDelete ? "Y" : "N";
        n.canPrint = canPrint ? "Y" : "N";
        return n;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getpId() {
        return pId;
    }

    public void setpId(Integer pId) {
        this.pId = pId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProgram_name() {
        return program_name;
    }

    public void setProgram_name(String program_name) {
        this.program_name = program_name;
    }

    public String getCanRead() {
        return canRead;
    }

    public void setCanRead(String canRead) {
        this.canRead = canRead;
    }

    public String getCanInsert() {
        return canInsert;
    }

    public void setCanInsert(String canInsert) {
        this.canInsert = canInsert;
    }

    public String getCanSave() {
        return canSave;
    }

    public void setCanSave(String canSave) {
        this.canSave = canSave;
    }

    public String getCanDelete() {
        return canDelete;
    }

    public void setCanDelete(String canDelete) {
        this.canDelete = canDelete;
    }

    public String getCanPrint() {
        return canPrint;
    }

    public void setCanPrint(String canPrint) {
        this.canPrint = canPrint;
    }
}
