import java.util.regex.Pattern
import org.apache.commons.lang.StringUtils
import java.util.regex.Matcher

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

class UrlDecorator {

    String decorate(String markup, Map params) {
        hyperlinkText1(markup, params.target)
    }

    private static final Pattern regex = Pattern.compile("\\(?\\b(http[s]?://|www[.])[-A-Za-z0-9+&@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&@#/%=~_()|]")

    private String hyperlinkText1(String text, String target) {
        def targetText = (StringUtils.isBlank(target)) ? "" : " target=\"" + target.trim() + "\""

        text.replaceAll(regex) {s, scheme->
            def url = scheme.startsWith('http') ? s : 'http://' + s
            '<a href="' + url + '"' + targetText + '>' + s + '</a>'
        }
    }

    // NOTES:   1) \w includes 0-9, a-z, A-Z, _
    //          2) The leading '-' is the '-' character. It must go first in character class expression
    private static final String VALID_CHARS = "-\\w+&@#/%=~()|";
    private static final String VALID_NON_TERMINAL = "?!:,.;";

    // Notes on the expression:
    //  1) Any number of leading '(' (left parenthesis) accepted.  Will be dealt with.
    //  2) s? ==> the s is optional so either [http, https] accepted as scheme
    //  3) All valid chars accepted and then one or more
    //  4) Case insensitive so that the scheme can be hTtPs (for example) if desired
    private static final Pattern URI_FINDER_PATTERN = Pattern.compile("\\(*https?://[" + VALID_CHARS + VALID_NON_TERMINAL + "]*[" + VALID_CHARS + "]", Pattern.CASE_INSENSITIVE);

    /**
     * <p>
     * Finds all "URL"s in the given _rawText, wraps them in
     * HTML link tags and returns the result (with the rest of the text
     * html encoded).
     * </p>
     * <p>
     * We employ the procedure described at:
     * http://www.codinghorror.com/blog/2008/10/the-problem-with-urls.html
     * which is a <b>must-read</b>.
     * </p>
     * Basically, we allow any number of left parenthesis (which will get stripped away)
     * followed by http:// or https://.  Then any number of permitted URL characters
     * (based on http://www.ietf.org/rfc/rfc1738.txt) followed by a single character
     * of that set (basically, those minus typical punctuation).  We remove all sets of
     * matching left & right parentheses which surround the URL.
     * </p>
     * <p>
     * This method *must* be called from a tag/component which will NOT
     * end up escaping the output.  For example:
     * <PRE>
     * <h:outputText ... escape="false" value="#{core:hyperlinkText(textThatMayHaveURLs, '_blank')}"/>
     * </pre>
     * </p>
     * <p>
     * Reason: we are adding <code>&lt;a href="..."&gt;</code> tags to the output *and*
     * encoding the rest of the string.  So, encoding the output will result in
     * double-encoding data which was already encoded - and encoding the <code>a href</code>
     * (which will render it useless).
     * </p>
     * <p>
     *
     * @param _rawText - if <code>null</code>, returns <code>""</code> (empty string).
     * @param _target - if not <code>null</code> or <code>""</code>, adds a target attributed to the generated link, using _target as the attribute value.
     */
    private String hyperlinkText2(final String _rawText, final String _target) {

        String returnValue

        if (!StringUtils.isBlank(_rawText)) {

            final Matcher matcher = URI_FINDER_PATTERN.matcher(_rawText)

            if (matcher.find()) {

                final int originalLength = _rawText.length()

                final String targetText = (StringUtils.isBlank(_target)) ? "" : " target=\"" + _target.trim() + "\""
                final int targetLength = targetText.length()

                // Counted 15 characters aside from the target + 2 of the URL (max if the whole string is URL)
                // Rough guess, but should keep us from expanding the Builder too many times.
                final StringBuilder returnBuffer = new StringBuilder(originalLength * 2 + targetLength + 15)

                int currentStart
                int currentEnd
                int lastEnd = 0

                String currentURL

                while(true) {
                    currentStart = matcher.start()
                    currentEnd = matcher.end()
                    currentURL = matcher.group()

                    // Adjust for URLs wrapped in ()'s ... move start/end markers
                    //      and substring the _rawText for new URL value.
                    while (currentURL.startsWith("(") && currentURL.endsWith(")")) {
                        currentStart = currentStart + 1
                        currentEnd = currentEnd - 1

                        currentURL = _rawText.substring(currentStart, currentEnd)
                    }

                    while (currentURL.startsWith("(")) {
                        currentStart = currentStart + 1

                        currentURL = _rawText.substring(currentStart, currentEnd)
                    }

                    // Text since last match
                    returnBuffer.append(_rawText.substring(lastEnd, currentStart))

                    // Wrap matched URL
                    returnBuffer.append("<a href=\"" + currentURL + "\"" + targetText + ">" + currentURL + "</a>")

                    lastEnd = currentEnd
                    if(!matcher.find()) {
                        break
                    }
                }

                if (lastEnd < originalLength) {
                    returnBuffer.append(_rawText.substring(lastEnd))
                }

                returnValue = returnBuffer.toString()
            }
        }

        if (returnValue == null) {
            returnValue = _rawText
        }

        return returnValue

    }
}