package org.ozsoft.ontology;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

public class Ontology {
    
    private static final Logger LOG = Logger.getLogger(Ontology.class);
    
    private final Map<String, Concept> concepts;

    private final Map<String, Relation> relations;
    
    public Ontology() {
        concepts = new HashMap<String, Concept>();
        relations = new HashMap<String, Relation>();
        LOG.debug("Created");
    }
    
    public void defineConcept(String conceptId) {
        if (!concepts.containsKey(conceptId)) {
            concepts.put(conceptId, new Concept(conceptId));
            LOG.debug(String.format("Defined concept '%s'", conceptId));
        }
    }
    
    public void defineRelation(String relationId) {
        if (!relations.containsKey(relationId)) {
            relations.put(relationId, new Relation(relationId));
            LOG.debug(String.format("Defined relation '%s'", relationId));
        }
    }
    
    public void defineRelation(String conceptId, String relationId) {
        Concept concept = concepts.get(conceptId);
        if (concept == null) {
            throw new IllegalArgumentException("Concept not found: " + conceptId);
        }
        Relation relation = relations.get(relationId);
        if (relation == null) {
            throw new IllegalArgumentException("Relation not found: " + relationId);
        }
        concept.addRelation(relation);
        LOG.debug(String.format("Defined relation '%s(%s)'", relationId, conceptId));
    }
    
    public void defineRelation(String conceptId1, String relationId, String conceptId2) {
        if (conceptId1.equals(conceptId2)) {
            throw new IllegalArgumentException("Cannot add relation between two identical objects");
        }
        Concept object1 = concepts.get(conceptId1);
        if (object1 == null) {
            throw new IllegalArgumentException("Concept not found: " + conceptId1);
        }
        Relation relation = relations.get(relationId);
        if (relation == null) {
            throw new IllegalArgumentException("Relation not found: " + relationId);
        }
        Concept object2 = concepts.get(conceptId2);
        if (object2 == null) {
            throw new IllegalArgumentException("Concept not found: " + conceptId2);
        }
        object1.addRelation(relation, object2);
        LOG.debug(String.format("Defined relation '%s(%s, %s)'", relationId, conceptId1, conceptId2));
    }
    
    public boolean isConcept(String id) {
        return concepts.containsKey(id);
    }
    
    public boolean isRelation(String id) {
        return relations.containsKey(id);
    }
    
    public boolean hasRelation(String conceptId, String relationId) {
        Concept concept = concepts.get(conceptId);
        if (concept == null) {
            throw new IllegalArgumentException("Concept not found: " + conceptId);
        }
        return concept.hasRelation(relationId);
    }
    
    /**
     * Checks whether there exists a binary relation between two concepts.
     * 
     * @param conceptId1
     *            The first concept.
     * @param relationId
     *            The relation.
     * @param concept2
     *            The second concept.
     * 
     * @return True if the binary relation exists, otherwise false.
     */
    public boolean hasRelation(String conceptId1, String relationId, String conceptId2) {
        Concept concept1 = concepts.get(conceptId1);
        if (concept1 == null) {
            throw new IllegalArgumentException("Concept not found: " + conceptId1);
        }
        Relation relation = relations.get(relationId);
        if (relation == null) {
            throw new IllegalArgumentException("Relation not found: " + relationId);
        }
        Concept concept2 = concepts.get(conceptId2);
        if (concept2 == null) {
            throw new IllegalArgumentException("Concept not found: " + conceptId2);
        }
        return relation.hasConcept(concept2);
    }
    
}
