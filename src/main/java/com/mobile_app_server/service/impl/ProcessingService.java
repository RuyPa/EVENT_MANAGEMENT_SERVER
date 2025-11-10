package com.mobile_app_server.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
public class ProcessingService {

    private final TaskExecutor taskExecutor;

    public ProcessingService(@Qualifier("recordTaskExecutor") TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    public static AtomicInteger count = new AtomicInteger(0);

    public void processRecords(List<String> records) {
        log.info("Starting processRecords at {} totalRecords={}", Instant.now(), records.size());

        int idx = 0;
        for (String r : records) {
            final int id = ++idx;
            taskExecutor.execute(() -> {
                handleInThread(r, id);
            });
        }
    }

    private static void handleInThread(String r, int id) {
        long start = System.currentTimeMillis();
        String threadName = Thread.currentThread().getName();
        log.info("[Task] start id={} thread={}", id, threadName);
        try {
            log.warn("count {}", count.incrementAndGet());
            // mô phỏng xử lý nặng (ví dụ đọc file, gọi API, v.v.)
            Thread.sleep(100 + (long) (Math.random() * 400));
            log.info("[Task] processing id={} payloadLen={}", id, r.length());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("[Task] interrupted id={}", id);
        } finally {
            long duration = System.currentTimeMillis() - start;
            log.info("[Task] done id={} duration-ms={} thread={}", id, duration, threadName);
        }
    }
}