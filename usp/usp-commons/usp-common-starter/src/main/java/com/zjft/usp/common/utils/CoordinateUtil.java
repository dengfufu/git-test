package com.zjft.usp.common.utils;

import com.zjft.usp.common.vo.Coordinate;

import static java.lang.StrictMath.*;

/**
 * 地图坐标系转换工具类
 *
 * @author Qiugm
 * @date 2019-08-15 11:28
 * @version 1.0.0
 **/
public class CoordinateUtil {
    private static final double pi = 3.14159265358979324;
    private static final double a = 6378245.0;
    private static final double ee = 0.00669342162296594323;
    private static final double x_pi = 3.14159265358979324 * 3000.0 / 180.0;
    public static double EARTH_RADIUS = 6378.137;

    private static boolean outOfChina(double lat, double lon) {
        return lon < 72.004 || lon > 137.8347 || lat < 0.8293 || lat > 55.8271;

    }

    private static double transformLat(double x, double y) {
        double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * sqrt(abs(x));
        ret += (20.0 * sin(6.0 * x * pi) + 20.0 * sin(2.0 * x * pi)) * 2.0 / 3.0;
        ret += (20.0 * sin(y * pi) + 40.0 * sin(y / 3.0 * pi)) * 2.0 / 3.0;
        ret += (160.0 * sin(y / 12.0 * pi) + 320 * sin(y * pi / 30.0)) * 2.0 / 3.0;

        return ret;
    }

    private static double transformLon(double x, double y) {
        double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * sqrt(abs(x));
        ret += (20.0 * sin(6.0 * x * pi) + 20.0 * sin(2.0 * x * pi)) * 2.0 / 3.0;
        ret += (20.0 * sin(x * pi) + 40.0 * sin(x / 3.0 * pi)) * 2.0 / 3.0;
        ret += (150.0 * sin(x / 12.0 * pi) + 300.0 * sin(x / 30.0 * pi)) * 2.0 / 3.0;

        return ret;
    }

    /**
     * 地球坐标转换为火星坐标 World Geodetic System ==> Mars Geodetic System Map:
     * latitude,longitude
     *
     * @param wgs
     *            Coordinate 地球坐标
     * @return Coordinate 火星坐标
     */
    public static Coordinate transform2Mars(Coordinate wgs) {
        Coordinate marsCoordinate = new Coordinate();

        double wgLat = wgs.getLat();
        double wgLon = wgs.getLon();

        if (CoordinateUtil.outOfChina(wgLat, wgLon)) {
            marsCoordinate.setLon(wgLon);
            marsCoordinate.setLat(wgLat);

            return marsCoordinate;
        }

        // 地球坐标转火星坐标算法
        double dLat = transformLat(wgLon - 105.0, wgLat - 35.0);
        double dLon = transformLon(wgLon - 105.0, wgLat - 35.0);
        double radLat = wgLat / 180.0 * pi;
        double magic = sin(radLat);
        magic = 1 - ee * magic * magic;
        double sqrtMagic = sqrt(magic);
        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
        dLon = (dLon * 180.0) / (a / sqrtMagic * cos(radLat) * pi);

        marsCoordinate.setLon(wgLon + dLon);
        marsCoordinate.setLat(wgLat + dLat);

        return marsCoordinate;
    }

    /**
     * 火星坐标转换为百度坐标 Mars Geodetic System ==> BaiDu Geodetic System Map:
     * latitude,longitude
     *
     * @param mgs
     *            Coordinate 火星坐标
     * @return Coordinate 百度坐标
     */
    public static Coordinate bdEncrypt(Coordinate mgs) {
        Coordinate bdCoordinate = new Coordinate();
        double x = mgs.getLon();
        double y = mgs.getLat();
        double z = sqrt(x * x + y * y) + 0.00002 * sin(y * x_pi);
        double theta = atan2(y, x) + 0.000003 * cos(x * x_pi);
        bdCoordinate.setLon(z * cos(theta) + 0.0065);
        bdCoordinate.setLat(z * sin(theta) + 0.006);
        return bdCoordinate;
    }

    /**
     * 百度坐标转火星坐标 BaiDu Geodetic System ==> Mars Geodetic System
     *
     * @param bgs
     *            Coordinate 百度坐标
     * @return Coordinate 火星坐标
     */
    public static Coordinate bdDecrypt(Coordinate bgs) {
        Coordinate marsCoordinate = new Coordinate();
        double x = bgs.getLon() - 0.0065;
        double y = bgs.getLat() - 0.006;
        double z = sqrt(x * x + y * y) - 0.00002 * sin(y * x_pi);
        double theta = atan2(y, x) - 0.000003 * cos(x * x_pi);
        marsCoordinate.setLon(z * cos(theta));
        marsCoordinate.setLat(z * sin(theta));
        return marsCoordinate;
    }

    /**
     * 百度坐标转GPS坐标 BaiDu Geodetic System ==> Mars Geodetic System
     *
     * @param bgs
     *            Coordinate 百度坐标
     * @return Coordinate GPS坐标
     */
    public static Coordinate bdToGps(Coordinate bgs) {
        Coordinate mgs = new Coordinate();
        double x = bgs.getLon() - 0.0065;
        double y = bgs.getLat() - 0.006;
        double z = sqrt(x * x + y * y) - 0.00002 * sin(y * x_pi);
        double theta = atan2(y, x) - 0.000003 * cos(x * x_pi);
        mgs.setLon(z * cos(theta));
        mgs.setLat(z * sin(theta));

        Coordinate gps = transform2Mars(mgs);
        return gps;
    }

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * 根据两点坐标计算两点距离(百度算法)
     *
     * @param lat1 纬度
     * @param lng1 经度
     * @param lat2
     * @param lng2
     * @return
     */
    public static Double getDistance(Double lat1, Double lng1, Double lat2, Double lng2) {
        if (lat1 == null || lng1 == null || lat2 == null || lng2 == null) {
            return 0d;
        }
        double radLat1 = rad(lat1);

        double radLat2 = rad(lat2);

        double a = radLat1 - radLat2;

        double b = rad(lng1) - rad(lng2);

        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)

                + Math.cos(radLat1) * Math.cos(radLat2)

                * Math.pow(Math.sin(b / 2), 2)));

        s = s * EARTH_RADIUS;
        // 精确到 0.1米
        s = Math.round(s * 10000) / 10000.0 * 1000;

        return s;
    }

    public static void main(String[] args) {
        Coordinate coordinate = new Coordinate();
        coordinate.setLon(113.936211);
        coordinate.setLat(22.527523);
        Coordinate coordinate2 = new Coordinate();
        coordinate2.setLat(22.542617548610533);
        coordinate2.setLon(113.9498035726628);
        Coordinate marsCoordinate = transform2Mars(coordinate);
        System.out.println(marsCoordinate.getLon());
        System.out.println(marsCoordinate.getLat());
        Coordinate marsCoordinate2 = bdDecrypt(coordinate2);
        System.out.println(marsCoordinate2.getLon());
        System.out.println(marsCoordinate2.getLat());

        double i = getDistance(23d, 113d, 23d, 113.5);
        System.out.println(i);
    }

}
