#Grails Markup Decorator Plugin
This plugin uses pluggable decorators to modify markup in GSP pages.

The plugin provides standard decorators for:

* Abbreviation - make sure content is shorter than a limit
* URL - convert www.company.com to hyperlink
* JIRA - convert uppercase characters followed by dash followed by numbers to a JIRA browse url (configurable)
* Telephone - convert telephone numbers to tel: hyperlinks

-----

# Usage Examples

In the following examples the tag body is hard coded to make it easier to see the result.
Normally you will have a variable with dynamic information from a database, etc.
like this `<g:decorate>${user.profile}</g:decorate>`.

##Abbreviation decorator
    <g:decorate max="20">The quick brown fox jumps over the lazy dog</g:decorate>

The quick brown f...

##URL decorator
    <g:decorate>www.grails.org</g:decorate>

\<a href="http://www.grails.org"> www.grails.org \</a>

##JIRA decorator
    <g:decorate>Please see GRAILS-1234 for more information.</g:decorate>

Please see \<a href="http://jira.grails.org/browse/GRAILS-1234"> GRAILS-1234 \</a> for more information.

##Phone decorator
    <g:decorate>My telephone number is 555-123456</g:decorate>

My telephone number is \<a href="tel:555123456"> 555-123456 \</a>

# Configuration
All installed decorators except `jiraDecorator` are applied by default when you use the `<g:decorate/>` tag.

You can limit the number of decorators applied by default with the 'decorator.include' parameter.

    decorator.include = ['abbreviate']

You can exclude decorators applied by default with the 'decorator.exclude' parameter.

    decorator.exclude = ['telephone']

# Custom decorators
You can provide your own decorators. Just put a Groovy class in grails-app/decorators with a class name that ends with **Decorator** and implement the `String decorate(String, Map)` method.

    class MyDecorator {

        /**
         * Make words/sentences surrounded with asterisk bold.
         */
        String decorate(String markup, Map params) {
            markup.replaceAll(/\*([\w\s]+)\*/){s, sentence -> '<strong>' + sentence + '</strong>' }
        }
    }

# See also

## Markdown decorator
The [Markdown Decorator](https://github.com/goeh/grails-decorator-markdown) plugin is an example of an extension plugin that adds Markdown syntax support.

