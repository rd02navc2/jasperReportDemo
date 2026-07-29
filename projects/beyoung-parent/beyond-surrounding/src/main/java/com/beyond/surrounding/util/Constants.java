package com.beyond.surrounding.util;

import java.math.BigDecimal;
import java.util.regex.Pattern;
public final class Constants { 

	private Constants() {
		throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
	}
	
	/* ─────────────────────────────────────────────────────────────
	    環境配置欄位 (注意：無 final，允許系統啟動時由設定檔動態寫入)
	   ───────────────────────────────────────────────────────────── */
	public static int PARKING_STATUS = 2;
	public static String BEYOND_WEB_SERVICE = "";
	public static String BEYOND_ERP_SERVICE = "";
	public static String BEYOND_REDEEM_SERVICE = "";
	public static String BEYOND_PARKTRON_SERVER = "";
	
	public static int BATCH_SIZE = 20;
	
	public static String VALIDATE_RESET_PASSWORD_URL = "";
	public static String RESET_MOBILE_PASSWORD_URL = "";
	
	public static String MAIL_USER_NAME = "";
	public static String MAIL_PASSWORD = "";
	public static String MAIL_HOST = "";
	public static String MAIL_PORT = "";
	public static String MAIL_STARTTLS = "";
	public static String MAIL_FROM_MAIL = "";
	
	public static String SYSTEM_PATH = "";
	public static String PICTURE_SERVER_URL = "";
	public static String PUSH_APNS_P12_FILE = "";
	public static String PUSH_APNS_PWD = "";

	/* ─────────────────────────────────────────────────────────────
	   ■ 固定常數定義 (真正的常數，皆有 final)
	   ───────────────────────────────────────────────────────────── */
	public static final BigDecimal VIP_AMOUNT_THRESHOLD = BigDecimal.valueOf(50000.0);
	
	public static final String BEYONDPAY_UNSUPPORTED_CODE = "1234";
	public static final String BEYONDPAY_UNSUPPORTED_MSG = "不支援此項電子支付";
	
	public static final String BEYONDPAY_CODE_MISMATCH = "1235";
	public static final String BEYONDPAY_CODE_MISMATCH_MSG = "電子支付對應錯誤";
		
	public static final Pattern VALID_TIME = Pattern.compile("[012][0-9]:[0-5][0-9]");
	public static final String DATE_FORMAT = "yyyy-MM-dd";
	public static final String DATETIME_FORMAT = "HH:mm";
	
	public static final String TOKEN_MANAGER = "TokenManager";
	
	// 系統通用狀態
	public static final int SYSTEM_PROBLEM = 0;
	public static final int SUCCESS = 1;
	public static final int FAIL = -1;
	
	// 會員檢核狀態碼
	public static final int BEYOND_ORIGINAL_USER = 2;
	public static final int ID_OR_PASSWORD_NULL = 3;
	public static final int MAIL_INVALIDATE = 4;
	public static final int BIRTHDAY_INVALIDATE = 5;
	public static final int TWO_PASSWORD_NOT_MATCH = 6;
	public static final int DATA_NULL = 7;
	public static final int FB_REGISTED = 8;
	public static final int PHONE_INVALIDATE = 9;
	public static final int PASSWORD_INVALIDATE = 11;
	public static final int USER_NOT_REGISTER = 20;
	public static final int LOGIN_ID_OR_PASSWORD_NULL = 21;
	public static final int LOGIN_ID_OR_PASSWORD_INVALIDATE = 22;
	public static final int NOT_ENOUGH_POINT_ERROR = 23;
	public static final int MEMBER_DATA_FAIL = 24;
	public static final int RESET_TOKEN_EXPIRE = 25;
	
	// Token 驗證狀態
	public static final int TOKEN_USER_LOGOUT = 30;
	public static final int TOKEN_USER_BANNED = 31;
	public static final int TOKEN_USER_SUCCESS = 1;
	
	// 發票檢核狀態碼
	public static final int RECEIPT_EXPIRED = 50;
	public static final int RECEIPT_USED = 51;
	public static final int RECEIPT_INVALIDATE = 52;
	public static final int RECEIPT_NO_RECEIPT = 53;
	public static final int RECEIPT_NOT_MATCH_TARGET_PURCHASE = 54;
	public static final int RECEIPT_NO_COUPON = 55;
	
	public static final int PASSWORD_MIN_LENGTH = 4;
	public static final int PASSWORD_MAX_LENGTH = 20;

