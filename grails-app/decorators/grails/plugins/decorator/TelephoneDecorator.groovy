/*
 * Copyright (c) 2012 Goran Ehrsson.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package grails.plugins.decorator

import java.util.regex.Pattern

class TelephoneDecorator {

    private static final Pattern PATTERN = ~/([:\s])(0\d{1,4}[\s\-]+[\d\s]{5,16}\d)/

    String decorate(String markup, Map params) {
        markup.replaceAll(PATTERN) {s, prefix, nbr ->
            prefix + '<a href="tel:' + nbr.replaceAll(/\D/, '') + '">' + nbr + '</a>'
        }
    }
}
