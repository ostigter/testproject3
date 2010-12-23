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
 * XMLDB exception indicating that a specific resource does not exist.
 * 
 * @author Oscar Stigter
 */
public class NotFoundException extends XmldbException {

    /** Serial version UID. */
    private static final long serialVersionUID = 9118174838396459623L;

    /**
     * Constructor.
     * 
     * @param uri
     *            The resource URI.
     */
    public NotFoundException(String uri) {
        super(String.format("Resource not found: '%s'", uri));
    }

}
