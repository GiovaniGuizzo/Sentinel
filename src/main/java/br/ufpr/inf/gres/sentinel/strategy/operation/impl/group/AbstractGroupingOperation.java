package br.ufpr.inf.gres.sentinel.strategy.operation.impl.group;

import br.ufpr.inf.gres.sentinel.base.mutation.Program;
import br.ufpr.inf.gres.sentinel.strategy.operation.Operation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Giovani Guizzo
 * @param <T>
 */
public abstract class AbstractGroupingOperation<T> extends Operation<Collection<T>, Collection<Collection<T>>> {

    /**
     *
     * @param name
     */
    public AbstractGroupingOperation(String name) {
        super(name);
    }

    /**
     *
     * @return
     */
    public abstract Function<T, ?> createGroupingFunction();

    /**
     *
     * @param input
     * @return
     */
    @Override
    public Collection<Collection<T>> doOperation(Collection<T> input, Program program) {
        Map<?, List<T>> collect = input.stream().collect(Collectors.groupingBy(this.createGroupingFunction()));
        return new ArrayList<>(collect.values());
    }

}
