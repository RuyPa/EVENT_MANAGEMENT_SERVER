package com.mobile_app_server.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;


@Getter
@Setter
@ToString
@CrossOrigin
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tblEvent")
@Builder(builderMethodName = "hiddenBuilder")
public class EventDto {
    @Id
    private Integer id;
    private String name;
    private Date startDate;
    private Date endDate;
    private String startTime;
    private String endTime;
    private String location;
    private String address;
    private String city;
    private String des;
    private String eventVideo;
    private String registrationType;
    private String websiteLink;
    private String imgUrl;
    @Transient
    private List<EventCategoryDto> categories;
    @Transient
    private UserDto userDto;

    public EventDto(ResultSetQuery resultSetQuery) {
        this.id = resultSetQuery.getId();
        this.startDate = resultSetQuery.getStartdate();
        this.name = resultSetQuery.getName();
        this.endDate = resultSetQuery.getEnddate();
        this.startTime = resultSetQuery.getStarttime();
        this.endTime = resultSetQuery.getEndtime();
        this.location = resultSetQuery.getLocation();
        this.address = resultSetQuery.getAddress();
        this.city = resultSetQuery.getCity();
        this.des = resultSetQuery.getDes();
        this.eventVideo = resultSetQuery.getEventvideo();
        this.registrationType = resultSetQuery.getRegistrationtype();
        this.websiteLink = resultSetQuery.getWebsitelink();
        this.imgUrl = resultSetQuery.getImgurl();
        this.userDto = new UserDto(resultSetQuery);
    }

    public static EventDtoBuilder builder() {
        return hiddenBuilder();
    }
}
