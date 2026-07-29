package com.beyond.report.entity;

import java.util.Date;
import jakarta.persistence.*;

@Entity
@Table(name = "MKT_REQUEST")
public class MKT_REQUEST {

    @Id
    @Column(length = 50)
    private String request_no;

    private Date request_date;

    @Column(length = 10)
    private String is_design;

    @Column(length = 50)
    private String select_item;

    @Column(length = 10)
    private String fee_mail;
    @Column(length = 10)
    private String fee_name;
    @Column(length = 10)
    private String fee_sms;
    @Column(length = 10)
    private String fee_mms;
    @Column(length = 10)
    private String fee_line;
    @Column(length = 10)
    private String fee_line_account;
    @Column(length = 10)
    private String fee_line_child;
    @Column(length = 10)
    private String fee_line_orange;
    @Column(length = 10)
    private String fee_line_vip;
    @Column(length = 10)
    private String fee_line_black;

    @Column(length = 10)
    private String free_official;
    @Column(length = 10)
    private String free_app_activity;
    @Column(length = 10)
    private String free_app_push;
    @Column(length = 10)
    private String free_fb_post;
    @Column(length = 10)
    private String free_fb_story;
    @Column(length = 10)
    private String free_ig_post;
    @Column(length = 10)
    private String free_ig_story;

    @Column(length = 10)
    private String promote_fee_withholding;
    @Column(length = 10)
    private String promote_fee_free;

    @Column(length = 50)
    private String apply_id;
    @Column(length = 100)
    private String apply_name;
    @Column(length = 100)
    private String apply_email;
    @Column(length = 20)
    private String apply_ext;
    @Column(length = 50)
    private String apply_dept;

    @Column(length = 50)
    private String apply_dept_manager1;
    @Column(length = 100)
    private String apply_dept_manager1_email;
    @Column(length = 50)
    private String apply_dept_manager2;
    @Column(length = 100)
    private String apply_dept_manager2_email;

    private Date expect_promote_date;
    private Date real_promote_date;

    @Column(length = 20)
    private String status;

    // === 【關鍵修改 1】長文案改用 TEXT 型態，不佔用 Row Size ===
    @Column(columnDefinition = "TEXT")
    private String copywriting;

    @Column(length = 100)
    private String counter_name;

    @Column(columnDefinition = "TEXT")
    private String promote_activity;

    @Column(length = 10)
    private String msg_list_address;
    @Column(length = 10)
    private String msg_list_mobile;
    @Column(length = 10)
    private String msg_brand_address;
    @Column(length = 10)
    private String msg_brand_mobile;

    @Column(length = 10)
    private String msg_sex_female;
    @Column(length = 10)
    private String msg_sex_male;
    @Column(length = 10)
    private String msg_sex_all;

    private Integer msg_age_from;
    private Integer msg_age_to;

    @Column(length = 10)
    private String msg_age_all;

    @Column(length = 10)
    private String msg_location_cy;
    @Column(length = 10)
    private String msg_location_newtp;
    @Column(length = 10)
    private String msg_location_tp;
    @Column(length = 10)
    private String msg_location_ty;
    @Column(length = 10)
    private String msg_location_other;

    @Column(columnDefinition = "TEXT")
    private String msg_location_other_desc;

    @Column(length = 50)
    private String msg_counter_category;
    @Column(length = 10)
    private String msg_counter_category_all;

    private Integer msg_consume_amt;

    @Column(length = 10)
    private String msg_consume_amt_all;

    @Column(columnDefinition = "TEXT")
    private String msg_others;

    @Column(length = 100)
    private String media_data_brand;
    @Column(length = 100)
    private String media_data_mkt;
    @Column(length = 10)
    private String media_show_image;
    @Column(length = 10)
    private String media_show_vedio;

    @Column(columnDefinition = "TEXT")
    private String media_others;

    private Date approve_dept_manager_date;
    @Column(length = 50)
    private String approve_dept_manager_id;

    private Date approve_plan_manager_date;
    @Column(length = 50)
    private String approve_plan_manager_id;

