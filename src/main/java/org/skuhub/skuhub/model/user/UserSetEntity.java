package org.skuhub.skuhub.model.user;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "USER_INFO_TB")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(toBuilder = true)  // toBuilder 메소드 추가
public class UserSetEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_key")
    private Long userKey;  // user_key로 매핑

    @Column(name = "user_id", nullable = false, unique = true, length = 20)
    private String userId;   // 유니크한 값

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "year", nullable = false, length = 4)
    private int year;

    @Column(name = "department", nullable = false, length = 20)
    private String department;

    @Column(name = "name", nullable = false, length = 20)
    private String name;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "withdrawal_date")
    private LocalDateTime withdrawalDate;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

