package org.ozsoft.ontology;

import org.junit.Assert;
import org.junit.Test;

public class OntologyTest {
    
    @Test
    public void test() {
        Ontology ontology = new Ontology();
        Assert.assertNotNull(ontology);
        
        Assert.assertFalse(ontology.isConcept("Car"));
        ontology.defineConcept("Car");
        Assert.assertTrue(ontology.isConcept("Car"));
        
        Assert.assertFalse(ontology.isRelation("isClass"));
        ontology.defineRelation("isClass");
        Assert.assertTrue(ontology.isRelation("isClass"));
        
        ontology.defineRelation("Car", "isClass");
        Assert.assertTrue(ontology.hasRelation("Car", "isClass"));

        ontology.defineRelation("isInstance");
        Assert.assertFalse(ontology.hasRelation("Car", "isInstance"));

        ontology.defineConcept("Vehicle");

        ontology.defineRelation("isSuperClassOf");
        ontology.defineRelation("Vehicle", "isSuperClassOf", "Car");
        Assert.assertTrue(ontology.hasRelation("Vehicle", "isSuperClassOf", "Car"));

        ontology.defineRelation("isSubClassOf");
        ontology.defineRelation("Car", "isSubClassOf", "Vehicle");
        
//        ontology.defineConcept("MyCar");
//        ontology.defineRelation("isInstance");
//        ontology.defineRelation("isInstanceOf");
//        ontology.defineRelation("Car", "isClass");
//        ontology.defineRelation("Vehicle", "isClass");
//        ontology.defineRelation("Vehicle", "isSuperClassOf", "Car");
//        ontology.defineRelation("Car", "isSuperClassOf", "SEAT");
//        ontology.defineRelation("MyCar", "isInstance");
    }

}
