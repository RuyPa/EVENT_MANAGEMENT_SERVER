package com.mobile_app_server.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.time.LocalDate;

@Getter
@Setter
@CrossOrigin
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tblUser")
public class UserDto {
    @Id
    private String id;
    private String username;
    private String password;
    private String name;
    private LocalDate dob;
    private String phonenumber;

    public UserDto(ResultSetQuery resultSetQuery) {
        this.name = resultSetQuery.getHostname();
    }
}
