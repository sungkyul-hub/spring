package org.skuhub.skuhub.external.sku.crawler;


import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.skuhub.skuhub.api.notice.service.NoticeServiceImpl;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class SkuNoticeCrawler {
    private  final NoticeServiceImpl noticeService;

    public void crawlAndSaveNotice(String url) {
        try {
            Document doc = Jsoup.connect(url).get();

            String category = doc.title();
            String title = doc.selectFirst("h2.view-title").text();

            // <dd> 데이터들
            Element dd1 = doc.select("dd").get(1); // 수정일
            Element dd2 = doc.select("dd").get(2); // 작성자
            Element dd4 = doc.select("dd").get(4); // 작성일

            String modifyDateStr = dd1.text();
            String writer = dd2.text();
            String noticeDateStr = dd4.text();

            String content = doc.selectFirst("div.view-con").text();
            String originalContent = doc.selectFirst("div.view-con").html();

            // 날짜 포맷 변환
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
            LocalDate noticeDate = LocalDate.parse(noticeDateStr, formatter);
            LocalDate noticeModifyDate = LocalDate.parse(modifyDateStr, formatter);

            noticeService.saveNotice(category, title, noticeDate, noticeModifyDate, writer, originalContent, content, url);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
