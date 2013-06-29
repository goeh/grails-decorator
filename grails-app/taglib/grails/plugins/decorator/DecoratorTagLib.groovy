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

class DecoratorTagLib {

    def grailsApplication

    def decorate = {attrs, body->

        // Get the markup to decorate.
        def markup = body().toString().trim()

        // Encode output?
        if(attrs.encode) {
            markup = markup."encodeAs${attrs.encode}"()
        }

        // Exclude any decorators?
        def exclude = (attrs.exclude ?: grailsApplication.config.decorator.exclude) ?: []
        if(exclude && !(exclude instanceof Collection)) {
            exclude = [exclude]
        }

        def disabled = grailsApplication.decoratorClasses.findAll{!it.enabled}*.propertyName.collect{it - 'Decorator'}
        if(disabled) {
            exclude.addAll(disabled)
        }

        // Build collection of decorators to include.
        def include = attrs.include ?: grailsApplication.config.decorator.include
        if(! include) {
            include = grailsApplication.decoratorClasses*.propertyName.collect{it - 'Decorator'}.findAll{!exclude.contains(it)}
        }
        if(!(include instanceof Collection)) {
            include = [include]
        }
        // Apply decorators.
        for(dc in include) {
            def decorator = grailsApplication.mainContext.getBean(dc + 'Decorator')
            markup = decorator.decorate(markup, attrs)
        }

        if(attrs.nlbr) {
            markup = markup.replaceAll('\n', '<br/>\n')
        }

        // Render final markup.
        out << markup
    }
}
