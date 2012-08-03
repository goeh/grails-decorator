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

import grails.plugins.decorator.GrailsDecoratorClass
import grails.plugins.decorator.DecoratorArtefactHandler
import grails.spring.BeanBuilder

class DecoratorGrailsPlugin {
    // the plugin version
    def version = "0.2"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "1.3 > *"
    // the other plugins this plugin depends on
    def dependsOn = [:]
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
        "grails-app/views/error.gsp"
    ]
    def watchedResources = [
            "file:./grails-app/decorators/**/*Decorator.groovy",
            "file:./plugins/*/grails-app/decorators/**/*Decorator.groovy"
    ]
    def artefacts = [new DecoratorArtefactHandler()]

    def title = "Markup Decorator"
    def author = "Goran Ehrsson"
    def authorEmail = "goran@technipelago.se"
    def description = '''\
<p>Use pluggable decorators to modify markup in GSP pages.</p>

<p>Examples</p>

<p><strong>Abbreviation decorator</strong></p>
<pre><g:decorate max="20">The quick brown fox jumps over the lazy dog</g:decorate></pre>
<p>The quick brown f...</p>

<p><strong>URL decorator</strong></p>
<pre><g:decorate>www.grails.org</g:decorate></pre>
<p>&lt;a href="http://www.grails.org">www.grails.org&lt;/a></p>

<p>Custom decorator artefacts can be placed in grails-app/decorators</p>
'''

    // URL to the plugin's documentation
    def documentation = "http://grails.org/plugin/decorator"

    // License: one of 'APACHE', 'GPL2', 'GPL3'
    def license = "APACHE"

    // Details of company behind the plugin (if there is one)
    def organization = [ name: "Technipelago AB", url: "http://www.technipelago.se/" ]

    // Any additional developers beyond the author specified above.
//    def developers = [ [ name: "Joe Bloggs", email: "joe@bloggs.net" ]]

    // Location of the plugin's issue tracker.
    def issueManagement = [ system: "github", url: "https://github.com/goeh/grails-decorate/issues" ]

    // Online location of the plugin's browseable source code.
    def scm = [ url: "https://github.com/goeh/grails-decorate" ]

    def doWithSpring = {
        // Configure decorators
        def decoratorClasses = application.decoratorClasses
        decoratorClasses.each { decoratorClass ->
            "${decoratorClass.propertyName}"(decoratorClass.clazz) { bean ->
                bean.autowire = "byName"
            }
        }
    }

    def doWithDynamicMethods = { ctx ->
        // TODO Implement registering dynamic methods to classes (optional)
    }

    def doWithApplicationContext = { applicationContext ->
        println "Installed markup decorators ${application.decoratorClasses*.propertyName}"
    }

    def onChange = { event ->
        // watching is modified and reloaded. The event contains: event.source,
        // event.application, event.manager, event.ctx, and event.plugin.
        if (application.isDecoratorClass(event.source)) {
            log.debug "Decorator ${event.source} modified!"

            def context = event.ctx
            if (!context) {
                log.debug("Application context not found - can't reload.")
                return
            }

            // Make sure the new decorator class is registered.
            def decoratorClass = application.addArtefact(GrailsDecoratorClass.TYPE, event.source)

            // Create the decorator bean.
            def bb = new BeanBuilder()
            bb.beans {
                "${decoratorClass.propertyName}"(decoratorClass.clazz) { bean ->
                    bean.autowire = "byName"
                }
            }
            bb.registerBeans(context)
        }
    }
}
