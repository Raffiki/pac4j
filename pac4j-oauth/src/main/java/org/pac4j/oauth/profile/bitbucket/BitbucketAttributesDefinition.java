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
package org.pac4j.oauth.profile.bitbucket;

import org.pac4j.core.profile.AttributesDefinition;
import org.pac4j.core.profile.converter.Converters;

import java.util.Arrays;

/**
 * This class defines the attributes of the Bitbucket profile.
 * 
 * @author Sebastian Sdorra
 * @since 1.5.1
 */
public class BitbucketAttributesDefinition extends AttributesDefinition {
  
    public static final String USERNAME = "username";
    public static final String FIRST_NAME = "first_name";
    public static final String LAST_NAME = "last_name";
    public static final String DISPLAY_NAME = "display_name";
    public static final String IS_TEAM = "is_team";
    public static final String AVATAR = "avatar";
    public static final String RESOURCE_URI = "resource_uri";
    public static final String EMAIL = "email";

    public BitbucketAttributesDefinition() {
        Arrays.stream(new String[] {USERNAME, FIRST_NAME, LAST_NAME, DISPLAY_NAME, IS_TEAM, AVATAR, RESOURCE_URI, EMAIL})
                .forEach(a -> primary(a, Converters.STRING));
        primary(IS_TEAM, Converters.BOOLEAN);
    }
}
