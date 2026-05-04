package mara.spichiger.todoprojekt.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String email;
    private String department;
    private String assignedTo;

    // Das @Enumerated sorgt dafür, dass der Status als Text (STRING)
    // in der Datenbank landet, nicht als Zahl.
    @Enumerated(EnumType.STRING)
    private Status status;
}