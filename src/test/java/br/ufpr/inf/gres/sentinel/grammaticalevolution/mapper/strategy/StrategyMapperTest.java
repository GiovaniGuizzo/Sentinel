package br.ufpr.inf.gres.sentinel.grammaticalevolution.mapper.strategy;

import br.ufpr.inf.gres.sentinel.base.mutation.Mutant;
import br.ufpr.inf.gres.sentinel.base.solution.Solution;
import br.ufpr.inf.gres.sentinel.grammaticalevolution.mapper.AbstractGrammarMapperTest;
import br.ufpr.inf.gres.sentinel.integration.IntegrationFacade;
import br.ufpr.inf.gres.sentinel.integration.IntegrationFacadeTest;
import br.ufpr.inf.gres.sentinel.strategy.Strategy;
import br.ufpr.inf.gres.sentinel.strategy.operation.Operation;
import br.ufpr.inf.gres.sentinel.strategy.operation.impl.defaults.AddAllOperatorsOperation;
import br.ufpr.inf.gres.sentinel.strategy.operation.impl.defaults.StoreMutantsOperation;
import br.ufpr.inf.gres.sentinel.strategy.operation.impl.execute.ExecuteOperatorsOperation;
import br.ufpr.inf.gres.sentinel.strategy.operation.impl.execute.type.impl.ConventionalExecution;
import br.ufpr.inf.gres.sentinel.strategy.operation.impl.select.operation.impl.SelectMutantsOperation;
import br.ufpr.inf.gres.sentinel.strategy.operation.impl.select.selection.SelectionOperation;
import br.ufpr.inf.gres.sentinel.strategy.operation.impl.select.type.impl.SequentialSelection;
import br.ufpr.inf.gres.sentinel.strategy.operation.impl.sort.impl.operator.OperatorTypeComparator;
import com.google.common.collect.Lists;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.*;

/**
 * @author Giovani Guizzo
 */
public class StrategyMapperTest {

	private static StrategyMapper DEFAULT_MAPPER;

	public StrategyMapperTest() {
	}

	@BeforeClass
	public static void setUpClass() {
		try {
			IntegrationFacade.setIntegrationFacade(new IntegrationFacadeTest.IntegrationFacadeStub());
			DEFAULT_MAPPER = new StrategyMapper(GrammarFiles.getDefaultGrammarPath());
		} catch (IOException ex) {
			Logger.getLogger(AbstractGrammarMapperTest.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@Test
	public void testHookInterpret() {
		StrategyMapper strategyMapper = new StrategyMapper();
		Strategy strategy = strategyMapper.interpret(Lists.newArrayList(1, 2, 3, 4, 5, 6));
		assertNotNull(strategy);
	}

	@Test
	public void testCreate() throws IOException {
		StrategyMapper strategyMapper = new StrategyMapper(new File(GrammarFiles.getDefaultGrammarPath()));
		Strategy strategy = strategyMapper.interpret(Lists.newArrayList(3));
		assertNotNull(strategy);
	}

	@Test
	public void testCreate2() throws IOException {
		StrategyMapper strategyMapper = new StrategyMapper(new File(GrammarFiles.getDefaultGrammarPath()));
		Strategy strategy = strategyMapper.interpret(Lists.newArrayList(0, 2, 1, 0, 0, 0, 0, 0, 9, 1, 0, 1, 0, 0, 1, 0, 3, 1, 2));
		assertNotNull(strategy);
		assertEquals("1.All Operators - 2.Execute Operators - 3.Select Mutants - 4.Store Mutants", strategy.toString());

		Operation<Solution, List<Mutant>> operation = strategy.getFirstOperation();
		assertTrue(operation instanceof AddAllOperatorsOperation);

		assertTrue(operation.getSuccessor() instanceof ExecuteOperatorsOperation);

		ExecuteOperatorsOperation execute = (ExecuteOperatorsOperation) operation.getSuccessor();
		assertTrue(execute.getExecutionType() instanceof ConventionalExecution);
		assertTrue(execute.getSelection() instanceof SelectionOperation);
		assertEquals(1.0, execute.getSelection().getPercentage(), 0.01D);
		assertTrue(execute.getSelection().getSelectionType() instanceof SequentialSelection);
		assertTrue(execute.getSelection().getSorter() instanceof OperatorTypeComparator);
		assertFalse(execute.getSelection().getSorter().isReversed());

		assertTrue(execute.getSuccessor() instanceof SelectMutantsOperation);

		SelectMutantsOperation mutantsOperation = (SelectMutantsOperation) execute.getSuccessor();
		assertTrue(mutantsOperation.getSelection() instanceof SelectionOperation);
		assertEquals(0.1D, mutantsOperation.getSelection().getPercentage(), 0.01D);
		assertTrue(mutantsOperation.getSelection().getSelectionType() instanceof SequentialSelection);
		assertNull(mutantsOperation.getSelection().getSorter());

		assertTrue(mutantsOperation.getSuccessor() instanceof StoreMutantsOperation);

		StoreMutantsOperation store = (StoreMutantsOperation) mutantsOperation.getSuccessor();
		assertNull(store.getSuccessor());
	}
}