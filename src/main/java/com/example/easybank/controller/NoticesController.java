package com.example.easybank.controller;

import com.example.easybank.model.Notices;
import com.example.easybank.repository.NoticeRepository;
import com.mysql.cj.protocol.x.Notice;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequiredArgsConstructor
public class NoticesController {

    private final NoticeRepository noticeRepository;
    @GetMapping("/notices")
    public ResponseEntity<List<Notices>> getNotices() {

        List<Notices> notices = noticeRepository.findActiveNotices();
        if(notices != null){
            return ResponseEntity.ok()
                    .cacheControl(CacheControl.maxAge(60, TimeUnit.SECONDS))
                    .body(notices);
        }else{
            return null;
        }
    }
}
