package org.ozsoft.ontology;

public class Main {

    public static void main(String[] args) {
        Ontology ontology = new Ontology();
        
        // Relations
        
        ontology.defineRelation("isClass");
        ontology.defineRelation("extends");
        ontology.defineRelation("isExtendedBy");

        ontology.defineRelation("isInstance");
        ontology.defineRelation("implements");
        ontology.defineRelation("isImplementedBy");

        ontology.defineRelation("hasPart");
        ontology.defineRelation("isPartOf");
        
        // Concepts
        
        ontology.defineConcept("Vehicle");
        ontology.defineConcept("Car");
        ontology.defineConcept("Engine");
        ontology.defineConcept("MyCar");
        

        // Concept relations
        
        ontology.defineRelation("Vehicle", "isClass");
        ontology.defineRelation("Car", "isClass");
        ontology.defineRelation("Engine", "isClass");
        
        ontology.defineRelation("Car", "extends", "Vehicle");
        ontology.defineRelation("Vehicle", "isExtendedBy", "Car");
        
        ontology.defineRelation("Car", "hasPart", "Engine");
        ontology.defineRelation("Engine", "isPartOf", "Car");
        
        ontology.defineRelation("MyCar", "isInstance");
        ontology.defineRelation("MyCar", "implements", "Car");
        ontology.defineRelation("Car", "isImplementedBy", "MyCar");
    }

}
