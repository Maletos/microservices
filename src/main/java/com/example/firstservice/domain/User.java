package com.example.firstservice.domain;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.annotations.*;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;
import java.util.Calendar;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE users SET active = false WHERE id=?")
@FilterDef(name = "deletedUserFilter", parameters = @ParamDef(name = "isActive", type = Boolean.class))
@Filter(name = "deletedUserFilter", condition = "active = :isActive")
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_generator")
    @SequenceGenerator(name="user_generator", sequenceName = "users_seq", allocationSize=1)
    private Long id;
    @NotBlank(message = "User name cannot be empty")
    @Length(max = 64, message = "User name cannot be longer than 64 characters")
    private String userName;
    @NotBlank(message = "First name cannot be empty")
    private String firstName;
    @NotBlank(message = "Second name cannot be empty")
    private String secondName;
    @NotBlank(message = "Last name cannot be empty")
    private String lastName;
    @Email(message = "Email is not correct")
    @Length(max = 128, message = "Email address cannot be longer than 128 characters")
    @NotBlank(message = "Email cannot be empty")
    private String emailAddress;
    @NotBlank(message = "Password cannot be empty")
    private String password;
    private String gender;
    private String city;
    private String imageRef;
    @NotBlank(message = "Phone number cannot be blank")
    @Digits(message="Number should contain 10 digits.", fraction = 0, integer = 10)
    private String phoneNumber;
    @Column(name = "birth_date", columnDefinition = "DATE")
    private LocalDate birthDate;
    private String about;
    private boolean active = Boolean.TRUE;
}
