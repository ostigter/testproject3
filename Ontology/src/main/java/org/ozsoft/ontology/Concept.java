package org.ozsoft.ontology;

import java.util.HashMap;
import java.util.Map;

public class Concept {
    
    private final String id;

    private final Map<String, Relation> relations;
    
    public Concept(String id) {
        this.id = id;
        relations = new HashMap<String, Relation>();
    }
    
    public String getId() {
        return id;
    }
    
    public void addRelation(Relation relation) {
        String relationId = relation.getId();
        if (!relations.containsKey(relationId)) {
            relations.put(relationId, relation);
        }
    }

    public void addRelation(Relation relation, Concept concept) {
        String relationId = relation.getId();
        Relation relation2 = relations.get(relationId);
        if (relation2 == null) {
            relation2 = relation;
        }
        relation.addConcept(concept);
        relations.put(relationId, relation);
    }
    
    public boolean hasRelation(String relationId) {
        return relations.containsKey(relationId);
    }
    
    public Relation getRelation(String relationId) {
        return relations.get(relationId);
    }
    
    @Override
    public String toString() {
        return id;
    }

}
