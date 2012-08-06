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

import org.codehaus.groovy.grails.commons.ArtefactHandlerAdapter;
import org.springframework.util.ReflectionUtils;
import java.util.Map;

/**
 *
 * @author Goran Ehrsson
 * @since 0.1
 */
class DecoratorArtefactHandler extends ArtefactHandlerAdapter {

    public DecoratorArtefactHandler() {
        super(GrailsDecoratorClass.TYPE, GrailsDecoratorClass.class, DefaultGrailsDecoratorClass.class, null);
    }

    @Override
    public boolean isArtefactClass(Class clazz) {
        // class shouldn't be null and should end with Decorator suffix
        if (clazz == null || !clazz.getName().endsWith(GrailsDecoratorClass.TYPE)) {
            return false;
        }

        return ReflectionUtils.findMethod(clazz, GrailsDecoratorClass.DECORATE, new Class[]{String.class, Map.class}) != null;
    }
}