    private Date approve_design_manager_date;
    @Column(length = 50)
    private String approve_design_manager_id;

    private Date approve_mkt_manager_date;
    @Column(length = 50)
    private String approve_mkt_manager_id;

    private Date approve_vp_manager_date;
    @Column(length = 50)
    private String approve_vp_manager_id;

    private Date reject_date;
    @Column(length = 50)
    private String reject_id;

    // === 【關鍵修改 2】退件原因與審核意見改用 TEXT ===
    @Column(columnDefinition = "TEXT")
    private String reject_reason;

    @Column(length = 255)
    private String a00_file;
    @Column(length = 255)
    private String a01_file;
    @Column(length = 255)
    private String a02_file;
    @Column(length = 255)
    private String a03_file;

    private Integer expect_promote_date_hour;
    private Integer real_promote_date_hour;

    @Column(length = 10)
    private String is_closed;

    @Column(columnDefinition = "TEXT")
    private String dept_manager_desc;

    @Column(columnDefinition = "TEXT")
    private String mkt_plan_desc;

    @Column(columnDefinition = "TEXT")
    private String mkt_design_desc;

    @Column(columnDefinition = "TEXT")
    private String mkt_manager_desc;

    @Column(columnDefinition = "TEXT")
    private String vp_desc;

    @Transient
    private String status_name;

    @Transient
    private java.math.BigInteger sno;

    @Transient
    private Integer rec_cnt;

