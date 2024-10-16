package com.example.easybank.repository;

import com.example.easybank.model.Notices;
import com.mysql.cj.protocol.x.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeRepository extends JpaRepository<Notices,Long> {
    @Query("from Notices n where CURRENT_DATE BETWEEN n.noticBegDt AND n.noticEndDt")
    List<Notices> findActiveNotices();

}
