package com.imuhao.pictureeveryday.utils;

/**
 * @author Smile
 * @time 2016/6/22  16:08
 * @desc ${TODD}
 */
public class Contance {

  public static final String BASE_URL = "http://gank.io/api/";
  //福利页面Url
  public static final String FULI_URL = "http://gank.io/api/data/福利/%s/%i";

  public static String getFuliUrl(int pageSize, int index) {
    return FULI_URL.replaceAll("%s", String.valueOf(pageSize))
        .replaceAll("%i", String.valueOf(index));
  }

  //标签
  public static final String FlagFragment = "Flag";
  public static final String FlagWelFare = "福利";
  public static final String FlagAndroid = "Android";
  public static final String FlagIOS = "iOS";
  public static final String FlagVideo = "休息视频";
  public static final String FlagJS = "前端";
  public static final String FlagExpand = "拓展资源";
  public static final String FlagRecommend = "瞎推荐";
  public static final String FlagAPP = "App";

  public static final String[] TITLES = {
      Contance.FlagAndroid, Contance.FlagIOS, Contance.FlagVideo, Contance.FlagJS,
      Contance.FlagExpand, Contance.FlagRecommend, Contance.FlagAPP
  };
}
