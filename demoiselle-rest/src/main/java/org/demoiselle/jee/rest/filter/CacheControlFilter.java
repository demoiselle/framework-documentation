/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.demoiselle.jee.rest.filter;

import java.io.IOException;
import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import org.demoiselle.jee.rest.DemoiselleRestConfig;
import org.demoiselle.jee.rest.annotation.CacheControl;
import org.demoiselle.jee.rest.annotation.Cors;

/**
 *
 * @author 70744416353
 */
@Provider
public class CacheControlFilter implements ContainerResponseFilter {

    @Context
    private ResourceInfo info;

    public void filter(ContainerRequestContext req, ContainerResponseContext res) throws IOException {

        if (req.getMethod().equals("GET")) {
            if (info.getResourceMethod() != null) {
                CacheControl max = info.getResourceMethod().getAnnotation(CacheControl.class);
                if (max != null) {
                    res.getHeaders().putSingle("Cache-Control", max.value());
                }
            }
        }
    }
}