package br.ufpr.inf.gres.sentinel.integration;

import br.ufpr.inf.gres.sentinel.base.mutation.Mutant;
import br.ufpr.inf.gres.sentinel.base.mutation.Operator;
import br.ufpr.inf.gres.sentinel.base.mutation.Program;

import java.util.List;

/**
 * @author Giovani Guizzo
 */
public abstract class IntegrationFacade {

	private static IntegrationFacade FACADE_INSTANCE;
	private static Program PROGRAM_UNDER_TEST;

	public static IntegrationFacade getIntegrationFacade() {
		return FACADE_INSTANCE;
	}

	public static void setIntegrationFacade(IntegrationFacade facade) {
		FACADE_INSTANCE = facade;
	}

	public static Program getProgramUnderTest() {
		return PROGRAM_UNDER_TEST;
	}

	public static void setProgramUnderTest(Program programUnderTest) {
		IntegrationFacade.PROGRAM_UNDER_TEST = programUnderTest;
	}

	public abstract List<Operator> getAllOperators();

	public abstract List<Mutant> executeOperator(Operator operator, Program programToBeMutated);

	public abstract Mutant combineMutants(List<Mutant> mutantsToCombine);

}