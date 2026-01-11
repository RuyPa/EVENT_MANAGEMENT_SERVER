package com.mobile_app_server.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class PCRFRequest implements Cloneable {

    private String usrMSISDN;
    private String srvName;
    private String QTAVALUE;
    private LocalDateTime srvStartDateTime;
    private LocalDateTime srvEndDateTime;

    @Override
    public String toString() {
        return "PCRFRequest{" +
                "usrMSISDN='" + usrMSISDN + '\'' +
                ", srvName='" + srvName + '\'' +
                ", QTAVALUE='" + QTAVALUE + '\'' +
                ", srvStartDateTime=" + srvStartDateTime +
                ", srvEndDateTime=" + srvEndDateTime +
                '}';
    }

    @Override
    public PCRFRequest clone() {
        try {
            return (PCRFRequest) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
