import org.apache.commons.lang.StringUtils

class AbbreviateDecorator {
    String decorate(String markup, Map params) {
        params.max ? StringUtils.abbreviate(markup, params.offset ? Integer.valueOf(params.offset) : 0, Integer.valueOf(params.max)) : markup
    }
}