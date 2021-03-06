package br.ufpr.inf.gres.sentinel.grammaticalevolution.mapper.strategy;

import br.ufpr.inf.gres.sentinel.base.mutation.Mutant;
import br.ufpr.inf.gres.sentinel.base.mutation.Program;
import br.ufpr.inf.gres.sentinel.base.solution.Solution;
import br.ufpr.inf.gres.sentinel.integration.IntegrationFacade;
import br.ufpr.inf.gres.sentinel.integration.IntegrationFacadeTest;
import br.ufpr.inf.gres.sentinel.integration.pit.PITFacade;
import br.ufpr.inf.gres.sentinel.strategy.Strategy;
import br.ufpr.inf.gres.sentinel.strategy.operation.Operation;
import br.ufpr.inf.gres.sentinel.strategy.operation.impl.defaults.AddAllOperatorsOperation;
import br.ufpr.inf.gres.sentinel.strategy.operation.impl.defaults.StoreMutantsOperation;
import br.ufpr.inf.gres.sentinel.strategy.operation.impl.execute.ExecuteOperatorsOperation;
import br.ufpr.inf.gres.sentinel.strategy.operation.impl.execute.type.impl.ConventionalExecution;
import br.ufpr.inf.gres.sentinel.strategy.operation.impl.select.operation.impl.RetainMutantsOperation;
import br.ufpr.inf.gres.sentinel.strategy.operation.impl.select.selection.SelectionOperation;
import br.ufpr.inf.gres.sentinel.strategy.operation.impl.select.type.impl.SequentialSelection;
import br.ufpr.inf.gres.sentinel.strategy.operation.impl.sort.impl.operator.OperatorTypeComparator;
import br.ufpr.inf.gres.sentinel.util.TestPrograms;
import com.google.common.collect.Lists;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Giovani Guizzo
 */
public class StrategyMapperTest {

    @BeforeClass
    public static void setUpClass() {
        IntegrationFacade.setIntegrationFacade(new IntegrationFacadeTest.IntegrationFacadeStub());
    }

    public StrategyMapperTest() {
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
        Strategy strategy
                = strategyMapper.interpret(Lists.newArrayList(0, 2, 1, 0, 0, 0, 0, 0, 9, 1, 0, 1, 0, 0, 1, 0, 3, 1, 2));
        assertNotNull(strategy);
        assertEquals("1.All Operators - 2.Execute Operators - 3.Retain Mutants - 4.Store Mutants", strategy.toString());

        Operation<Solution, Collection<Mutant>> operation = strategy.getFirstOperation();
        assertTrue(operation instanceof AddAllOperatorsOperation);

        assertTrue(operation.getSuccessor() instanceof ExecuteOperatorsOperation);

        ExecuteOperatorsOperation execute = (ExecuteOperatorsOperation) operation.getSuccessor();
        assertTrue(execute.getExecutionType() instanceof ConventionalExecution);
        assertTrue(execute.getSelection() instanceof SelectionOperation);
        assertEquals(1.0, execute.getSelection().getPercentage(), 0.01D);
        assertTrue(execute.getSelection().getSelectionType() instanceof SequentialSelection);
        assertTrue(execute.getSelection().getSorter() instanceof OperatorTypeComparator);
        assertFalse(execute.getSelection().getSorter().isReversed());

        assertTrue(execute.getSuccessor() instanceof RetainMutantsOperation);

        RetainMutantsOperation mutantsOperation = (RetainMutantsOperation) execute.getSuccessor();
        assertTrue(mutantsOperation.getSelection() instanceof SelectionOperation);
        assertEquals(0.1D, mutantsOperation.getSelection().getPercentage(), 0.01D);
        assertTrue(mutantsOperation.getSelection().getSelectionType() instanceof SequentialSelection);
        assertNull(mutantsOperation.getSelection().getSorter());

        assertTrue(mutantsOperation.getSuccessor() instanceof StoreMutantsOperation);

        StoreMutantsOperation store = (StoreMutantsOperation) mutantsOperation.getSuccessor();
        assertNull(store.getSuccessor());
    }

    @Test
    public void testCreate3() throws IOException {
        PITFacade facade = new PITFacade(System.getProperty("user.dir"));
        Program programUnderTest = facade.instantiateProgram(TestPrograms.TRIANGLE);
        IntegrationFacade.setIntegrationFacade(facade);

        StrategyMapper strategyMapper = new StrategyMapper(new File(GrammarFiles.getDefaultGrammarPath()));
        Strategy strategy
                = strategyMapper.interpret(Lists.newArrayList(0, 2, 1, 0, 0, 0, 0, 0, 9, 1, 0, 1, 0, 0, 1, 0, 3, 1, 2));
        assertNotNull(strategy);
        assertEquals("1.All Operators - 2.Execute Operators - 3.Retain Mutants - 4.Store Mutants", strategy.toString());

        Operation<Solution, Collection<Mutant>> operation = strategy.getFirstOperation();
        assertTrue(operation instanceof AddAllOperatorsOperation);

        assertTrue(operation.getSuccessor() instanceof ExecuteOperatorsOperation);

        ExecuteOperatorsOperation execute = (ExecuteOperatorsOperation) operation.getSuccessor();
        assertTrue(execute.getExecutionType() instanceof ConventionalExecution);
        assertTrue(execute.getSelection() instanceof SelectionOperation);
        assertEquals(1.0, execute.getSelection().getPercentage(), 0.01D);
        assertTrue(execute.getSelection().getSelectionType() instanceof SequentialSelection);
        assertTrue(execute.getSelection().getSorter() instanceof OperatorTypeComparator);
        assertFalse(execute.getSelection().getSorter().isReversed());

        assertTrue(execute.getSuccessor() instanceof RetainMutantsOperation);

        RetainMutantsOperation mutantsOperation = (RetainMutantsOperation) execute.getSuccessor();
        assertTrue(mutantsOperation.getSelection() instanceof SelectionOperation);
        assertEquals(0.1D, mutantsOperation.getSelection().getPercentage(), 0.01D);
        assertTrue(mutantsOperation.getSelection().getSelectionType() instanceof SequentialSelection);
        assertNull(mutantsOperation.getSelection().getSorter());

        assertTrue(mutantsOperation.getSuccessor() instanceof StoreMutantsOperation);

        StoreMutantsOperation store = (StoreMutantsOperation) mutantsOperation.getSuccessor();
        assertNull(store.getSuccessor());

        Collection<Mutant> result = strategy.run(programUnderTest);
        assertFalse(result.isEmpty());
        setUpClass();
    }

    @Test
    public void testHookInterpret() {
        StrategyMapper strategyMapper = new StrategyMapper();
        Strategy strategy = strategyMapper.interpret(Lists.newArrayList(1, 2, 3, 4, 5, 6));
        assertNotNull(strategy);
    }
}
