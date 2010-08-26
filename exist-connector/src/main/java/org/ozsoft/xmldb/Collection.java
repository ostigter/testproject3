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

import java.util.ArrayList;
import java.util.List;

/**
 * Collection stored in an XML database.
 * 
 * @author Oscar Stigter
 */
public class Collection extends Resource {

	/** Resources. */
	private final List<Resource> resources;

	/**
	 * Constructor.
	 * 
	 * @param name
	 *            The name.
	 */
	public Collection(String name) {
		super(name);
		resources = new ArrayList<Resource>();
	}

	/**
	 * Returns the resources.
	 * 
	 * @return The resources.
	 */
	public List<Resource> getResources() {
		return resources;
	}

	/**
	 * Adds a resource.
	 * 
	 * @param resource
	 *            The resource.
	 */
	public void addResource(Resource resource) {
		if (!resources.contains(resource)) {
			resources.add(resource);
		}
	}

}