	// 禮券交易狀態
	public static final int COUPON_TRANSACTION_NORMAL = 1;
	public static final int COUPON_TRANSACTION_CANCEL = 2;
	public static final int COUPON_TRANSACTION_GET_BY_POS = 3;
	public static final int COUPON_TRANSACTION_SUCCESS = 4;
	public static final int COUPON_TRANSACTION_WITHDRAW = 5;
	
	public static final String TRANSACTION_MESSAGE_PREPARE = "準備交易";
	public static final String TRANSACTION_MESSAGE_CANCEL = "交易取消";
	public static final String TRANSACTION_MESSAGE_SUCCESS = "交易成功";
	public static final String TRANSACTION_MESSAGE_FAIL = "交易失敗";
	public static final String TRANSACTION_MESSAGE_TIMEOUT = "交易逾期";
	public static final int TRANSACTION_TIME_OUT_MINUTE = 3;
	
	// 系統提示訊息
	public static final String SERVER_EROOR = "系統發生錯誤！";
	public static final String MS_WEB_SERVICE_ERROR = "取得資料錯誤！";
	
	public static final String MSG_SUCCESS = "成功";
	public static final String MSG_LOGIN_SUCCESS = "登入成功。";
	public static final String MSG_LOGIN_SYSTEM_PROBLEM = "系統發生問題！";
	public static final String MSG_LOGIN_MEMBER_NOT_REGISTER = "會員尚未註冊。";
	public static final String MSG_ORIGINAL_MEMBER = "原比漾會員。";
	public static final String MSG_MAIL_INVALIDATE = "Email格式錯誤。";
	public static final String MSG_BIRTHDAY_INVALIDATE = "生日格式錯誤。";
	public static final String MSG_PHONE_INVALIDATE = "電話格式錯誤。";
	public static final String MSG_ID_PASSWORD_INVALIDATE = "身份證或密碼錯誤。";
	public static final String MSG_PASSWORD_INVALIDATE = "密碼錯誤。";
	public static final String MSG_DATA_ERROR = "資料錯誤！";
	public static final String MSG_REGISTER_SUCCESS = "註冊成功。";
	public static final String MSG_PASSWORD_NULL = "密碼不可為空白。";
	public static final String MSG_REPASSWORD_ERROR = "兩組密碼不一致。";
	public static final String MSG_ID_EMAIL_FAIL = "身份證或Email錯誤。";
	public static final String MSG_LOGOUT_SUCCESS = "登出成功。";
	public static final String MSG_LOGOUT_FAIL = "登出失敗。";
	public static final String MSG_PASSWORD_RESET_SUCCESS = "重設密碼成功！";
	public static final String MSG_USER_LOGOUT = "使用者已登出！";
	public static final String MSG_USER_BANNED = "使用者已被停權！";
	public static final String MSG_NOT_ENOUGH_POINT_ERROR = "點數不足！";
	public static final String MSG_RESET_TOKEN_EXPIRE = "重設密碼時間失效";
	
	public static final String MSG_RECEIPT_EXPIRED = "發票已經超過7天登錄！";
	public static final String MSG_RECEIPT_USED = "發票已經被登錄點數！";
	public static final String MSG_RECEIPT_APP_USED = "發票已經被APP登錄！";
	public static final String MSG_RECEIPT_INVALIDATE = "發票資料錯誤，請至VIP櫃台處理";
	public static final String MSG_RECEIPT_REDEEM_USED = "有發票已被兌換贈獎。";
	public static final String MSG_RECEIPT_NO_COUPON = "沒有足夠的電子禮券，請至贈獎櫃台兌換！";
	public static final String MSG_RECEIPT_NOT_MATCH_TARGET_PURCHASE = "消費未達兌換門檻！";
	public static final String MSG_RECEIPT_5_MINS = "資料處理中，請稍候再試，或至VIP櫃台處理";
	
	// 郵件範本
	public static final String CUSTOMER_SERVICE_SUBJECT = "APP顧客意見回覆";
	public static final String CUSTOMER_SERVICE_CONTENT = "館別資訊：%s。\n\n"
														+ "留言者姓名：%s。\n\n"
														+ "eMail：%s。\n\n"
														+ "聯絡電話：%s。\n\n"
														+ "內容：%s。";
	
	public static final String FORGET_PASSWORD_SUBJECT = "重設比漾廣場APP密碼";
	public static final String FORGET_PASSWORD_CONTENT = "親密的客戶您好，\n\n請點擊以下的連結回到比漾廣場APP設定一組新密碼。\n\n%s\n\n(連結有效期限：%s)\n\n若您之前在比漾廣場APP註冊帳號或是有其他疑問，歡迎來信客服信箱 crdep@beyondplaza.com.tw";
	
