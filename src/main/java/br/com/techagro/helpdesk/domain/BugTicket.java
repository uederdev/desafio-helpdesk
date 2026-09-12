package br.com.techagro.helpdesk.domain;

import br.com.techagro.helpdesk.domain.enums.SeverityTicket;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BugTicket extends Ticket {

    private SeverityTicket severity;
    private String stepsToReproduce;
    private String affectedVersion;
}
