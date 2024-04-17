package com.mobile_app_server.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.web.bind.annotation.CrossOrigin;

@Getter
@Setter
@CrossOrigin
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tblCategory")
@ToString
public class CategoryDto {
    @Id
    private Integer id;
    private String name;
    private String des;

    public CategoryDto(ResultSetQuery resultSetQuery) {
        this.name = resultSetQuery.getCatename();
    }

    public CategoryDto(Integer cateId){
        this.id = cateId;
    }

    public CategoryDto(Integer id, String name){
        this.id = id;
        this.name = name;
    }
}
