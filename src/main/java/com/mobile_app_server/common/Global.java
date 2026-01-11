package com.mobile_app_server.common;

import com.mobile_app_server.dto.PCRFMessage;
import com.mobile_app_server.dto.PartnerPCRFConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.DelayQueue;

public class Global {

    public static List<PartnerPCRFConfig> listPartnerPCRFConfig= new ArrayList<PartnerPCRFConfig>();

    static {
        // Partner 1 - delay = 0
        PartnerPCRFConfig partner1 = new PartnerPCRFConfig();
        partner1.partnerId = 1;
        partner1.partnerName = "PARTNER_A";
        partner1.partnerKey = "PARTNER_A_KEY";
        partner1.queueName = "tp-cntt-pcrf-in";
        partner1.pcrfInfo = "PCRF_A";
        partner1.profile = new String[]{"BASIC", "DATA"};
        partner1.prefix = new String[]{"84", "-999"};
        partner1.suffix = new String[]{"01", "02", "-999"};
        partner1.isEncrypt = 0;
        partner1.isPrefix = 1;
        partner1.prefixLength = 2;
        partner1.timeDelay = 0; // ✅ không delay
        partner1.blackListSet = "BLACKLIST_A";
        partner1.whiteListSet = "WHITELIST_A";

        // Partner 2 - delay = 15s
        PartnerPCRFConfig partner2 = new PartnerPCRFConfig();
        partner2.partnerId = 2;
        partner2.partnerName = "PARTNER_B";
        partner2.partnerKey = "PARTNER_B_KEY";
        partner2.queueName = "tp-cntt-pcrf-in";
        partner2.pcrfInfo = "PCRF_B";
        partner2.profile = new String[]{"PREMIUM"};
        partner2.prefix = new String[]{"84", "-999"};
        partner2.suffix = new String[]{"03", "04", "-999"};
        partner2.isEncrypt = 0;
        partner2.isPrefix = 1;
        partner2.prefixLength = 2;
        partner2.timeDelay = 15; // ✅ delay 15 giây
        partner2.blackListSet = "BLACKLIST_B";
        partner2.whiteListSet = "WHITELIST_B";

        listPartnerPCRFConfig.add(partner1);
        listPartnerPCRFConfig.add(partner2);
    }


    public static DelayQueue<PCRFMessage> listPCRFDelayMessages = new DelayQueue<>();


}
