package com.mobile_app_server.config;

import com.mobile_app_server.common.Global;
import com.mobile_app_server.dto.PCRFMessage;
import com.mobile_app_server.service.impl.KafkaProducerService;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;

import java.util.concurrent.RejectedExecutionException;

@Component
@Slf4j
public class PCRFProcess implements Runnable {

    private final TaskExecutor executor;
    private final KafkaProducerService kafkaProducerService;

    public PCRFProcess(@Qualifier("pcrfTaskExecutor") TaskExecutor executor,
                       KafkaProducerService kafkaProducerService) {
        this.executor = executor;
        this.kafkaProducerService = kafkaProducerService;
    }

    @PostConstruct
    public void start() {
        Thread t = new Thread(this, "pcrf-delay-message-process");
        t.setDaemon(true);
        t.start();
    }

    @Override
    public void run() {
        log.info("Delay PCRF message consumer started");
        while (!Thread.currentThread().isInterrupted()) {
            PCRFMessage delayMsg = null;
            try {

//                take = get + remove :v
//                take: block tại đây cho đến khi gặp massage, tránh while loop liên tục
                delayMsg = Global.listPCRFDelayMessages.take();
                final PCRFMessage msgToProcess = delayMsg;

                try {
                    executor.execute(() -> process(msgToProcess));
                } catch (RejectedExecutionException e) {
                    log.warn("Executor full, re-queuing message: {}", msgToProcess);

//                    trong TH vượt quá max pool size, thì phải put lại vào queue để retry
                    msgToProcess.setSendTime(System.currentTimeMillis() + msgToProcess.getTimeDelayConfig() * 1000L);
                    Global.listPCRFDelayMessages.offer(msgToProcess);
                }

            } catch (InterruptedException e) {
                log.info("Delay PCRF message process interrupted, shutting down...");
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                log.error("Unexpected error in Delay PCRF loop", e);
            }
        }
    }

    private void process(PCRFMessage msg) {
        try {
            kafkaProducerService.sendPcrfMessage(msg);
        } catch (Exception e) {
            log.error("Error publishing message: {}", msg, e);
        }
    }
}