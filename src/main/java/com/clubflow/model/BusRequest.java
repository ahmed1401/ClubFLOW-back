package com.clubflow.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "bus_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BusRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String destination;

    @Column(nullable = false)
    private String departureDate;

    private String returnDate;

    @Column(nullable = false)
    private Integer numberOfPassengers;

    @Column(nullable = false)
    private String purpose;

    @Column(columnDefinition = "TEXT")
    private String additionalNotes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus status = RequestStatus.PENDING;

    private String rejectionReason;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public enum RequestStatus {
        PENDING,
        APPROVED,
        REJECTED
    }
}