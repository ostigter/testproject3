// This file is part of the exist-connector project.
//
// Copyright 2010 Oscar Stigter
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.ozsoft.xmldb;

/**
 * Generic XML database exception.
 * 
 * @author Oscar Stigter
 */
public class XmldbException extends Exception {

    /** Serial version UID. */
    private static final long serialVersionUID = 8972251689403547077L;

    /**
     * Constructor with a message only.
     * 
     * @param message
     *            The message.
     */
    public XmldbException(String message) {
        super(message);
    }

    /**
     * Constructor with a message and a nested exception as cause.
     * 
     * @param message
     *            The message.
     * @param t
     *            The nested exception as cause.
     */
    public XmldbException(String message, Throwable t) {
        super(message, t);
    }

}
