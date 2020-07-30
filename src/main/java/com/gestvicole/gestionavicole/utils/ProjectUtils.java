package com.gestvicole.gestionavicole.utils;

import com.gestvicole.gestionavicole.utils.dashboard.GraphBody;
import pl.allegro.finance.tradukisto.MoneyConverters;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ProjectUtils {

    public static List<GraphBody> getGraphBodies(HashMap<String, Integer> hashMap) {
        List<GraphBody> ret = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : hashMap.entrySet()) {
            ret.add(new GraphBody(entry.getKey(), entry.getValue()));
        }
        return ret;
    }

    public static Date toDate(String dateSource, String pattern) throws ParseException {
        return new SimpleDateFormat(pattern).parse(dateSource);
    }

    public static String toString(Date date, String pattern) throws ParseException {
        return new SimpleDateFormat(pattern).format(date);
    }

    public static String toString(Date date, String pattern, Locale locale) throws ParseException {
        return new SimpleDateFormat(pattern, locale).format(date);
    }

    public static List<Date> getLast7Days() {
        List<Date> ret = new LinkedList<>();
        try {
            Date today = new Date();
            String maxDate = toString(today, "yyyy-MM-dd");
            Calendar cal = Calendar.getInstance();
            cal.setTime(toDate(maxDate, "yyyy-MM-dd"));
            for (int i = 1; i <= 7; i++) {
                ret.add(cal.getTime());
                cal.add(Calendar.DAY_OF_WEEK, -1);
            }
            Collections.reverse(ret);
        } catch (ParseException e) {
            e.printStackTrace(System.err);
        }
        return ret;
    }

    public static List<Date> getLast12Months()  {
        List<Date> ret = new LinkedList<>();
        try {
            Date today = new Date();
            String maxDate = toString(today, "MM-yyyy");
            Calendar cal = Calendar.getInstance();
            cal.setTime(toDate(maxDate, "MM-yyyy"));
            for (int i = 1; i <= 12; i++) {
                ret.add(cal.getTime());

                cal.add(Calendar.MONTH, -1);
            }
            Collections.reverse(ret);
        } catch (ParseException e) {
            e.printStackTrace(System.err);
        }
        return ret;
    }

    public static String generateStringNumber(long id) {
        String number;
        if (id < 10) {
            number = "00" + id + "";
        } else if (id < 100) {
            number = "0" + id + "";
        } else {
            number = id + "";
        }
        return number;
    }

    public static String convertNumberToWord(Double number) {
        MoneyConverters converter = MoneyConverters.FRENCH_BANKING_MONEY_VALUE;
        return converter.asWords(new BigDecimal(number)).replace(" € 00/100", "");
    }

    public static String formatNumber(double number) {
        return NumberFormat.getNumberInstance(Locale.FRENCH).format(number);
    }

    public static String formatNumber(long number) {
        return NumberFormat.getNumberInstance(Locale.FRENCH).format(number);
    }

}
