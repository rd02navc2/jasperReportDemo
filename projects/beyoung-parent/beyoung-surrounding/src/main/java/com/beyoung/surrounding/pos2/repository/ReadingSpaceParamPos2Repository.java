package com.beyoung.surrounding.pos2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.beyoung.surrounding.pos2.entity.READING_SPACE_PARAM;
import java.util.List;

@Repository
public interface ReadingSpaceParamPos2Repository extends JpaRepository<READING_SPACE_PARAM, String> {

    /**
     * 透過參數名稱清單，批次查詢參數設定
     * Spring Data JPA 會根據方法名自動解析為：
     * SELECT * FROM READING_SPACE_PARAM WHERE param_name IN (...)
     */
    List<READING_SPACE_PARAM> findByParamNameIn(List<String> paramNames);
}