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

class JiraDecorator {

    def grailsApplication

    private static final Pattern PATTERN = ~/([A-Z0-9_]+)\-(\d{1,5})/

    Map projects = null

    String decorate(String markup, Map params) {
        if (projects == null) {
            synchronized (this) {
                if (projects == null) {
                    projects = grailsApplication.config.decorator.jira ?: [:]
                }
            }
        }
        markup.replaceAll(PATTERN) { s, project, id ->
            def url = projects[project]
            return url ? "<a href=\"$url/$s\">$s</a>" : s
        }
    }
}
