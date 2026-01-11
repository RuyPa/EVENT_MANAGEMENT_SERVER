package com.mobile_app_server.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class PCRFMessage implements Delayed {

    private String topic;
    private PCRFRequest pcrfRequest;
    private long timeDelayConfig;
    private long sendTime;

    @Override
    public String toString() {
        return "PCRFMessage [topic=" + topic + ", pcrfRequest=" + pcrfRequest + "]";
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(
                sendTime - System.currentTimeMillis(),
                TimeUnit.MILLISECONDS
        );
    }

    @Override
    public int compareTo(Delayed o) {
        return Long.compare(
                this.sendTime,
                ((PCRFMessage) o).sendTime
        );
    }
}