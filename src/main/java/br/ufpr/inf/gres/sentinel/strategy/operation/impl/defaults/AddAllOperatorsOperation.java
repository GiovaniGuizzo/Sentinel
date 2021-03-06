package br.ufpr.inf.gres.sentinel.strategy.operation.impl.defaults;

import br.ufpr.inf.gres.sentinel.base.mutation.Mutant;
import br.ufpr.inf.gres.sentinel.base.mutation.Program;
import br.ufpr.inf.gres.sentinel.base.solution.Solution;
import br.ufpr.inf.gres.sentinel.grammaticalevolution.mapper.strategy.factory.TerminalRuleType;
import br.ufpr.inf.gres.sentinel.integration.IntegrationFacade;
import br.ufpr.inf.gres.sentinel.strategy.operation.Operation;
import java.util.Collection;

/**
 * @author Giovani Guizzo
 */
public class AddAllOperatorsOperation extends Operation<Solution, Collection<Mutant>> {

    /**
     *
     */
    public AddAllOperatorsOperation() {
        super(TerminalRuleType.ALL_OPERATORS);
    }

    /**
     *
     * @param solution
     * @return
     */
    @Override
    public Collection<Mutant> doOperation(Solution solution, Program program) {
        solution.getOperators().addAll(IntegrationFacade.getIntegrationFacade().getAllOperators());
        return this.next(solution, program);
    }

    /**
     *
     * @return
     */
    @Override
    public boolean isSpecific() {
        return false;
    }

}
