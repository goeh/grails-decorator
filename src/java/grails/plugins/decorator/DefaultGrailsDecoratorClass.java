/*
 *  Copyright 2012 Goran Ehrsson.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package grails.plugins.decorator;

import java.util.Map;

import groovy.lang.MetaClass;
import org.codehaus.groovy.grails.commons.AbstractInjectableGrailsClass;

/**
 *
 * @author Goran Ehrsson
 * @since 0.1
 */
public class DefaultGrailsDecoratorClass extends AbstractInjectableGrailsClass implements GrailsDecoratorClass {

    public DefaultGrailsDecoratorClass(Class clazz) {
        super(clazz, GrailsDecoratorClass.TYPE);
    }

    public String decorate(String markup, Map params) {
        return (String)getMetaClass().invokeMethod(getReferenceInstance(), DECORATE, new Object[]{markup, params});
    }

    public boolean getEnabled() {
        MetaClass mc = getMetaClass();
        Object ri = getReferenceInstance();
        if(mc.hasProperty(ri, "enabled") != null) {
            return ((Boolean)mc.invokeMethod(ri, "getEnabled", new Object[]{})).booleanValue();
        }
        return true;
    }
}
