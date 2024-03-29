package com.mobile_app_server.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.CrossOrigin;

@Getter
@Setter
@CrossOrigin
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tblEventCategory")
public class EventCategoryDto {
    @Id
    private Integer id;
    @Transient
    private CategoryDto categoryDto;

    public EventCategoryDto(ResultSetQuery resultSetQuery) {
        this.categoryDto = new CategoryDto(resultSetQuery);
    }

    public EventCategoryDto(Integer cateId){
        this.categoryDto = new CategoryDto(cateId);
    }
}