	public static final String CONTENT_TYPE_JSON = "application/json;charset=utf-8";
	
	// JSON 欄位 key 值
	public static final String JSON_CODE = "code";
	public static final String JSON_MESSAGE = "message";
	
	public static final int JSON_INT_SUCCESS = 1;
	public static final int JSON_INT_WARNING = 2;
	public static final int JSON_INT_FAIL = -1;
	public static final int JSON_INT_USER_LOGOUT = -2;
	public static final int JSON_INT_USER_BANNED = -3;
	public static final String JSON_SUCCESS = "success";
	public static final String JSON_FAIL = "fail";
	public static final String JSON_ERROR = "error";
	public static final String JSON_DATA = "data";
	public static final String JSON_FORCE_UPDATE = "force_update";
	public static final String JSON_LINK = "link";
	
	public static final String JSON_USER_ID = "user_id";
	public static final String JSON_TOKEN = "token";
	public static final String JSON_CARD_ID = "card_id";
	
	public static final String JSON_USER_NAME = "userName";
	public static final String JSON_USER_EMAIL = "email";
	public static final String JSON_USER_TELPHONE = "telphone";
	public static final String JSON_USER_ADDRESS = "address";
	public static final String JSON_USER_BIRTHDAY = "birthday";
	
	public static final String JSON_USER_OLD_PASSWORD = "oldpassword";
	public static final String JSON_USER_PASSWORD = "password";
	public static final String JSON_USER_RE_PASSWORD = "repassword";
	
	public static final String RECEIPT_COMPUTE = "0";
	public static final String RECEIPT_REDEEM = "1";

	public static final String IMAGE_SIZE_LARGE = "large";
	public static final String IMAGE_SIZE_MEDIAN = "median";
	public static final String IMAGE_SIZE_SMALL = "small";
	
	// 比漾商業邏輯代碼
	public static final String BEYOND_BRANCH_ID = "BY001";
	public static final String BEYOND_CODE_SUCCESS = "0000";
	public static final String BEYOND_CODE_DATA_DUPLICATED = "0001";
	public static final String BEYOND_CODE_DATA_FAIL = "0002";
	public static final String BEYOND_CODE_NO_DATA_FAIL = "0003";
	public static final String BEYOND_APP_CARD_TYPE = "APP";
	public static final String BEYOND_TEMP_APP_CARD_TYPE = "000";
	public static final String BEYOND_USER_ID_PREFIX = "APP";
	
	public static final int BEYOND_RECEIPT_NORMAL = 1;
	public static final int BEYOND_RECEIPT_CAMPAIGN_STATE_SUCCESS = 3;
	public static final int BEYOND_RECEIPT_WITHDRAW = -1;

	public static final int BEYOND_RECEIPT_POINT_STATE_EXPIRED = -2;
	public static final int BEYOND_RECEIPT_POINT_STATE_USED = -3;
	public static final int BEYOND_RECEIPT_POINT_STATE_INVALIDATE = -4;
	public static final int BEYOND_RECEIPT_POINT_STATE_APP_USED = -5;
	
	public static final int BEYOND_RECEIPT_CAMPAIGN_STATE_EXPIRED = -6;
	public static final int BEYOND_RECEIPT_CAMPAIGN_STATE_NOT_START = -7;
	public static final int BEYOND_RECEIPT_CAMPAIGN_STATE_USED = -8;
	public static final int BEYOND_RECEIPT_CAMPAIGN_STATE_INVALIDATE = -9;
	public static final int BEYOND_RECEIPT_CAMPAIGN_STATE_UNAVAILABLE = -10;
	
	public static final String BEYOND_RECEIPT_POINT_STATE_EXPIRED_MESSAGE = "已過期";
	public static final String BEYOND_RECEIPT_POINT_STATE_USED_MESSAGE = "已集點";
	public static final String BEYOND_RECEIPT_STATE_WITHDRAW = "已退貨";
	public static final String BEYOND_RECEIPT_CAMPAIGN_STATE_USED_MESSAGE = "已兌換";

	public static final String BEYOND_RECEIPT_POINT_USED_BY_POS = "0004"; 
	public static final String BEYOND_RECEIPT_POINT_EXPIRED = "0005";     
	public static final String BEYOND_RECEIPT_POINT_USED_BY_ERP = "0006"; 
	public static final String BEYOND_RECEIPT_POINT_INVALIDATE = "0007";  
	public static final String BEYOND_RECEIPT_POINT_WITHDRAW = "0009";    
	
