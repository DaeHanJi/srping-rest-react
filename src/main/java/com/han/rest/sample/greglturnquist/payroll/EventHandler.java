package com.han.rest.sample.greglturnquist.payroll;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import static com.han.rest.sample.greglturnquist.payroll.WebSocketConfiguration.MESSAGE_PREFIX;

@Log4j2
@Component
@RepositoryEventHandler(Employee.class)
public class EventHandler {

    private final SimpMessagingTemplate websocket;
    private final EntityLinks entityLinks;

    @Autowired
    public EventHandler(SimpMessagingTemplate websocket, EntityLinks entityLinks) {
        this.websocket = websocket;
        this.entityLinks = entityLinks;
    }

    @HandleAfterDelete
    public void newEmployee(Employee employee) {
        log.debug("TEST : newEmployee");
        this.websocket.convertAndSend(MESSAGE_PREFIX + "/newEmployee", getPath(employee));
    }

    @HandleAfterDelete
    public void deleteEmployee(Employee employee) {
        log.debug("TEST : deleteEmployee");
        this.websocket.convertAndSend(MESSAGE_PREFIX + "/deleteEmployee", getPath(employee));
    }

    @HandleAfterSave
    public void updateEmployee(Employee employee) {
        log.debug("TEST : updateEmployee {}::{}",MESSAGE_PREFIX + "/updateEmployee", getPath(employee));
        this.websocket.convertAndSend(MESSAGE_PREFIX + "/updateEmployee", getPath(employee));
    }

    private String getPath(Employee employee) {
        log.debug("TEST : getPath");
        return this.entityLinks.linkForItemResource(employee.getClass(), employee.getId()).toUri().getPath();
    }
}
