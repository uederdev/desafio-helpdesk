package br.com.techagro.helpdesk.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SupportTicket extends Ticket{

    private String customerImpact;
    private String resolutionNotes;
    private CategoryTicket category;

}
