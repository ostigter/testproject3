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

import java.io.File;
import java.io.InputStream;
import java.util.Map;

import org.dom4j.Document;

/**
 * Connector to an XML database. <br />
 * <br />
 * 
 * Can be used to retrieve, store and delete resources, execute ad-hoc queries
 * and stored XQuery modules, and call stored XQuery functions.
 * 
 * @author Oscar Stigter
 */
public interface XmldbConnector {

    /**
     * Retrieves a collection.
     * 
     * @param uri
     *            The collection URI.
     * 
     * @return The collection.
     * 
     * @throws XmldbException
     *             If the collection could not be found or retrieved.
     */
    Collection retrieveCollection(String uri) throws XmldbException;

    /**
     * Retrieves a resource.
     * 
     * Use this method for non-XML resources (e.g. plain text or binary). For
     * XML resources, use the retrieveXmlDocument method.
     * 
     * @param uri
     *            The resource URI.
     * 
     * @return The resource content.
     * 
     * @throws XmldbException
     *             If the resource could not be found or read.
     */
    byte[] retrieveResource(String uri) throws XmldbException;

    /**
     * Retrieves an XML document.
     * 
     * @param uri
     *            The document URI.
     * 
     * @return The document.
     * 
     * @throws XmldbException
     *             If the resource could not be found or read, or is not a valid
     *             XML document.
     */
    Document retrieveXmlDocument(String uri) throws XmldbException;

    /**
     * Stores an XML document.
     * 
     * @param uri
     *            The resource URI.
     * @param doc
     *            The XML document.
     * 
     * @throws XmldbException
     *             If the document could not be stored.
     */
    void storeResource(String uri, Document doc) throws XmldbException;

    /**
     * Stores a resource based on its content.
     * 
     * @param uri
     *            The resource URI.
     * @param content
     *            The content.
     * 
     * @throws XmldbException
     *             If the resource could not be stored.
     */
    void storeResource(String uri, String content) throws XmldbException;

    /**
     * Stores a resource based on an input stream with the content.
     * 
     * @param uri
     *            The resource URI.
     * @param is
     *            The input stream with the content.
     * 
     * @throws XmldbException
     *             If the input stream could not be read or the resource could
     *             not be stored.
     */
    void storeResource(String uri, InputStream is) throws XmldbException;

    /**
     * Imports a resource from a file or directory.
     * 
     * @param uri
     *            The resource URI.
     * @param file
     *            The file or directory.
     * 
     * @throws XmldbException
     *             If the file does not exist or could not be read, or the
     *             resource could not be stored.
     */
    void importResource(String uri, File file) throws XmldbException;

    /**
     * Exports a resource to a file or directory.
     * 
     * @param uri
     *            The resource URI.
     * @param file
     *            The file or directory.
     * 
     * @throws XmldbException
     *             If the resource does not exist or could not be read, or the
     *             file could not be written.
     */
    void exportResource(String uri, File file) throws XmldbException;

    /**
     * Deletes a resource (recursively).
     * 
     * @param uri
     *            The resource URI.
     * 
     * @throws XmldbException
     *             If the resource could not be deleted.
     */
    void deleteResource(String uri) throws XmldbException;

    /**
     * Executes an ad-hoc query.
     * 
     * @param query
     *            The query text.
     * 
     * @return The query result.
     * 
     * @throws XmldbException
     *             If the query could not be executed.
     */
    String executeQuery(String query) throws XmldbException;

    /**
     * Calls a stored, executable XQuery module.
     * 
     * @param uri
     *            The module URI.
     * @param params
     *            The (optional) module parameters.
     * 
     * @return The query result.
     * 
     * @throws XmldbException
     *             If the query could not be executed.
     */
    String callModule(String uri, Map<String, String> params) throws XmldbException;

    /**
     * Calls a stored XQuery function.
     * 
     * The function must be part of an XQuery module.
     * 
     * @param moduleNamespace
     *            The XQuery module namespace.
     * @param moduleUri
     *            The XQuery module URI.
     * @param functionName
     *            The XQuery function name.
     * @param params
     *            The (optional) parameter values.
     * 
     * @return The function result.
     */
    String callFunction(String moduleNamespace, String moduleUri, String functionName, String... params) throws XmldbException;

}
