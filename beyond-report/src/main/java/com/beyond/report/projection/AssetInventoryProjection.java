package com.beyond.report.projection;

import java.util.Date;

public interface AssetInventoryProjection {
    String getInventory_date();
    String getSelf_no();
    String getModel_no();
    String getType();
    String getType_name();
    String getProd_desc();
    String getOwner_id();
    String getOwner_name();
    String getTitle();
    String getDept_name();
    String getConfirm_id();
    Date getConfirm_date();
    String getMaintain_hist();
    String getMemo_hist();
}