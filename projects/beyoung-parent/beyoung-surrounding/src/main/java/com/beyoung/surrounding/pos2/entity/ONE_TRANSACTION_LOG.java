package com.beyoung.surrounding.pos2.entity;

import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "POS2_ONE_TRANSACTION_LOG")
@Table(name = "ONE_TRANSACTION_LOG")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ONE_TRANSACTION_LOG implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @Column(name = "order_id")
    private String orderId;

    @Column(name = "pos_center")
    private String posCenter;

    @Column(name = "pos_counter_id")
    private String posCounterId;

    @Column(name = "pos_product_name")
    private String posProductName;

    @Column(name = "pos_amount")
    private Integer posAmount;

    @Column(name = "pos_id")
    private String posId;

    @Column(name = "pos_date_time")
    private Date posDateTime;

    @Column(name = "access_date")
    private Date accessDate;

    @Column(name = "transaction_type")
    private String transactionType;

    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "transaction_date")
    private Date transactionDate;

    @Column(name = "one_time_key")
    private String oneTimeKey;

    @Column(name = "refund_order_id")
    private String refundOrderId;

    @Column(name = "refund_transaction_id")
    private Integer refundTransactionId;

    @Column(name = "refund_transaction_date")
    private Date refundTransactionDate;

    @Column(name = "invoice_no")
    private String invoiceNo;

    @Column(name = "wallet_provider")
    private String walletProvider;
    
    private String code;
    private String message;
    private String transTime;

    // --- Getters and Setters ---

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getPosCenter() { return posCenter; }
    public void setPosCenter(String posCenter) { this.posCenter = posCenter; }

    public String getPosCounterId() { return posCounterId; }
    public void setPosCounterId(String posCounterId) { this.posCounterId = posCounterId; }

    public String getPosProductName() { return posProductName; }
    public void setPosProductName(String posProductName) { this.posProductName = posProductName; }

    public Integer getPosAmount() { return posAmount; }
    public void setPosAmount(Integer posAmount) { this.posAmount = posAmount; }

    public String getPosId() { return posId; }
    public void setPosId(String posId) { this.posId = posId; }

    public Date getPosDateTime() { return posDateTime; }
    public void setPosDateTime(Date posDateTime) { this.posDateTime = posDateTime; }

    public Date getAccessDate() { return accessDate; }
    public void setAccessDate(Date accessDate) { this.accessDate = accessDate; }

    public String getTransactionType() { return transactionType; }
    public void setTransactionType(String transactionType) { this.transactionType = transactionType; }

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public Date getTransactionDate() { return transactionDate; }
    public void setTransactionDate(Date transactionDate) { this.transactionDate = transactionDate; }

    public String getOneTimeKey() { return oneTimeKey; }
    public void setOneTimeKey(String oneTimeKey) { this.oneTimeKey = oneTimeKey; }

    public String getRefundOrderId() { return refundOrderId; }
    public void setRefundOrderId(String refundOrderId) { this.refundOrderId = refundOrderId; }

    public Integer getRefundTransactionId() { return refundTransactionId; }
    public void setRefundTransactionId(Integer refundTransactionId) { this.refundTransactionId = refundTransactionId; }

    public Date getRefundTransactionDate() { return refundTransactionDate; }
    public void setRefundTransactionDate(Date refundTransactionDate) { this.refundTransactionDate = refundTransactionDate; }

    public String getInvoiceNo() { return invoiceNo; }
    public void setInvoiceNo(String invoiceNo) { this.invoiceNo = invoiceNo; }

    public String getWalletProvider() { return walletProvider; }
    public void setWalletProvider(String walletProvider) { this.walletProvider = walletProvider; }
    
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public String getTransTime() {
        return transTime;
    }
    public void setTransTime(String transTime) {
        this.transTime = transTime;
    }
	
}