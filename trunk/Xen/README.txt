README
======


Introduction
------------

Xantippe is an open source, lightweight XML database written in Java featuring
validation, XQuery and indexing. It is written by Oscar Stigter and published
under the Apache License 2.0.

Project page: http://code.google.com/p/xantippe


Features
--------

- Documents (XML, plain text or binary) are stored in binary files on the file
  system.

- Documents are hierarchically ordered in a tree of collections, similar to
  directories in a file system.

- XML documents can be automatically validated against their schema's.

- Documents can be quickly retrieved using indices based on document keys. XML
  documents can be indexed automatically when inserted or updated based on
  their contents. Document keys can also be set manually, which is especially
  useful for non-XML documents.

- Indices and validation are configured per collection using an inheritance
  tree.

- Queries can be exectued using XQuery modules stored in the database, or by
  executing queries ad hoc. The XQuery functionality is handled by Saxon-B.
  Saxon is an open source XQuery/XSLT processor written by Michael Kay
  (http://www.saxonica.org).


Status
------

This product is under heavy development, highly unstable and nowhere near
completion. Please use it or the code at your own risk!
