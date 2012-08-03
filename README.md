#Grails Markup Decorator Plugin
This plugin uses pluggable decorators to modify markup in GSP pages.

The plugin provides standard decorators for:

* Abbreviation - make sure content is shorter than a limit
* URL - convert www.company.com to hyperlink
* Markdown - markdown parser
* JIRA - convert uppercase characters followed by dash followed by numbers to a JIRA browse url (configurable)
* Telephone - convert telephone numbers to tel: hyperlinks

-----

## Examples

###Abbreviation decorator
    <g:decorate max="20">The quick brown fox jumps over the lazy dog</g:decorate>

The quick brown f...

###URL decorator
    <g:decorate>www.grails.org</g:decorate>

\<a href="http://www.grails.org">www.grails.org\</a>

###Markdown decorator
    This plugin is **awesome!**

\<p>This plugin is \<strong>awesome!\</strong>\</p>

###JIRA decorator
    <g:decorate>Please see GRAILS-1234 for more information.</g:decorate>

Please see \<a href="http://jira.grails.org/browse/GRAILS-1234">GRAILS-1234\</a> for more information.

###Phone decorator
    <g:decorate>My telephone number is 555-123456</g:decorate>

My telephone number is \<a href="tel:555123456">555-123456\</a>

## Configuration
All installed decorators are applied by default when you use the '<g:decorate>' tag.

You can limit the number of decorators applied by default with the 'decorator.include' parameter.

    decorator.include = ['abbreviate']

You can exclude decorators applied by default with the 'decorator.exclude' parameter.

    decorator.exclude = ['markdown']

## Custom decorators
You can provide your own decorators. Just put a Groovy class in grails-app/decorators with a class name that ends with **Decorator**.

    class MyDecorator {

        /**
         * Make words/sentences surrounded with asterisk bold.
         */
        String decorate(String markup, Map params) {
            markup.replaceAll(/\*([\w\s]+)\*/){s, sentence -> '<strong>' + sentence + '</strong>' }
        }

    }