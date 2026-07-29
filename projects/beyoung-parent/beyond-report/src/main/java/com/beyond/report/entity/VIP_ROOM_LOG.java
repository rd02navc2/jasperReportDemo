package com.beyond.report.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "VIP_ROOM_LOG")
@IdClass(VIP_ROOM_LOG_ComposeKey.class)
public class VIP_ROOM_LOG implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(length = 50)
    private String center;

    @Id
    @Temporal(TemporalType.DATE)
    private Date transaction_date;

    @Column(length = 20)
    private String transaction_time;

    @Column(length = 50)
    private String counter_id;

    @Column(length = 50)
    private String pos_id;

    @Id
    @Column(length = 50)
    private String user_id;

    @Column(length = 100)
    private String user_name;

    @Column(length = 50)
    private String card_no;

    private Integer price;

    @Temporal(TemporalType.TIMESTAMP)
    private Date enter_date;

    @Temporal(TemporalType.TIMESTAMP)
    private Date exit_date;

    @Temporal(TemporalType.TIMESTAMP)
    private Date refund_date;

    @Column(length = 50)
    private String invoice_no;

    @Column(length = 10)
    private String vip;

    @Column(length = 10)
    private String in_room;

    @Column(length = 255)
    private String comment;

    private Integer point;
    private Integer total_qty;

    @Transient
    private Integer age;

    @Transient
    private Integer rec_cnt;

    @Transient
    private Integer tot_rec_cnt;

    @Column(length = 50)
    private String access_id;

    @Column(length = 50)
    private String LPK01;

    @Column(length = 50)
    private String LPK04;

    @Column(length = 50)
    private String LPK06;

    @Column(length = 50)
    private String LPK15;

    @Column(length = 50)
    private String LPK18;

    public Integer getTot_rec_cnt() {
        return tot_rec_cnt;
    }
    public void setTot_rec_cnt(Integer tot_rec_cnt) {
        this.tot_rec_cnt = tot_rec_cnt;
    }
    public String getAccess_id() {
        return access_id;
    }
    public void setAccess_id(String access_id) {
        this.access_id = access_id;
    }
    public Integer getPoint() {
        return point;
    }
    public void setPoint(Integer point) {
        this.point = point;
    }
    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    public Integer getRec_cnt() {
        return rec_cnt;
    }
    public void setRec_cnt(Integer rec_cnt) {
        this.rec_cnt = rec_cnt;
    }
    public String getCenter() {
        return center;
    }
    public void setCenter(String center) {
        this.center = center;
    }
    public Date getTransaction_date() {
        return transaction_date;
    }
    public void setTransaction_date(Date transaction_date) {
        this.transaction_date = transaction_date;
    }
    public String getTransaction_time() {
        return transaction_time;
    }
    public void setTransaction_time(String transaction_time) {
        this.transaction_time = transaction_time;
    }
    public String getCounter_id() {
        return counter_id;
    }
    public void setCounter_id(String counter_id) {
        this.counter_id = counter_id;
    }
    public String getPos_id() {
        return pos_id;
    }
    public void setPos_id(String pos_id) {
        this.pos_id = pos_id;
    }
    public String getUser_id() {
        return user_id;
    }
    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
    public String getUser_name() {
        return user_name;
    }
    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
    public String getCard_no() {
        return card_no;
    }
    public void setCard_no(String card_no) {
        this.card_no = card_no;
    }
    public Integer getPrice() {
        return price;
    }
    public void setPrice(Integer price) {
        this.price = price;
    }
    public Date getEnter_date() {
        return enter_date;
    }
    public void setEnter_date(Date enter_date) {
        this.enter_date = enter_date;
    }
    public Date getExit_date() {
        return exit_date;
    }
    public void setExit_date(Date exit_date) {
        this.exit_date = exit_date;
    }
    public Date getRefund_date() {
        return refund_date;
    }
    public void setRefund_date(Date refund_date) {
        this.refund_date = refund_date;
    }
    public String getInvoice_no() {
        return invoice_no;
    }
    public void setInvoice_no(String invoice_no) {
        this.invoice_no = invoice_no;
    }
    public String getVip() {
        return vip;
    }
    public void setVip(String vip) {
        this.vip = vip;
    }
    public String getIn_room() {
        return in_room;
    }
    public void setIn_room(String in_room) {
        this.in_room = in_room;
    }
    public String getLPK01() {
        return LPK01;
    }
    public void setLPK01(String lPK01) {
        LPK01 = lPK01;
    }
    public String getLPK04() {
        return LPK04;
    }
    public void setLPK04(String lPK04) {
        LPK04 = lPK04;
    }
    public String getLPK06() {
        return LPK06;
    }
    public void setLPK06(String lPK06) {
        LPK06 = lPK06;
    }
    public String getLPK15() {
        return LPK15;
    }
    public void setLPK15(String lPK15) {
        LPK15 = lPK15;
    }
    public String getLPK18() {
        return LPK18;
    }
    public void setLPK18(String lPK18) {
        LPK18 = lPK18;
    }
    public Integer getAge() {
        return age;
    }
    public void setAge(Integer age) {
        this.age = age;
    }
    public Integer getTotal_qty() {
        return total_qty;
    }
    public void setTotal_qty(Integer total_qty) {
        this.total_qty = total_qty;
    }
}