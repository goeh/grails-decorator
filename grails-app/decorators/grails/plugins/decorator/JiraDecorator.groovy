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

class JiraDecorator {

    def grailsApplication

    String decorate(String markup, Map params) {
        def config = grailsApplication.config.decorator.jira
        markup.replaceAll(/([A-Z0-9_]+)\-(\d{1,5})/){s, project, id ->
            def url = config[project]
            return url ? "<a href=\"$url/$s\">$s</a>" : s
        }
    }
}
