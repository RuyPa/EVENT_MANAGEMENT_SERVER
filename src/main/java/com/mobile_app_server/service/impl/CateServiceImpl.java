package com.mobile_app_server.service.impl;

import com.mobile_app_server.dto.CategoryDto;
import com.mobile_app_server.dto.ResultSetQuery;
import com.mobile_app_server.repo.CateRepo;
import com.mobile_app_server.service.CateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CateServiceImpl implements CateService {

    private final CateRepo cateRepo;

    @Override
    public List<CategoryDto> getAllCate() {

        List<ResultSetQuery> data = cateRepo.getAllCate();
        return data.stream().map(cateMap -> new CategoryDto(cateMap.getId(), cateMap.getName()))
                .collect(Collectors.toList());
    }
}
