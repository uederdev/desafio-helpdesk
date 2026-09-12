package br.com.techagro.helpdesk.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "categories")
public class CategoryTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false, unique = true)
    private Long id;

    @Column(name = "description", length = 50, nullable = false)
    private String description;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof CategoryTicket that)) return false;
        return Objects.equals(id, that.id) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description);
    }

    @Override
    public String toString() {
        return "CategoryTicket{" +
                "id=" + id +
                ", description='" + description + '\'' +
                '}';
    }
}
