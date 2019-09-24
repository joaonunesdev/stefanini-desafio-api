package com.stefanini.desafio.api.event;

import org.springframework.context.ApplicationEvent;

import javax.servlet.http.HttpServletResponse;

public class ResourceCreatedEvent extends ApplicationEvent {

    private HttpServletResponse response;
    private Long codigo;

    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public ResourceCreatedEvent(Object source, HttpServletResponse response, Long codigo) {
        super(source);
        this.response = response;
        this.codigo = codigo;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public Long getCodigo() {
        return codigo;
    }

}
