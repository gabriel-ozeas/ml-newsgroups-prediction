# ml-newsgroups-prediction
Some machine learning algorithm for predict the famous 20 news groups dataset

## How to execute experiments?

You need to have Java 8 JRE or JDK installed in your computer with JAVA_HOME set in your env variables.

You can execute the experiments running `bin/newsgroup-classication-exe [OPTIONS]`

The options are:

`--adaboost-multinomial-experiment`

This experiment create a classifier using AdaBoost algorithm with Naive Bayes Multinomial. You can use the parameters -number-of-words to specify how many words will be used in the vocabulary. -tranning-size specifies the percentage of  instances that will be used in tranning. 

`--arff-experiment`

This experiment export the ARFF training and testing files

`--dataset-dir`

Set the dataset directory path

`--decision-tree-experiment`

This experiment create a classifier using decision-tree algorithm. You can use the parameters -number-of-words to specify how many words will be used in the
vocabulary. -tranning-size specifies the percentage of instances that will be used in tranning.

`--download-dataset`

This option will download the dataset from internet

`--find-best-number-of-attributes-experiment`

This experiment evaluate classifiers with diferent attribute number trainning. It uses Naive Bayes and Naive Bayes Multinomial to build the classifiers. By
default experiment will run with 100, 1000, 2500, 5000, 20000, 40000 words

`--help`

Print this message

`--knn-experiment`

This experiment create a classifier using knn algorithm. You can use the parameters -number-of-words to specify how many words will be used in the vocabulary. -tranning-size specifies the percentage of instances that will be used in tranning.

`--naive-bayes-experiment`

This experiment create a classifier using naive-bayes algorithm. You can use the parameters -number-of-words to specify how many words will be used in the
vocabulary. -tranning-size specifies the percentage of instances that will be used in tranning.

`--naive-bayes-multinomial-experiment`
This experiment create a classifier using naive-bayes multinomial algorithm. You can use the parameters -number-of-words to specify how many words will be used in the vocabulary. -tranning-size specifies the percentage of instances that will be used in tranning.

## Examples

#### Running naive-bayes experiment
`bin/newsgroup-classication-exe --download-dataset --naive-bayes-experiment`

## How to build?

To build and package the distribution just execute `./gradlew clean build distZip`. It will generate a zip file in build/distribution directory. 
