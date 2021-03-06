# Sentinel

Mutation testing is an effective approach to evaluate and strengthen software test suites, but its adoption is currently limited by the mutants' execution computational cost. Several strategies have been proposed to reduce this cost (a.k.a. mutation cost reduction strategies), however none of them has proven to be effective for all scenarios since they often need an ad-hoc manual selection and configuration depending on the software under test (SUT).

We propose a novel multi-objective evolutionary hyper-heuristic approach, dubbed Sentinel, to automate the generation of optimal cost reduction strategies for every new SUT. Sentinel generates strategies that can be reused in newer versions of the software.

Sentinel uses [PIT](https://pitest.org/) as the mutation framework.

# Usage

This project uses maven. So, the first step is to compile and test Sentinel with the following command:

```bash
mvn clean install
```

Bear in mind that the testing phase may take a while (up to 10 minutes).

Sentinel can be invoked through its command line class `br.ufpr.inf.gres.sentinel.main.cli.Sentinel`. Try:

```bash
java -cp target/Sentinel-0.3-jar-with-dependencies.jar  br.ufpr.inf.gres.sentinel.main.cli.Sentinel --help
```

or directly from the jar:

```bash
java -jar target/Sentinel-0.3-jar-with-dependencies.jar --help
```

The most important and usually the only required argument for training and testing is `-tp`, which defines the training/testing program. This argument is given in the format:

```bash
<name>;<sourceDir>;<targetClassesGlob>;<targetTestsGlob>;<excludedTestClassesGlob>;<classpathItems>
```

For example, to train Sentinel with the included project `Triangle`, you can run the following command line from the project's main directory:

```bash
java -cp 'target/Sentinel-0.3-jar-with-dependencies.jar' br.ufpr.inf.gres.sentinel.main.cli.Sentinel train -c -tp "Triangle;src/test;br.ufpr.inf.gres.Tri*;br.ufpr.inf.gres.Tri*Test*;org.excluded.tests.*;src/test"
```

Optionally (and also encouraged), the argument `-c` can be provided to improve the performance of Sentinel training with the usage a of a cache mechanism. The first few mutant executions will take longer, but then the training will be done in a matter of seconds. The cache results are stored in `.cache` for future use, thus any subsequent training will use such a file.

At the end, the default training output directory `training` will contain the results of the training process with the best strategies found. These files describe the strategies. If used with Sentinel in the testing phase, the strategies will be reconstructed, executed, and then compared to predefined Random Mutant Sampling, Random Operator Selection, and Selective Mutation strategies.

# Reference

The published paper can be found [here](https://ieeexplore.ieee.org/document/9117067) ([preprint](https://bit.ly/Guizzo-TSE-2020)).

More information about the research behind Sentinel can be found on its [official webpage](https://solar.cs.ucl.ac.uk/os/sentinel).

An experimental package can be downloaded [from this link](https://bit.ly/Sentinel-Replication). It contains the assets needed to run the experiments for the testing phase.

## Bibtex:

```bibtex
@Article{Guizzo2020,
  author =  {Giovani Guizzo and Federica Sarro and Jens Krinke and Silvia Regina Vergilio},
  title =   {{Sentinel: A Hyper-Heuristic for the Generation of Mutant Reduction Strategies}},
  journal = {Transactions on Software Engineering},
  year =    {2020},
  issn =    {1939-3520},
  doi =     {10.1109/TSE.2020.3002496}
}
```
