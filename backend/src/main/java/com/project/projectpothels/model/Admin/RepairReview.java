package com.project.projectpothels.model.Admin;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "repair_review")
public class RepairReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "assignment_id", unique = true)
    private Assignment  assignment;

    private Integer rating;

    @Column(length = 1000)
    private String review;

    private LocalDateTime reviewAt;

    @PrePersist
    public void beforeSave() {

        if (reviewAt == null) {

            reviewAt = LocalDateTime.now();
        }
    }

    public RepairReview() {
    }

    public RepairReview(Long id, Assignment assignment, Integer rating, String review, LocalDateTime reviewAt) {
        this.id = id;
        this.assignment = assignment;
        this.rating = rating;
        this.review = review;
        this.reviewAt = reviewAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Assignment getAssignment() {
        return assignment;
    }

    public void setAssignment(Assignment assignment) {
        this.assignment = assignment;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public LocalDateTime getReviewAt() {
        return reviewAt;
    }

    public void setReviewAt(LocalDateTime reviewAt) {
        this.reviewAt = reviewAt;
    }

    
}
