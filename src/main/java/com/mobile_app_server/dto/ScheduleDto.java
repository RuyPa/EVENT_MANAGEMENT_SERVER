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
@Table(name = "tblSchedule")
@Builder(builderMethodName = "hiddenBuilder")
public class ScheduleDto {

    @Id
    private Integer id;
    private String name;
    private String startTime;
    private String endTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    private String des;
    private String location;
    @Transient
    private EventDto eventDto;

    public ScheduleDto(ResultSetQuery resultSetQuery) {
        this.id = resultSetQuery.getId();
        this.name = resultSetQuery.getName();
        this.startDate = resultSetQuery.getStartdate();
        this.endDate = resultSetQuery.getEnddate();
        this.startTime = resultSetQuery.getStarttime();
        this.endTime = resultSetQuery.getEndtime();
        this.location = resultSetQuery.getLocation();
        this.des = resultSetQuery.getDes();
    }

    public static ScheduleDto.ScheduleDtoBuilder builder() {
        return hiddenBuilder();
    }
}
