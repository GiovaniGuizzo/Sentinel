package br.ufpr.inf.gres.sentinel.base.mutation;

import java.util.LinkedHashSet;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Giovani Guizzo
 */
public class OperatorTest {

    public OperatorTest() {
    }

    @Test
    public void testCloneConstructor() {
        Operator instance = new Operator("Operator1", "Type1");
        instance.getGeneratedMutants().add(new Mutant("Mutant1", null, new Program("Program1", "Program/path")));
        instance.getGeneratedMutants().add(new Mutant("Mutant2", null, new Program("Program1", "Program/path")));
        Operator instance2 = new Operator(instance);
        assertEquals(instance, instance2);
        assertEquals(instance.getType(), instance2.getType());
        assertArrayEquals(instance.getGeneratedMutants().toArray(), instance2.getGeneratedMutants().toArray());
    }

    @Test
    public void testEquals() {
        Operator instance = new Operator("Operator1", "Type1");
        Operator instance2 = new Operator("Operator1", "Type1");
        assertEquals(instance, instance2);
    }

    @Test
    public void testEquals2() {
        Operator instance = new Operator("Operator1", "Type1");
        Operator instance2 = new Operator("Operator2", "Type1");
        assertNotEquals(instance, instance2);
    }

    @Test
    public void testEquals3() {
        Operator instance = new Operator("Operator1", "Type1");
        Operator instance2 = new Operator("Operator1", "Type2");
        assertEquals(instance, instance2);
    }

    @Test
    public void testEquals4() {
        Operator instance = new Operator("Operator1", "Type1");
        Operator instance2 = instance;
        assertEquals(instance, instance2);
    }

    @Test
    public void testEquals5() {
        Operator instance = new Operator("Operator1", "Type1");
        Operator instance2 = null;
        assertNotEquals(instance, instance2);
    }

    @Test
    public void testEquals6() {
        Operator instance = new Operator("Operator1", "Type1");
        Object instance2 = new Object();
        assertNotEquals(instance, instance2);
    }

    @Test
    public void testGetAndSetGeneratedMutants() {
        Operator instance = new Operator("Operator1", "Type1");
        LinkedHashSet<Mutant> setUniqueList = new LinkedHashSet<>();
        instance.setGeneratedMutants(setUniqueList);
        LinkedHashSet<Mutant> result = instance.getGeneratedMutants();
        assertEquals(setUniqueList, result);
    }

    @Test
    public void testGetAndSetName() {
        Operator instance = new Operator("Operator1", "Type1");
        instance.setName("OperatorTest");
        String result = instance.getName();
        assertEquals("OperatorTest", result);
    }

    @Test
    public void testGetAndSetType() {
        Operator instance = new Operator("Operator1", "Type1");
        instance.setType("TypeTest");
        String result = instance.getType();
        assertEquals("TypeTest", result);
    }

    @Test
    public void testHashCode() {
        Operator instance = new Operator("Operator1", "Type1");
        Operator instance2 = new Operator("Operator1", "Type1");
        int result = instance.hashCode();
        int result2 = instance2.hashCode();
        assertEquals(result, result2);
    }

    @Test
    public void testHashCode2() {
        Operator instance = new Operator("Operator1", "Type1");
        Operator instance2 = new Operator("Operator2", "Type1");
        int result = instance.hashCode();
        int result2 = instance2.hashCode();
        assertNotEquals(result, result2);
    }

    @Test
    public void testHashCode3() {
        Operator instance = new Operator("Operator1", "Type1");
        Operator instance2 = instance;
        int result = instance.hashCode();
        int result2 = instance2.hashCode();
        assertEquals(result, result2);
    }

    @Test
    public void testHashCode4() {
        Operator instance = new Operator("Operator1", "Type1");
        Operator instance2 = new Operator("Operator1", "Type2");
        int result = instance.hashCode();
        int result2 = instance2.hashCode();
        assertEquals(result, result2);
    }

    @Test
    public void testToString() {
        Operator instance = new Operator("Operator1", "Type1");
        assertEquals("Operator1", instance.toString());
    }

}
