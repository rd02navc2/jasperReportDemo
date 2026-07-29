package com.beyoung.parking.util;

public class ErrCodeConst {
	public static final String finished = "0000";
	public static final String finished_message = "作業完成";
	public static final String duplicate = "0001";
	public static final String duplicate_message = "會員資料已存在";
	public static final String not_found = "0002";
	public static final String not_found_message = "找不到會員資料";
	public static final String pos_used = "0004";
	public static final String pos_used_message = "POS 已經有這張發票的累積點數記錄";
	public static final String expired = "0005";
	public static final String expired_message = "這張發票已經過期(7 days)";
	public static final String erp_used = "0006";
	public static final String erp_used_message = "這張發票在 ERP 已經有累積點數記錄";
	public static final String pos_not_found = "0007";
	public static final String pos_not_found_message = "POS 的資料找不到這張發票號碼";
	public static final String erp_not_found = "9999";
	public static final String erp_not_found_message = "ERP 找不到這張發票號碼";
	public static final String lpj_not_found = "0008";
	public static final String lpj_not_found_message = "會員資料檔找不到這筆會員資料";
	public static final String pos_refund = "0009";
	public static final String pos_refund_message = "這張發票已經退貨";
	public static final String parking_used = "0010";
	public static final String parking_used_message = "這張發票已經停車補登過";
	public static final String parking_not_today = "0011";
	public static final String parking_not_today_message = "這張發票不是今日的交易發票";
	public static final String pos_parking_not_found = "0012";
	public static final String pos_parking_not_found_message = "今日 POS 資料找不到這張發票號碼";
	public static final String parking_not_found = "0013";
	public static final String parking_not_found_message = "查無停車資料";
	public static final String not_validate = "0014";
	public static final String not_validate_message = "找不到會員卡或者不是有效卡";
	
	public static final String pos_linepay_payment = "9999";
	public static final String pos_linepay_payment_message = "LinePay payment failure";
	public static final String pos_linepay_payment_detail = "9998";
	public static final String pos_linepay_payment_detail_message = "LinePay inquire detail failure";
	public static final String pos_linepay_refund_inv_not_found = "9997";
	public static final String pos_linepay_refund_inv_not_found_message = "The invoice does not use LinePay";
	public static final String pos_linepay_refund = "9996";
	public static final String pos_linepay_refund_message = "LinePay refund failure";
	public static final String pos_linepay_refund_detail = "9995";
	public static final String pos_linepay_refund_detail_message = "LinePay refund inquire detail failure";

	public static final String pos_action_check_barcode = "9999";
	public static final String pos_action_check_barcode_message = "此條碼無法辨識";

	//reading space
	public static final String pos_rs_repeat = "9999";
	public static final String pos_rs_repeat_message = "今日已交易";
	public static final String pos_rs_not_found = "9998";
	public static final String pos_rs_not_found_message = "查無會員資料";
	public static final String pos_rs_used = "9997";
	public static final String pos_rs_used_message = "已使用不可退貨";
	public static final String pos_rs_no_pay = "9996";
	public static final String pos_rs_no_pay_message = "查無交易資料";
	public static final String pos_rs_not_exit = "9995";
	public static final String pos_rs_not_exit_message = "已入場尚未出場";
	public static final String pos_rs_refunded = "9994";
	public static final String pos_rs_refunded_message = "此發票之前已退貨";
	public static final String pos_rs_over = "9993";
	public static final String pos_rs_over_message = "超過使用人數";
	public static final String pos_rs_point_not_enough = "9992";
	public static final String pos_rs_point_not_enough_message = "會員點數不足";
	public static final String pos_rs_erp_ws = "9991";
	public static final String pos_rs_erp_ws_message = "扣點發生錯誤";
	public static final String pos_rs_name_not_found = "9990";
	public static final String pos_rs_name_not_found_message = "查無會員姓名";
	
	//door control
	public static final String dc_not_found = "9999";
	public static final String dc_not_found_message = "查無會員資料";
	public static final String dc_hr_not_found = "9998";
	public static final String dc_hr_not_found_message = "查無員工資料";

	//回傳甚麼，APP直接秀
	public static final String append_exclude_counter = "A001";
	public static final String append_exclude_counter_message = "此專櫃無法累積點數";
	
	//shuttle bus
	public static final String sb_finished = "0000";
	public static final String sb_finished_message = "歡迎光臨";
	public static final String sb_not_found = "9999";
	public static final String sb_not_found_message = "查無會員資料";
	public static final String sb_000 = "9998";
	public static final String sb_000_message = "卡號尚未註冊";
	public static final String sb_duplicate = "9997";
	public static final String sb_duplicate_message = "會員重複掃描";
	
	//vip_room
	public static final String vip_room_rs_not_found = "9999";
	public static final String vip_room_rs_not_found_message = "查無會員資料";
	public static final String vip_room_rs_not_vip = "9998";
	public static final String vip_room_rs_not_vip_message = "該會員不是VIP";
	public static final String vip_room_rs_repeat = "9997";
	public static final String vip_room_rs_repeat_message = "今日已Booking，不需重複Booking";
	public static final String vip_room_rs_used = "9996";
	public static final String vip_room_rs_used_message = "今日已入場，不可取消Booking";
	public static final String vip_room_rs_no_pay = "9995";
	public static final String vip_room_rs_no_pay_message = "無Booking資料";
	public static final String vip_room_rs_refunded = "9994";
	public static final String vip_room_rs_refunded_message = "之前已取消Booking，不需重複取消";
	
	//pace
	public static final String pos_pace_finished = "0000";
	public static final String pos_pace_finished_message = "交易完成";
	public static final String pos_pace_fail = "9999";
	public static final String pos_pace_fail_message = "交易失敗";
	public static final String pos_pace_not_found = "9998";
	public static final String pos_pace_not_found_message = "查無資料";
	public static final String pos_pace_refund_fail = "9997";
	public static final String pos_pace_refund_fail_message = "退款失敗";
	public static final String pos_pace_barcode_fail = "9996";
	public static final String pos_pace_barcode_fail_message = "條碼認證失敗";
	
	//AD
	public static final String ad_connect_error = "9999";
	public static final String ad_connect_error_message = "連線失敗";
	public static final String ad_user_not_found = "9998";
	public static final String ad_user_not_found_message = "帳號或密碼無法認證";
	
	//bppay
	public static final String pos_bppay_error = "9999";
	public static final String pos_bppay_error_message = "BeyondPay 付款失敗";
}
