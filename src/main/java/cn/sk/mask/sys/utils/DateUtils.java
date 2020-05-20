package cn.sk.mask.sys.utils;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

    /**
     * 日期工具类
     */
    public class DateUtils {
        public static final DateFormat dateFomat = new SimpleDateFormat("yyyy-MM-dd");
        public static final String STANDARD_FORMAT = "yyyy-MM-dd HH:mm:ss";
        public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
        public static final String YYYYMMDD = "yyyyMMdd";
        public static final String YYYYMMDD2 = "yyyy/MM/dd";
        public static final String YYYYMM = "yyyyMM";
        public static final String YYYY = "yyyy";
        public static final String MMDDHHMM = "MM-dd HH:mm";

        private DateUtils(){}

        /**
         * 获取系统当前时间
         * @param format 时间格式
         * @return
         */
        public static String getSystemTime(String format){
       /* Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);
        if(month<10){
            return year+"-0"+month+"-"+day+" "+hour+":"+minute+":"+second;
        }else{
            return year+"-"+month+"-"+day+" "+hour+":"+minute+":"+second;
        }*/
            Date date = new Date();
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            return simpleDateFormat.format(date);

        }

        public static Date strToDate(String dateTimeStr, String formatStr){
            DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(formatStr);
            DateTime dateTime = dateTimeFormatter.parseDateTime(dateTimeStr);
            return dateTime.toDate();
        }

        public static String dateToStr(Date date,String formatStr){
            if(date == null){
                return StringUtils.EMPTY;
            }
            DateTime dateTime = new DateTime(date);
            return dateTime.toString(formatStr);
        }

        public static Date strToDate(String dateTimeStr){
            DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(STANDARD_FORMAT);
            DateTime dateTime = dateTimeFormatter.parseDateTime(dateTimeStr);
            return dateTime.toDate();
        }

        public static String dateToStr(Date date){
            if(date == null){
                return StringUtils.EMPTY;
            }
            DateTime dateTime = new DateTime(date);
            return dateTime.toString(STANDARD_FORMAT);
        }




        /**
         * 获取前/后n天日期(M月d日)
         *
         * @return
         */
        public static String getMonthDay(int diff) {
            DateFormat df = new SimpleDateFormat("M月d日");
            Calendar c = Calendar.getInstance();
            c.add(Calendar.DAY_OF_YEAR, diff);
            return df.format(c.getTime());
        }
        /**
         * 获取时间
         * @return
         */
        public static long getCreateTime(){
            return new Date().getTime();
        }
        /**
         * 截取时间..
         * 2015/04/16时间转换成 2015-04-16
         * @param strdate
         * @return
         */
        public static String subRestDate(String strdate){
            String newDate="";
            if(strdate!=null&&newDate!=""){
                newDate=strdate.replace("/", "-").substring(0, 10);
            }else{
                newDate="";
            }
            return newDate;
        }
        /**
         * 获取年
         * @param date
         * @return
         */
        public static int getYear(Date date) {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            return c.get(1);
        }

        /**
         * 获取月份
         * @param date
         * @return
         */
        public static int getMonth(Date date) {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            return c.get(2) + 1;
        }
        /**
         * 获取天
         * @param date
         * @return
         */
        public static int getDay(Date date) {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            return c.get(5);
        }
        /**
         * 获取时
         * @param date
         * @return
         */
        public static int getHour(Date date) {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            return c.get(11);
        }
        /**
         * 获取分
         * @param date
         * @return
         */
        public static int getMinute(Date date) {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            return c.get(12);
        }
        /**
         * 获取秒
         * @param date
         * @return
         */
        public static int getSecond(Date date) {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            return c.get(13);
        }
        /**
         * 获取毫秒
         * @param date
         * @return
         */
        public static long getMillis(Date date) {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            return c.getTimeInMillis();
        }
        /***
         * 增加或减少天数
         * @param date
         * @param num
         * @return
         */
        public static Date addDay(Date date, int num) {
            Calendar startDT = Calendar.getInstance();
            startDT.setTime(date);
            startDT.add(Calendar.DAY_OF_MONTH, num);
            return startDT.getTime();
        }

        /**
         * 把String yyyy-MM-dd HH:mm:ss类型 的 转成Date
         * @param dateString
         * @return
         * @throws ParseException
         */
        public static Date stringCoventDate(String dateString) throws ParseException {
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = sdf.parse(dateString.replace("/", "-"));
            return date;
        }

        /**
         * date 转成String 类型，格式为yyyy-MM-dd
         *
         * @param date
         * @return
         */
        public static String dateCoventString(Date date) {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String time = df.format(date);
            return time;
        }

        /**
         * 指定时间，并且在它的基础上加多mm分钟
         * @return
         */
        public static String addOneMinuteTime(Date date,int mm){
            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            System.out.println("当前时间：" + sdf.format(date));
            Date afterDate = new Date(date.getTime() + (mm*60000));
            System.out.println(sdf.format(afterDate ));
            return sdf.format(afterDate);
        }

        /**
         * 指定时间，并且在它的基础上加多mm分钟
         * @return
         */
        public static String addOneMinuteTime(Date date,int mm,String format){
            DateFormat sdf = new SimpleDateFormat(format);
            System.out.println("当前时间：" + sdf.format(date));
            Date afterDate = new Date(date.getTime() + (mm*60000));
            System.out.println(sdf.format(afterDate ));
            return sdf.format(afterDate);
        }

        /**
         * 计算时间差---分钟表示
         * @param date1 结束时间
         * @param date2 开始时间
         * @return
         */
        public static long timeDifferenceTomm(Date date1,Date date2){
            return (date1.getTime()-date2.getTime())/60/1000;
        }
        /**
         * 计算时间差---分钟表示
         * @param date1 结束时间
         * @param date2 开始时间
         * @return
         */
        public static String timeDifferenceTomm(long date1,long date2){
            return (date1-date2)/60/1000+"分钟";
        }
        /**
         * 计算时间差---分钟表示
         * @param timestamp 开始时间戳
         * @return
         */
        public static long timeDifferenceTomm(long timestamp){
            return ((System.currentTimeMillis()/1000-timestamp)/60);
        }

        /**
         * 将date 转成指定的String 类型
         * @param date
         * @return
         */
        public static String dateToString(Date date, String format)
        {
            String result = "";
            if(date != null){
                try
                {
                    if(format==null || format.length()==0)
                    {
                        SimpleDateFormat sdf = new SimpleDateFormat(YYYYMMDD);
                        result = sdf.format(date);
                    }else{
                        SimpleDateFormat sdf = new SimpleDateFormat(format);
                        result = sdf.format(date);
                    }
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                }
            }
            return result;
        }

        /**
         * 把String转成date
         * @param dateString
         * @return format
         * @throws ParseException
         */
        public static Date stringToDate(String dateString,String format) throws ParseException {
            SimpleDateFormat sdf=new SimpleDateFormat(format);
            Date date = sdf.parse(dateString);
            return date;
        }

        /**
         * 比较两个时间大小
         * @param dt1 date1
         * @param dt2 date2
         * @return
         */
        public  static int compareDate(Date dt1,Date dt2){
            if (dt1.getTime() > dt2.getTime()) {
                System.out.println("dt1 大于dt2");
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                System.out.println("dt1 小于 dt2");
                return -1;
            } else {//相等
                return 0;
            }
        }

        /**
         * 把日期格式化成当天的初始一个时间点
         * @param date
         * @return
         */
        public static Date getDateWithStartTime(Date date) throws ParseException{
            if (date == null) return null;
            String dateStr = dateToString(date,DateUtils.YYYYMMDD);
            dateStr = dateStr + " 03:00:00";

            return stringToDate(dateStr, DateUtils.YYYYMMDDHHMMSS);
        }

        public static String getDateToYYYYMMDDhhmmss(){
            SimpleDateFormat sdf = new SimpleDateFormat(YYYYMMDDHHMMSS);
            return sdf.format(new Date());
        }

        /**
         * @param args
         * @throws ParseException
         */
        public static void main(String[] args) throws ParseException {
            //		System.out.println(formatDate(parseDate("2010/3/6")));
            //		System.out.println(getDate("yyyy年MM月dd日 E"));
            //		long time = new Date().getTime()-parseDate("2012-11-19").getTime();
            //		System.out.println(time/(24*60*60*1000));

            Date date1 = stringCoventDate("2016-09-13 11:11:03");
            Date date2 = stringCoventDate("2016-09-13 11:12:03");
//		Date date1 = new Date();
//		System.out.println(date2.getTime());
//		System.out.println(System.currentTimeMillis());
            System.out.println(timeDifferenceTomm(date2, date1));
            System.out.println(compareDate(date2, date1));
//		System.out.println(30>timeDifferenceTomm(date2.getTime()/1000));
            String xxx = "dddddddddd////";
            System.out.println("==========="+xxx.replace("/", "-"));
//        System.out.println(System.currentTimeMillis());
        }

}
