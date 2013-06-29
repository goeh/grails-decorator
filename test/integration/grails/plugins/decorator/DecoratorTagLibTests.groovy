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

import grails.test.GroovyPagesTestCase
import org.junit.Before

class DecoratorTagLibTests extends GroovyPagesTestCase {

    def grailsApplication

    @Before
    void setupConfiguration() {
        grailsApplication.config.decorator.jira.GRAILS = "http://jira.grails.org/browse"
        grailsApplication.config.decorator.jira.GROOVY = "http://jira.codehaus.org/browse"
    }

    void testInstalledClasses() {
        def list = grailsApplication.decoratorClasses*.propertyName
        assert list.contains('abbreviateDecorator')
        assert list.contains('jiraDecorator')
        assert list.contains('telephoneDecorator')
        assert list.contains('urlDecorator')
    }

    void testEnabledClasses() {
        def list = grailsApplication.decoratorClasses.findAll { it.enabled }*.propertyName
        assert list.contains('abbreviateDecorator')
        assert list.contains('telephoneDecorator')
        assert list.contains('urlDecorator')

        assert !list.contains('jiraDecorator')
    }

    void testEmptyBody() {
        assert applyTemplate('<g:decorate></g:decorate>') == ""
    }

    void testAbbreviate() {
        assert applyTemplate('<g:decorate max="20">The quick brown fox jumps over the lazy dog.</g:decorate>') == "The quick brown f..."
        assert applyTemplate('<g:decorate max="\${20}" include="abbreviate">The quick brown fox jumps over the lazy dog.</g:decorate>') == "The quick brown f..."
        assert applyTemplate('<g:decorate encode="HTML"><script>alert("buuuh!")</script></g:decorate>') == '&lt;script&gt;alert(&quot;buuuh!&quot;)&lt;/script&gt;'
    }

    void testJira() {
        assert applyTemplate('<g:decorate include="jira">I think GRAILS-1234 is related to GROOVY-666.</g:decorate>') == 'I think <a href="http://jira.grails.org/browse/GRAILS-1234">GRAILS-1234</a> is related to <a href="http://jira.codehaus.org/browse/GROOVY-666">GROOVY-666</a>.'
    }

    void testTelephone() {
        assert applyTemplate('<g:decorate>Tel: 08-123456</g:decorate>') == 'Tel: <a href="tel:08123456">08-123456</a>'
        assert applyTemplate('<g:decorate>H 055-1234567</g:decorate>') == 'H <a href="tel:0551234567">055-1234567</a>'
        assert applyTemplate('<g:decorate>CC 1055-1234567</g:decorate>') == 'CC 1055-1234567'
        assert applyTemplate('<g:decorate>W 31 55 66 77 88</g:decorate>') == 'W 31 55 66 77 88'
        assert applyTemplate('<g:decorate>Phone:031 55 66 77 88</g:decorate>') == 'Phone:<a href="tel:03155667788">031 55 66 77 88</a>'
    }

    void testUrl() {
        assert applyTemplate('<g:decorate>www.grails.org</g:decorate>') == '<a href="http://www.grails.org">www.grails.org</a>'
        assert applyTemplate('<g:decorate>http://www.grails.org</g:decorate>') == '<a href="http://www.grails.org">http://www.grails.org</a>'
        assert applyTemplate('<g:decorate>https://www.google.com</g:decorate>') == '<a href="https://www.google.com">https://www.google.com</a>'
    }

    void testNlBr() {
        assert applyTemplate('<g:decorate>Row 1\nRow 2\nRow 3</g:decorate>') == 'Row 1\nRow 2\nRow 3'
        assert applyTemplate('<g:decorate nlbr="true">Row 1\nRow 2\nRow 3</g:decorate>') == 'Row 1<br/>\nRow 2<br/>\nRow 3'
    }
}
