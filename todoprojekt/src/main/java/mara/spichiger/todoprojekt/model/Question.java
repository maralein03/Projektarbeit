package mara.spichiger.todoprojekt.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String author;

    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "todo_id")
    private Todo todo;

    @PrePersist
    protected void onCreate() {
        timestamp = LocalDateTime.now();
    }
}