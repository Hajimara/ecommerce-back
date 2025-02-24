package com.shop.constant;

public class ConstantTime {
    private ConstantTime() {}

    public static final int SECOND = 1000; // 1초 = 1000ms
    public static final int MINUTE = 60 * SECOND; // 1분 = 60초
    public static final int HOUR = 60 * MINUTE; // 1시간 = 60분
    public static final int DAY = 24 * HOUR; // 1일 = 24시간
    public static final int WEEK = 7 * DAY; // 1주 = 7일
}
