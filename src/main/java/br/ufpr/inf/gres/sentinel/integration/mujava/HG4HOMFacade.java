package br.ufpr.inf.gres.sentinel.integration.mujava;

import br.ufpr.inf.gres.hg4hom.core.MutationSystem;
import br.ufpr.inf.gres.hg4hom.core.classpath.ClassInfo;
import br.ufpr.inf.gres.hg4hom.core.classpath.Resources;
import br.ufpr.inf.gres.hg4hom.core.enums.MutationOperatorType;
import br.ufpr.inf.gres.hg4hom.core.enums.MutationTestResultType;
import br.ufpr.inf.gres.hg4hom.core.enums.operator.ClassMutationOperator;
import br.ufpr.inf.gres.hg4hom.core.enums.operator.TraditionalMutationOperator;
import br.ufpr.inf.gres.hg4hom.core.exceptions.HomException;
import br.ufpr.inf.gres.hg4hom.core.mutation.generator.AbstractMutantsGenerator;
import br.ufpr.inf.gres.hg4hom.core.mutation.generator.SelectionMutantsGeneratorFactory;
import br.ufpr.inf.gres.hg4hom.core.mutation.type.Fom;
import br.ufpr.inf.gres.hg4hom.core.mutation.type.Hom;
import br.ufpr.inf.gres.hg4hom.core.test.execution.TestResult;
import br.ufpr.inf.gres.hg4hom.core.test.runner.AbstractTestBuilder;
import br.ufpr.inf.gres.hg4hom.core.test.runner.ClassMutantsBuilder;
import br.ufpr.inf.gres.hg4hom.core.test.runner.TraditionalMutantsBuilder;
import br.ufpr.inf.gres.hg4hom.core.util.mutation.MutationLog;
import br.ufpr.inf.gres.hg4hom.hom.runner.HomBuilder;
import br.ufpr.inf.gres.hg4hom.hom.strategies.impl.EachChoiceStrategy;
import br.ufpr.inf.gres.sentinel.base.mutation.Mutant;
import br.ufpr.inf.gres.sentinel.base.mutation.Operator;
import br.ufpr.inf.gres.sentinel.base.mutation.Program;
import br.ufpr.inf.gres.sentinel.base.mutation.TestCase;
import br.ufpr.inf.gres.sentinel.integration.IntegrationFacade;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.list.SetUniqueList;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Giovani Guizzo
 */
public class HG4HOMFacade extends IntegrationFacade {

	/**
	 * List of names of class mutation operators
	 */
	public static final ClassMutationOperator[] CM_OPERATORS = ClassMutationOperator.values();

	/**
	 * List of names of traditional mutation operators
	 */
	public static final TraditionalMutationOperator[] TM_OPERATORS = TraditionalMutationOperator.values();

	private String muJavaHome;

	public HG4HOMFacade(String muJavaHome) {
		this.muJavaHome = muJavaHome;
	}

	public String getMuJavaHome() {
		return muJavaHome;
	}

	public void setMuJavaHome(String muJavaHome) {
		this.muJavaHome = muJavaHome;
	}

	@Override
	public List<Operator> getAllOperators() {
		List<Operator> operators = new ArrayList<>();
		for (ClassMutationOperator operator : CM_OPERATORS) {
			operators.add(new Operator(operator.getValue(), "Class_" + operator.getValue().charAt(0)));
		}
		for (TraditionalMutationOperator operator : TM_OPERATORS) {
			operators.add(new Operator(operator.getValue(), "Traditional_" + operator.getValue().charAt(0)));
		}
		return operators;
	}