    // Below are Getters and Setters (保持不變) ...
    public String getRequest_no() { return request_no; }
    public void setRequest_no(String request_no) { this.request_no = request_no; }
    public Date getRequest_date() { return request_date; }
    public void setRequest_date(Date request_date) { this.request_date = request_date; }
    public String getFee_mail() { return fee_mail; }
    public void setFee_mail(String fee_mail) { this.fee_mail = fee_mail; }
    public String getFee_name() { return fee_name; }
    public void setFee_name(String fee_name) { this.fee_name = fee_name; }
    public String getFee_sms() { return fee_sms; }
    public void setFee_sms(String fee_sms) { this.fee_sms = fee_sms; }
    public String getFee_mms() { return fee_mms; }
    public void setFee_mms(String fee_mms) { this.fee_mms = fee_mms; }
    public String getFee_line() { return fee_line; }
    public void setFee_line(String fee_line) { this.fee_line = fee_line; }
    public String getFee_line_account() { return fee_line_account; }
    public void setFee_line_account(String fee_line_account) { this.fee_line_account = fee_line_account; }
    public String getFee_line_child() { return fee_line_child; }
    public void setFee_line_child(String fee_line_child) { this.fee_line_child = fee_line_child; }
    public String getFee_line_orange() { return fee_line_orange; }
    public void setFee_line_orange(String fee_line_orange) { this.fee_line_orange = fee_line_orange; }
    public String getFree_official() { return free_official; }
    public void setFree_official(String free_official) { this.free_official = free_official; }
    public String getFree_app_activity() { return free_app_activity; }
    public void setFree_app_activity(String free_app_activity) { this.free_app_activity = free_app_activity; }
    public String getFree_app_push() { return free_app_push; }
    public void setFree_app_push(String free_app_push) { this.free_app_push = free_app_push; }
    public String getFree_fb_post() { return free_fb_post; }
    public void setFree_fb_post(String free_fb_post) { this.free_fb_post = free_fb_post; }
    public String getFree_fb_story() { return free_fb_story; }
    public void setFree_fb_story(String free_fb_story) { this.free_fb_story = free_fb_story; }
    public String getFree_ig_story() { return free_ig_story; }
    public void setFree_ig_story(String free_ig_story) { this.free_ig_story = free_ig_story; }
    public String getPromote_fee_withholding() { return promote_fee_withholding; }
    public void setPromote_fee_withholding(String promote_fee_withholding) { this.promote_fee_withholding = promote_fee_withholding; }
    public String getPromote_fee_free() { return promote_fee_free; }
    public void setPromote_fee_free(String promote_fee_free) { this.promote_fee_free = promote_fee_free; }
    public String getIs_design() { return is_design; }
    public void setIs_design(String is_design) { this.is_design = is_design; }
    public String getApply_id() { return apply_id; }
    public void setApply_id(String apply_id) { this.apply_id = apply_id; }
    public String getApply_name() { return apply_name; }
    public void setApply_name(String apply_name) { this.apply_name = apply_name; }
    public String getApply_email() { return apply_email; }
    public void setApply_email(String apply_email) { this.apply_email = apply_email; }
    public String getApply_ext() { return apply_ext; }
    public void setApply_ext(String apply_ext) { this.apply_ext = apply_ext; }
    public String getApply_dept() { return apply_dept; }
    public void setApply_dept(String apply_dept) { this.apply_dept = apply_dept; }
    public String getApply_dept_manager1() { return apply_dept_manager1; }
    public void setApply_dept_manager1(String apply_dept_manager1) { this.apply_dept_manager1 = apply_dept_manager1; }
    public String getApply_dept_manager1_email() { return apply_dept_manager1_email; }
    public void setApply_dept_manager1_email(String apply_dept_manager1_email) { this.apply_dept_manager1_email = apply_dept_manager1_email; }
    public String getApply_dept_manager2() { return apply_dept_manager2; }
    public void setApply_dept_manager2(String apply_dept_manager2) { this.apply_dept_manager2 = apply_dept_manager2; }
    public String getApply_dept_manager2_email() { return apply_dept_manager2_email; }
    public void setApply_dept_manager2_email(String apply_dept_manager2_email) { this.apply_dept_manager2_email = apply_dept_manager2_email; }
    public Date getExpect_promote_date() { return expect_promote_date; }
    public void setExpect_promote_date(Date expect_promote_date) { this.expect_promote_date = expect_promote_date; }
    public Date getReal_promote_date() { return real_promote_date; }
    public void setReal_promote_date(Date real_promote_date) { this.real_promote_date = real_promote_date; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getCopywriting() { return copywriting; }
    public void setCopywriting(String copywriting) { this.copywriting = copywriting; }
    public String getCounter_name() { return counter_name; }
    public void setCounter_name(String counter_name) { this.counter_name = counter_name; }
    public String getPromote_activity() { return promote_activity; }
    public void setPromote_activity(String promote_activity) { this.promote_activity = promote_activity; }
    public String getMsg_list_address() { return msg_list_address; }
    public void setMsg_list_address(String msg_list_address) { this.msg_list_address = msg_list_address; }
    public String getMsg_list_mobile() { return msg_list_mobile; }
    public void setMsg_list_mobile(String msg_list_mobile) { this.msg_list_mobile = msg_list_mobile; }
    public String getMsg_brand_address() { return msg_brand_address; }
    public void setMsg_brand_address(String msg_brand_address) { this.msg_brand_address = msg_brand_address; }
    public String getMsg_brand_mobile() { return msg_brand_mobile; }
    public void setMsg_brand_mobile(String msg_brand_mobile) { this.msg_brand_mobile = msg_brand_mobile; }
    public String getMsg_sex_female() { return msg_sex_female; }
    public void setMsg_sex_female(String msg_sex_female) { this.msg_sex_female = msg_sex_female; }
    public String getMsg_sex_male() { return msg_sex_male; }
    public void setMsg_sex_male(String msg_sex_male) { this.msg_sex_male = msg_sex_male; }
    public String getMsg_sex_all() { return msg_sex_all; }
    public void setMsg_sex_all(String msg_sex_all) { this.msg_sex_all = msg_sex_all; }
    public String getMsg_age_all() { return msg_age_all; }
    public void setMsg_age_all(String msg_age_all) { this.msg_age_all = msg_age_all; }
    public String getMsg_location_cy() { return msg_location_cy; }
    public void setMsg_location_cy(String msg_location_cy) { this.msg_location_cy = msg_location_cy; }
    public String getMsg_location_newtp() { return msg_location_newtp; }
    public void setMsg_location_newtp(String msg_location_newtp) { this.msg_location_newtp = msg_location_newtp; }
    public String getMsg_location_tp() { return msg_location_tp; }
    public void setMsg_location_tp(String msg_location_tp) { this.msg_location_tp = msg_location_tp; }
    public String getMsg_location_ty() { return msg_location_ty; }
    public void setMsg_location_ty(String msg_location_ty) { this.msg_location_ty = msg_location_ty; }
    public String getMsg_location_other() { return msg_location_other; }
    public void setMsg_location_other(String msg_location_other) { this.msg_location_other = msg_location_other; }
    public String getMsg_location_other_desc() { return msg_location_other_desc; }
    public void setMsg_location_other_desc(String msg_location_other_desc) { this.msg_location_other_desc = msg_location_other_desc; }
    public String getMsg_counter_category() { return msg_counter_category; }
    public void setMsg_counter_category(String msg_counter_category) { this.msg_counter_category = msg_counter_category; }
    public String getMsg_counter_category_all() { return msg_counter_category_all; }
    public void setMsg_counter_category_all(String msg_counter_category_all) { this.msg_counter_category_all = msg_counter_category_all; }
    public String getMsg_consume_amt_all() { return msg_consume_amt_all; }
    public void setMsg_consume_amt_all(String msg_consume_amt_all) { this.msg_consume_amt_all = msg_consume_amt_all; }
    public String getMsg_others() { return msg_others; }
    public void setMsg_others(String msg_others) { this.msg_others = msg_others; }
    public String getMedia_data_brand() { return media_data_brand; }
    public void setMedia_data_brand(String media_data_brand) { this.media_data_brand = media_data_brand; }
    public String getMedia_data_mkt() { return media_data_mkt; }
    public void setMedia_data_mkt(String media_data_mkt) { this.media_data_mkt = media_data_mkt; }
    public String getMedia_show_image() { return media_show_image; }
    public void setMedia_show_image(String media_show_image) { this.media_show_image = media_show_image; }
    public String getMedia_show_vedio() { return media_show_vedio; }
    public void setMedia_show_vedio(String media_show_vedio) { this.media_show_vedio = media_show_vedio; }
    public String getMedia_others() { return media_others; }
    public void setMedia_others(String media_others) { this.media_others = media_others; }
    public Date getApprove_dept_manager_date() { return approve_dept_manager_date; }
    public void setApprove_dept_manager_date(Date approve_dept_manager_date) { this.approve_dept_manager_date = approve_dept_manager_date; }
    public String getApprove_dept_manager_id() { return approve_dept_manager_id; }
    public void setApprove_dept_manager_id(String approve_dept_manager_id) { this.approve_dept_manager_id = approve_dept_manager_id; }
    public Date getApprove_plan_manager_date() { return approve_plan_manager_date; }
    public void setApprove_plan_manager_date(Date approve_plan_manager_date) { this.approve_plan_manager_date = approve_plan_manager_date; }
    public String getApprove_plan_manager_id() { return approve_plan_manager_id; }
    public void setApprove_plan_manager_id(String approve_plan_manager_id) { this.approve_plan_manager_id = approve_plan_manager_id; }
    public Date getApprove_design_manager_date() { return approve_design_manager_date; }
    public void setApprove_design_manager_date(Date approve_design_manager_date) { this.approve_design_manager_date = approve_design_manager_date; }
    public String getApprove_design_manager_id() { return approve_design_manager_id; }
    public void setApprove_design_manager_id(String approve_design_manager_id) { this.approve_design_manager_id = approve_design_manager_id; }
    public Date getApprove_mkt_manager_date() { return approve_mkt_manager_date; }
    public void setApprove_mkt_manager_date(Date approve_mkt_manager_date) { this.approve_mkt_manager_date = approve_mkt_manager_date; }
    public String getApprove_mkt_manager_id() { return approve_mkt_manager_id; }
    public void setApprove_mkt_manager_id(String approve_mkt_manager_id) { this.approve_mkt_manager_id = approve_mkt_manager_id; }
    public Date getApprove_vp_manager_date() { return approve_vp_manager_date; }
    public void setApprove_vp_manager_date(Date approve_vp_manager_date) { this.approve_vp_manager_date = approve_vp_manager_date; }
    public String getApprove_vp_manager_id() { return approve_vp_manager_id; }
    public void setApprove_vp_manager_id(String approve_vp_manager_id) { this.approve_vp_manager_id = approve_vp_manager_id; }
    public String getA00_file() { return a00_file; }
    public void setA00_file(String a00_file) { this.a00_file = a00_file; }
    public String getA01_file() { return a01_file; }
    public void setA01_file(String a01_file) { this.a01_file = a01_file; }
    public String getA02_file() { return a02_file; }
    public void setA02_file(String a02_file) { this.a02_file = a02_file; }
    public String getA03_file() { return a03_file; }
    public void setA03_file(String a03_file) { this.a03_file = a03_file; }
    public String getStatus_name() { return status_name; }
    public void setStatus_name(String status_name) { this.status_name = status_name; }
    public java.math.BigInteger getSno() { return sno; }
    public void setSno(java.math.BigInteger sno) { this.sno = sno; }
    public Integer getRec_cnt() { return rec_cnt; }
    public void setRec_cnt(Integer rec_cnt) { this.rec_cnt = rec_cnt; }
    public String getFee_line_vip() { return fee_line_vip; }
    public void setFee_line_vip(String fee_line_vip) { this.fee_line_vip = fee_line_vip; }
    public String getFee_line_black() { return fee_line_black; }
    public void setFee_line_black(String fee_line_black) { this.fee_line_black = fee_line_black; }
    public String getFree_ig_post() { return free_ig_post; }
    public void setFree_ig_post(String free_ig_post) { this.free_ig_post = free_ig_post; }
    public Integer getMsg_consume_amt() { return msg_consume_amt; }
    public void setMsg_consume_amt(Integer msg_consume_amt) { this.msg_consume_amt = msg_consume_amt; }
    public Integer getExpect_promote_date_hour() { return expect_promote_date_hour; }
    public void setExpect_promote_date_hour(Integer expect_promote_date_hour) { this.expect_promote_date_hour = expect_promote_date_hour; }
    public Integer getReal_promote_date_hour() { return real_promote_date_hour; }
    public void setReal_promote_date_hour(Integer real_promote_date_hour) { this.real_promote_date_hour = real_promote_date_hour; }
    public Integer getMsg_age_from() { return msg_age_from; }
    public void setMsg_age_from(Integer msg_age_from) { this.msg_age_from = msg_age_from; }
    public Integer getMsg_age_to() { return msg_age_to; }
    public void setMsg_age_to(Integer msg_age_to) { this.msg_age_to = msg_age_to; }
    public String getIs_closed() { return is_closed; }
    public void setIs_closed(String is_closed) { this.is_closed = is_closed; }
    public Date getReject_date() { return reject_date; }
    public void setReject_date(Date reject_date) { this.reject_date = reject_date; }
    public String getReject_id() { return reject_id; }
    public void setReject_id(String reject_id) { this.reject_id = reject_id; }
    public String getReject_reason() { return reject_reason; }
    public void setReject_reason(String reject_reason) { this.reject_reason = reject_reason; }
    public String getDept_manager_desc() { return dept_manager_desc; }
    public void setDept_manager_desc(String dept_manager_desc) { this.dept_manager_desc = dept_manager_desc; }
    public String getMkt_plan_desc() { return mkt_plan_desc; }
    public void setMkt_plan_desc(String mkt_plan_desc) { this.mkt_plan_desc = mkt_plan_desc; }
    public String getMkt_design_desc() { return mkt_design_desc; }
    public void setMkt_design_desc(String mkt_design_desc) { this.mkt_design_desc = mkt_design_desc; }
    public String getMkt_manager_desc() { return mkt_manager_desc; }
    public void setMkt_manager_desc(String mkt_manager_desc) { this.mkt_manager_desc = mkt_manager_desc; }
    public String getVp_desc() { return vp_desc; }
    public void setVp_desc(String vp_desc) { this.vp_desc = vp_desc; }
    public String getSelect_item() { return select_item; }
    public void setSelect_item(String select_item) { this.select_item = select_item; }
}