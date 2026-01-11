package com.mobile_app_server.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PartnerPCRFConfig {

    //public String isdn;
    public int partnerId;
    public String partnerName;
    public String partnerKey;
    //public String queueInTmp;
    public String queueName;
    public String pcrfInfo;
    public String[] profile;
    public String[] prefix;
    public String[] suffix;
    public int isEncrypt;
    public int isPrefix;
    public int prefixLength;
    public int timeDelay;
    public String blackListSet;
    public String whiteListSet;
    @Override
    public String toString() {
        return "PartnerPCRFConfig [partnerId=" + partnerId + ", partnerName=" + partnerName + ", partnerKey="
                + partnerKey + ", queueName=" + queueName + ", pcrfInfo=" + pcrfInfo + ", profile=" + profile
                + ", prefix=" + prefix + ", suffix=" + suffix + ", isEncrypt=" + isEncrypt + ", isPrefix=" + isPrefix
                + ", prefixLength=" + prefixLength + ", timeDelay=" + timeDelay
                + ", blackListSet=" + blackListSet + ", whiteListSet=" + whiteListSet
                + "]";
    }
}