	public static final String BEYOND_RECEIPT_CAMPAIGN_UNAVAILABLE = "5001";   
	public static final String BEYOND_RECEIPT_CAMPAIGN_USED = "5002";          
	public static final String BEYOND_RECEIPT_CAMPAIGN_NOT_START = "5003";     
	public static final String BEYOND_RECEIPT_CAMPAIGN_TIME_ERROR = "5008";     
	public static final String BEYOND_RECEIPT_CAMPAIGN_WITHDRAW = "5007";     
	public static final String BEYOND_RECEIPT_CAMPAIGN_PARAMETER_ERROR = "9999"; 
	
	public static final String BEYOND_RECEIPT_CAMPAIGN_NOT_START_MESSAGE = "活動尚未開始"; 
	public static final String BEYOND_RECEIPT_CAMPAIGN_USED_MESSAGE = "發票重覆兌換"; 
	public static final String BEYOND_RECEIPT_CAMPAIGN_UNAVAILABLE_MESSAGE = "本筆消費無兌換活動"; 
	public static final String BEYOND_RECEIPT_CAMPAIGN_PARAMETER_ERROR_MESSAGE = "發票資料發生問題"; 
	public static final String BEYOND_RECEIPT_CAMPAIGN_WITHDRAW_MESSAGE = "發票退貨無法兌換"; 
	
	public static final String BEYOND_RECEIPT_REDEEEM_FIELD_COUPON_NO = "coupon_no_all";
	public static final String BEYOND_RECEIPT_REDEEEM_FIELD_OTHER = "other_all";
	public static final String BEYOND_RECEIPT_REDEEEM_FIELD_COUPON = "coupon";
	public static final String BEYOND_RECEIPT_REDEEEM_FIELD_ITEM_NAME = "item_name";
	public static final String BEYOND_RECEIPT_REDEEEM_FIELD_COMMENT = "comment";
	
	public static final String BEYOND_RECEIPT_REDEEEM_USED = "5001";
	public static final String BEYOND_RECEIPT_REDEEEM_NOT_REACH_TARGET = "5002"; 
	public static final String BEYOND_RECEIPT_REDEEEM_NO_COUPON = "5003"; 
	public static final String BEYOND_RECEIPT_REDEEEM_WITHDRAW = "5007"; 
	public static final String BEYOND_RECEIPT_REDEEEM_OTHER_ERROR = "9998";  
	public static final String BEYOND_RECEIPT_REDEEEM_PARAMETER_ERROR = "9999";  
	
	public static final String BEYOND_RECEIPT_REDEEEM_NO_RECEIPT = "1001"; 
	public static final String BEYOND_RECEIPT_APP_USED = "1002"; 

	public static final String BEYOND_RECEIPT_POINT_5_MINS = "1003";    
	
	public static final String BEYOND_DIRECT_ANSWER_PREFIX = "A";    
	public static final String BEYOND_DIRECT_ANSWER = "1999";    
	
	public static final String BEYOND_CODE_ENTRY_POINT = "2";
	public static final String BEYOND_CODE_POINT_COUPON = "5";
	public static final String BEYOND_CODE_GET_POINT = "7";
	public static final String BEYOND_CODE_WITHDRAW_POINT = "8";
	public static final String BEYOND_CODE_POINT_REDEEM = "9";
	
	public static final String BEYOND_MSG_ENTRY_POINT = "補登點數";
	public static final String BEYOND_MSG_POINT_COUPON = "點數兌換";
	public static final String BEYOND_MSG_POINT_COUPON_WITHDRAW = "點數兌換退還點數";
	public static final String BEYOND_MSG_GET_POINT = "消費得點";
	public static final String BEYOND_MSG_WITHDRAW_POINT = "退貨還點";
	public static final String BEYOND_MSG_POINT_REDEEM = "點數折抵消費";
	public static final String BEYOND_MSG_POINT_REDEEM_WITHDRAW = "點數折抵消費取消";
	
	public static final String ERP_SUCCESS = "0";
	public static final String ERP_MEMBER_DATA_ERROR = "TSD0612";
	public static final String ERP_INSUFFICIENT_POINT = "alm-833";
	public static final String ERP_SUCCESS_MSG = "兌換成功";
	public static final String ERP_MEMBER_DATA_ERROR_MSG = "會員資料錯誤";
	public static final String ERP_INSUFFICIENT_POINT_MSG = "會員點數不足";
	
	public static final String BEYOND_MSG_CAMPAIGN_REDEEM_BUTTON = "限今日兌換(%s)";
	public static final String NOT_MEMBER = "未註冊";
}