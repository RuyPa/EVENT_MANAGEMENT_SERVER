package com.mobile_app_server.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Date;

@Getter
@Setter
@ToString
@CrossOrigin
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tblTask")
@Builder(builderMethodName = "hiddenBuilder")
public class TaskDto {

    @Id
    private Integer id;
    private String name;
    private String startTime;
    private Integer checked;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;
    private String des;
    @Transient
    private EventDto eventDto;

    public TaskDto(ResultSetQuery resultSetQuery) {
        this.id = resultSetQuery.getId();
        this.name = resultSetQuery.getName();
        this.startDate = resultSetQuery.getStartdate();
        this.startTime = resultSetQuery.getStarttime();
        this.des = resultSetQuery.getDes();
        this.checked = resultSetQuery.getChecked();
    }

    public static TaskDto.TaskDtoBuilder builder() {
        return hiddenBuilder();
    }
}
