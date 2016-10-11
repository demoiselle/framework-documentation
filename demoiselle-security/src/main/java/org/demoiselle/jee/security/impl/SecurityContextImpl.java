/*
 * Demoiselle Framework
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later.
 * See the lgpl.txt file in the root directory or <https://www.gnu.org/licenses/lgpl.html>.
 */
package org.demoiselle.jee.security.impl;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.demoiselle.jee.core.interfaces.security.DemoisellePrincipal;

import org.demoiselle.jee.core.interfaces.security.SecurityContext;
import org.demoiselle.jee.core.interfaces.security.TokensManager;

@ApplicationScoped
public class SecurityContextImpl implements SecurityContext {

    private static final long serialVersionUID = 1L;

    @Inject
    private TokensManager tm;

    @Override
    public boolean hasPermission(String resource, String operation) {
        if ((tm.getUser().getPermissions().entrySet()
                .stream()
                .filter(p -> p.getKey().equalsIgnoreCase(resource))
                .filter(p -> p.getValue().equalsIgnoreCase(operation))
                .count() <= 0)) {
            return false;
        }
        return true;
    }

    @Override
    public boolean hasRole(String role) {
        if (tm.getUser().getRoles().stream().filter(p -> p.equals(role)).count() <= 0) {
            return false;
        }
        return true;
    }

    @Override
    public boolean isLoggedIn() {
        return tm.validate();
    }

    @Override
    public DemoisellePrincipal getUser() {
        return tm.getUser();
    }

    @Override
    public void setUser(DemoisellePrincipal loggedUser) {
        tm.setUser(loggedUser);
    }

}
