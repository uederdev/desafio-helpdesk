package br.com.techagro.helpdesk.domain;

import java.time.LocalDateTime;

public class Comment {

    private Long id;
    private String content;
    private LocalDateTime createdAt ;
    private Ticket ticket;
    private User author;

}
