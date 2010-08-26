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
 * XMLDB exception indicating that the user is not authorized to perform a
 * specific operation on the database.
 * 
 * @author Oscar Stigter
 */
public class NotAuthorizedException extends XmldbException {
    
    /** Serial version UID. */
    private static final long serialVersionUID = -4506483007291569676L;

    /**
     * Constructor with a message only.
     * 
     * @param message
     *            The message.
     */
    public NotAuthorizedException(String message) {
        super(message);
    }

}
