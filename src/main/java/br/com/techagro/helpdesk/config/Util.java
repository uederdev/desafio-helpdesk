package br.com.techagro.helpdesk.config;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

public class Util {

    public static URI getUri(String path, Object... id) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(path)
                .buildAndExpand(id)
                .toUri();
    }
}
