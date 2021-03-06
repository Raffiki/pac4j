/*
  Copyright 2012 - 2015 pac4j organization

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package org.pac4j.oidc.profile;

import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;
import com.nimbusds.oauth2.sdk.Scope;
import com.nimbusds.oauth2.sdk.token.BearerAccessToken;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.oidc.client.OidcClient;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.text.ParseException;
import java.util.List;

/**
 * <p>This class is the user profile for sites using OpenID Connect protocol.</p>
 * <p>It is returned by the {@link OidcClient}.</p>
 *
 * @author Michael Remond
 * @version 1.7.0
 */
public class OidcProfile extends CommonProfile implements Externalizable {

    private static final long serialVersionUID = -52855988661742374L;

    private transient static final String ACCESS_TOKEN = "access_token";
    private transient static final String ID_TOKEN = "id_token";

    public OidcProfile() {
    }

    public void setAccessToken(BearerAccessToken accessToken) {
        addAttribute(ACCESS_TOKEN, accessToken);
    }

    public BearerAccessToken getAccessToken() {
        return (BearerAccessToken) getAttribute(ACCESS_TOKEN);
    }

    public String getIdTokenString() {
        return (String) getAttribute(ID_TOKEN);
    }

    public void setIdTokenString(String idTokenString) {
        addAttribute(ID_TOKEN, idTokenString);
    }

    public JWT getIdToken() throws ParseException {
        if (getIdTokenString() != null) {
            return JWTParser.parse(getIdTokenString());
        }
        return null;
    }

    @Override
    public void writeExternal(final ObjectOutput out) throws IOException {
        super.writeExternal(out);
        BearerAccessTokenBean bean = null; 
        if (getAccessToken() != null) {
            bean = BearerAccessTokenBean.toBean(getAccessToken());
        }
        out.writeObject(bean);
        out.writeObject(getIdTokenString());
    }

    @Override
    public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
        final BearerAccessTokenBean bean = (BearerAccessTokenBean) in.readObject();
        if (bean != null) {
            setAccessToken(BearerAccessTokenBean.fromBean(bean));
        }
        setIdTokenString((String) in.readObject());
    }

    @Override
    public void clearSensitiveData() {
        removeAttribute(ACCESS_TOKEN);
        removeAttribute(ID_TOKEN);
    }

    private static class BearerAccessTokenBean implements Serializable {
        private static final long serialVersionUID = 7726472295622796149L;
        private String value;
        private long lifetime;
        private List<String> scope;

        public BearerAccessTokenBean(final String value, final long lifetime,
                                     final List<String> scope) {
            this.value = value;
            this.lifetime = lifetime;
            this.scope = scope;
        }

        public static BearerAccessTokenBean toBean(final BearerAccessToken token) {
            final Scope scope = token.getScope();
            if (scope != null) {
                return new BearerAccessTokenBean(token.getValue(),
                        token.getLifetime(), scope.toStringList());
            } else {
                return new BearerAccessTokenBean(token.getValue(),
                        token.getLifetime(), null);
            }
        }

        public static BearerAccessToken fromBean(final BearerAccessTokenBean token) {
            return new BearerAccessToken(token.value, token.lifetime,
                    Scope.parse(token.scope));
        }
    }
}