	public List<Mutant> executeOperator(Operator operator) {
		Program programToBeMutated = IntegrationFacade.getProgramUnderTest();

		Preconditions.checkNotNull(operator, "Operator cannot be null.");
		Preconditions.checkNotNull(programToBeMutated, "Program to be Mutated cannot be null.");

		MutationSystem.setJMutationStructure(muJavaHome, programToBeMutated.getSimpleName());

		List<Mutant> mutants = new ArrayList<>();

		MutationOperatorType mutationType;
		if (operator.getType().startsWith("Class")) {
			mutationType = MutationOperatorType.ClassMutation;
		} else {
			mutationType = MutationOperatorType.TraditionalMutation;
		}

		try {
			ClassInfo originalClass = new ClassInfo(programToBeMutated.getSourceFile(), new File(MutationSystem.SRC_PATH));
			ClassInfo testSet = new Resources(MutationSystem.TESTSET_PATH).getClasses()
																		  .stream()
																		  .filter(test -> test.getClassName()
																							  .equals(programToBeMutated
																									  .getSimpleName() + "Test"))
																		  .collect(Collectors.toList())
																		  .get(0);

			MutationSystem.setMutationSystemPath(originalClass);
			MutationSystem.recordInheritanceRelation();

			List<String> mutationOperators = Lists.newArrayList(operator.getName());

			AbstractMutantsGenerator mutantsGenerator = new SelectionMutantsGeneratorFactory().getMutantsGeneratorSelector(originalClass, mutationType, mutationOperators);
			mutantsGenerator.makeMutants();
			mutantsGenerator.compileMutants();

			TestResult testResult;
			AbstractTestBuilder executor;
			if (mutationType.equals(MutationOperatorType.TraditionalMutation)) {
				executor = new TraditionalMutantsBuilder(testSet, originalClass);

				List<String> methods = ((TraditionalMutantsBuilder) executor).getMethods();
				for (String method : methods) {
					MutationSystem.MUTANT_PATH = MutationSystem.TRADITIONAL_MUTANT_PATH + File.separator + method;
					executor.runMutants(executor.getMutants()
												.stream()
												.filter(s -> s.startsWith(operator.getName() + "_"))
												.collect(Collectors.toList()));
				}
			} else {
				executor = new ClassMutantsBuilder(testSet, originalClass);

				executor.runMutants(executor.getMutants()
											.stream()
											.filter(s -> s.startsWith(operator.getName() + "_"))
											.collect(Collectors.toList()));
			}
			testResult = executor.classifyResult();
			for (Map.Entry<String, MutationTestResultType> entry : testResult.mutantState.entrySet()) {
				String mutantName = entry.getKey();
				Mutant mutant = new Mutant(mutantName, programToBeMutated.getSourceFile(), programToBeMutated);
				mutant.getOperators().add(operator);
				ArrayList<String> testCases = testResult.testCaseMutants.get(mutantName);
				mutant.getKillingTestCases().addAll(testCases.stream().map(TestCase::new).collect(Collectors.toList()));
				mutants.add(mutant);
			}
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}

		return mutants;
	}

	@Override
	public Mutant combineMutants(List<Mutant> mutantsToCombine) {
		Preconditions.checkNotNull(mutantsToCombine, "Mutant List cannot be null.");
		Preconditions.checkArgument(mutantsToCombine.size() == 2, "There must be exactly 2 mutants in the list. Unfortunately, more than 2 mutants is not supported right now. Found mutants: " + mutantsToCombine
				.size() + ".");

		try {
			if (mutantsToCombine.stream().noneMatch(Mutant::isHigherOrder)) {
				Program programToBeMutated = IntegrationFacade.getProgramUnderTest();
				MutationSystem.setJMutationStructure(muJavaHome, programToBeMutated.getSimpleName());

				ClassInfo originalClass = new ClassInfo(programToBeMutated.getSourceFile(), new File(MutationSystem.SRC_PATH));
				ClassInfo testSet = new Resources(MutationSystem.TESTSET_PATH).getClasses()
																			  .stream()
																			  .filter(test -> test.getClassName()
																								  .equals(programToBeMutated
																										  .getSimpleName() + "Test"))
																			  .collect(Collectors.toList())
																			  .get(0);

				MutationSystem.setMutationSystemPath(originalClass);

				MutationLog<Fom> mutationLog = new MutationLog();
				mutationLog.setPath(MutationSystem.CLASS_MUTANT_PATH);
				ArrayList<Fom> foms = mutationLog.load(Fom[].class);
				mutationLog.setPath(MutationSystem.TRADITIONAL_MUTANT_PATH);
				foms.addAll(mutationLog.load(Fom[].class));
				List<String> mutantNames = mutantsToCombine.stream()
														   .map(Program::getFullName)
														   .collect(Collectors.toList());
				foms.removeIf(fom -> !mutantNames.contains(fom.getName()));

				MutationSystem.MUTANT_PATH = MutationSystem.HIGHER_ORDER_MUTANT_PATH;
				HomBuilder builder = new HomBuilder(new EachChoiceStrategy(foms), originalClass, testSet);
				Hom hom = builder.create();
				builder.compile(hom);
				builder.test(hom);

				Mutant generatedHom = new Mutant(hom.getName(), new File(hom.getPath()), programToBeMutated);
				generatedHom.getConstituentMutants().addAll(mutantsToCombine);
				generatedHom.getKillingTestCases()
							.addAll(hom.getTestCases().stream().map(TestCase::new).collect(Collectors.toList()));
				generatedHom.getOperators()
							.addAll(mutantsToCombine.stream()
													.map(Mutant::getOperators)
													.reduce((operators, operators2) -> {
														SetUniqueList<Operator> union = SetUniqueList.setUniqueList(new ArrayList<>());
														union.addAll(operators);
														union.addAll(operators2);
														return union;
													})
													.get());
				return generatedHom;
			}
		} catch (HomException e) {
			e.printStackTrace();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return null;
	}

}