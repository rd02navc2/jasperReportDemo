package com.beyoung.surrounding.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class GetDateTime {
	public static void main(String[] args) {
		try {
			System.out.println(getTimeMilli(""));
		} catch (Exception var2) {
			var2.printStackTrace();
		}

	}

	public static String getNewDate(String sDate, String sToken, int iAddYear, int iAddMon, int iAddDay)
			throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy" + sToken + "MM" + sToken + "dd");
		Date _dDate = sdf.parse(sDate);
		Calendar _cDate = Calendar.getInstance();
		_cDate.setTime(_dDate);
		_cDate.add(1, iAddYear);
		_cDate.add(2, iAddMon);
		_cDate.add(5, iAddDay);
		Date dt1 = _cDate.getTime();
		return sdf1.format(dt1);
	}

	public static Date getNewDate(Date date, int iAddDay) {
		Calendar _cDate = Calendar.getInstance();
		_cDate.setTime(date);
		_cDate.add(5, iAddDay);
		return _cDate.getTime();
	}

	public static String getTodayDateW(String sToken) {
		Calendar date = Calendar.getInstance();
		String sTodayYear = Integer.toString(date.get(1));
		String sTodayMonth = Integer.toString(date.get(2) + 1);
		sTodayMonth = sTodayMonth.length() == 1 ? "0" + sTodayMonth : sTodayMonth;
		String sTodayDay = Integer.toString(date.get(5));
		sTodayDay = sTodayDay.length() == 1 ? "0" + sTodayDay : sTodayDay;
		return sTodayYear + sToken + sTodayMonth + sToken + sTodayDay;
	}

	public static String getTodayYearW() {
		Calendar date = Calendar.getInstance();
		String sTodayYear = Integer.toString(date.get(1));
		return sTodayYear;
	}

	public static String getTime(String sToken) {
		Calendar date = Calendar.getInstance();
		String _sHour = Integer.toString(date.get(11));
		_sHour = _sHour.length() == 1 ? "0" + _sHour : _sHour;
		String _sMin = Integer.toString(date.get(12));
		_sMin = _sMin.length() == 1 ? "0" + _sMin : _sMin;
		String _sSec = Integer.toString(date.get(13));
		_sSec = _sSec.length() == 1 ? "0" + _sSec : _sSec;
		return _sHour + sToken + _sMin + sToken + _sSec;
	}

	public static String getTimeMilli() {
		Calendar date = Calendar.getInstance();
		String _sHour = Integer.toString(date.get(11));
		_sHour = _sHour.length() == 1 ? "0" + _sHour : _sHour;
		String _sMin = Integer.toString(date.get(12));
		_sMin = _sMin.length() == 1 ? "0" + _sMin : _sMin;
		String _sSec = Integer.toString(date.get(13));
		_sSec = _sSec.length() == 1 ? "0" + _sSec : _sSec;
		String _sMilliSec = Integer.toString(date.get(14));
		return _sHour + ":" + _sMin + ":" + _sSec + "." + _sMilliSec;
	}

	public static String getTimeMilli(String sToken) {
		Calendar date = Calendar.getInstance();
		String _sHour = Integer.toString(date.get(11));
		_sHour = _sHour.length() == 1 ? "0" + _sHour : _sHour;
		String _sMin = Integer.toString(date.get(12));
		_sMin = _sMin.length() == 1 ? "0" + _sMin : _sMin;
		String _sSec = Integer.toString(date.get(13));
		_sSec = _sSec.length() == 1 ? "0" + _sSec : _sSec;
		String _sMilliSec = Integer.toString(date.get(14));
		return _sHour + sToken + _sMin + sToken + _sSec + sToken + _sMilliSec;
	}

	public static String addYearW(int iAddYear) {
		Calendar date = Calendar.getInstance();
		String sNewYear = Integer.toString(date.get(1) + iAddYear);
		return sNewYear;
	}

	public static String getWeek(String sDate) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String[] weeks = new String[]{"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};
		Calendar cal = Calendar.getInstance();
		cal.setTime(sdf.parse(sDate));
		int week_index = cal.get(7) - 1;
		if (week_index < 0) {
			week_index = 0;
		}

		return weeks[week_index];
	}

	public static String getWeekT(String sDate) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String[] weeks = new String[]{"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
		Calendar cal = Calendar.getInstance();
		cal.setTime(sdf.parse(sDate));
		int week_index = cal.get(7) - 1;
		if (week_index < 0) {
			week_index = 0;
		}

		return weeks[week_index];
	}
}