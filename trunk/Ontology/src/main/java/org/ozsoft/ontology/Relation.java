package org.ozsoft.ontology;

import java.util.HashMap;
import java.util.Map;

public class Relation extends Concept {
    
    private final Map<String, Concept> concepts;
    
    private Relation inverseRelation;
    
    public Relation(String id) {
        super(id);
        concepts = new HashMap<String, Concept>();
    }
    
    public Relation getInverRelation() {
        return inverseRelation;
    }
    
    public void setInserveRelation(Relation relation) {
        inverseRelation = relation;
    }
    
    public void addConcept(Concept concept) {
        String objectId = concept.getId();
        if (!concepts.containsKey(objectId)) {
            concepts.put(objectId, concept);
        }
    }
    
    public boolean hasConcept(Concept concept) {
        return concepts.containsKey(concept);
    }

}
