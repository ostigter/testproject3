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

        ontology.defineRelation("isProperty");
        ontology.defineRelation("hasProperty");

        ontology.defineRelation("isColor");
        ontology.defineRelation("hasColor");
        
        // Concepts
        
        ontology.defineConcept("Vehicle");
        ontology.defineConcept("Car");
        ontology.defineConcept("Engine");
        ontology.defineConcept("MyCar");
        ontology.defineConcept("Property");
        ontology.defineConcept("Color");
        ontology.defineConcept("Red");

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
        
        ontology.defineRelation("Color", "isProperty");
//        ontology.defineRelation("MyCar", "hasProperty", "Color", "Red");
        
        ontology.defineRelation("Red", "isColor");
        ontology.defineRelation("MyCar", "hasColor", "Red");
    }

}
