package io.github.xcvqqz.cloud_file_storage.entity;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true,  updatable = false)
    private Long id;

    @Column(length = 30, nullable = false, unique = true, updatable = false)
    private String name;

    @Column(length = 30, nullable = false)
    private String password;


}
