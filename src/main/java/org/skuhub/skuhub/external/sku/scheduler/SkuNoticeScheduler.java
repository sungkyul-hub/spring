package org.skuhub.skuhub.external.sku.scheduler;

import lombok.RequiredArgsConstructor;
import org.skuhub.skuhub.external.sku.crawler.SkuNoticeCrawler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SkuNoticeScheduler {
    private final SkuNoticeCrawler crawler;

    @Scheduled(cron = "0 0 * * * *")
    public void scheduleCrawling() {
        String url= null;
        crawler.crawlAndSaveNotice(url);
    }
}