package com.mobile_app_server.repo;

import com.mobile_app_server.dto.CategoryDto;
import com.mobile_app_server.dto.ResultSetQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CateRepo extends JpaRepository<CategoryDto, Integer> {

    @Query(value = "select cate.id, cate.name  from tblCategory cate", nativeQuery = true)
    List<ResultSetQuery> getAllCate();
}
