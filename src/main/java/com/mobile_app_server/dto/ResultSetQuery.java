package com.mobile_app_server.dto;

import java.util.Date;

public interface ResultSetQuery {
    Integer getId();
    String getUserId();
    String getStarttime();
    String getName();
    String getEndtime();
    String getHostname();
    String getLocation();
    String getAddress();
    String getCity();
    String getDes();
    String getEventvideo();
    String getRegistrationtype();
    String getWebsitelink();
    String getImgurl();
    Date getStartdate();
    Date getEnddate();
    String getCatename();
    Integer getChecked();
}
