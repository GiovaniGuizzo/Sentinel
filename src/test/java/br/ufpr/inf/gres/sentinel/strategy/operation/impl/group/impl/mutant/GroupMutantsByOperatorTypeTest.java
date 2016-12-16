package br.ufpr.inf.gres.sentinel.strategy.operation.impl.group.impl.mutant;

import br.ufpr.inf.gres.sentinel.base.mutation.Mutant;
import br.ufpr.inf.gres.sentinel.base.mutation.Operator;
import br.ufpr.inf.gres.sentinel.integration.IntegrationFacade;
import org.junit.Test;

import java.util.function.Function;

import static org.junit.Assert.*;

public class GroupMutantsByOperatorTypeTest {

	@Test
	public void createGroupingFunction() throws Exception {
		GroupMutantsByOperatorType operation = new GroupMutantsByOperatorType();
		Function<Mutant, String> function = operation.createGroupingFunction();

		Mutant mutant1 = new Mutant("Mutant1", null, IntegrationFacade.getProgramUnderTest());
		Mutant mutant2 = new Mutant("Mutant2", null, IntegrationFacade.getProgramUnderTest());

		Operator operator1 = new Operator("Operator1", "Type1");
		Operator operator2 = new Operator("Operator2", "Type2");
		Operator operator3 = new Operator("Operator3", "Type3");

		mutant1.getOperators().add(operator1);
		mutant1.getOperators().add(operator2);
		mutant1.getOperators().add(operator3);

		mutant2.getOperators().add(operator1);

		assertEquals("Type1", function.apply(mutant1));
		assertEquals("Type1", function.apply(mutant2));
	}

	@Test
	public void createGroupingFunction2() throws Exception {
		GroupMutantsByOperatorType operation = new GroupMutantsByOperatorType();
		Function<Mutant, String> function = operation.createGroupingFunction();

		Mutant mutant1 = new Mutant("Mutant1", null, IntegrationFacade.getProgramUnderTest());
		Mutant mutant2 = new Mutant("Mutant2", null, IntegrationFacade.getProgramUnderTest());

		Operator operator1 = new Operator("Operator1", "Type1");
		Operator operator2 = new Operator("Operator2", "Type2");
		Operator operator3 = new Operator("Operator3", "Type3");

		mutant1.getOperators().add(operator3);

		mutant2.getOperators().add(operator2);
		mutant2.getOperators().add(operator3);

		assertEquals("Type3", function.apply(mutant1));
		assertEquals("Type2", function.apply(mutant2));
	}

	@Test
	public void isSpecific() throws Exception {
		GroupMutantsByOperatorType operation = new GroupMutantsByOperatorType();
		assertFalse(operation.isSpecific());
	}

}