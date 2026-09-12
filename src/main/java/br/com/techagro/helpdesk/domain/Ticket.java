package br.com.techagro.helpdesk.domain;

import br.com.techagro.helpdesk.domain.enums.PriorityTicket;
import br.com.techagro.helpdesk.domain.enums.StatusTicket;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
public abstract class Ticket {

    private Long id;
    private String title;
    private String description;
    private StatusTicket status;
    private PriorityTicket priority;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private LocalDate dueAt;
    private User assignedTo;
    private User createdBy;
}
