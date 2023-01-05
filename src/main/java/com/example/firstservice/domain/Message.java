package com.example.firstservice.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "messages_generator")
    @SequenceGenerator(name="messages_generator", sequenceName = "messages_seq", allocationSize=1)
    private Long id;
    @NotBlank(message = "Please set up a field")
    @Length(max = 2048, message = "Cannot be longer than 2048")
    private String description;
    @Length(max = 255, message = "Cannot be longer than 255")
    private String title;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User author;
    private String fileName;
}
