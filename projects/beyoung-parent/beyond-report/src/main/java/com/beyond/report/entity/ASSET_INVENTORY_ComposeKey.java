package com.beyond.report.entity;

import java.io.Serializable;
import java.util.Objects;

public class ASSET_INVENTORY_ComposeKey implements Serializable {
    private static final long serialVersionUID = 1L;

    // 欄位名稱必須與 ASSET_INVENTORY 內的 @Id 欄位名稱與型態完全一致
    private String inventory_date;
    private String self_no;

    public ASSET_INVENTORY_ComposeKey() {
    }

    public ASSET_INVENTORY_ComposeKey(String inventory_date, String self_no) {
        this.inventory_date = inventory_date;
        this.self_no = self_no;
    }

    public String getInventory_date() {
        return inventory_date;
    }

    public void setInventory_date(String inventory_date) {
        this.inventory_date = inventory_date;
    }

    public String getSelf_no() {
        return self_no;
    }

    public void setSelf_no(String self_no) {
        this.self_no = self_no;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ASSET_INVENTORY_ComposeKey that = (ASSET_INVENTORY_ComposeKey) o;
        return Objects.equals(inventory_date, that.inventory_date) &&
               Objects.equals(self_no, that.self_no);
    }

    @Override
    public int hashCode() {
        return Objects.hash(inventory_date, self_no);
    }
}