# A benchmark for feature selection when feature models evolve


## Synopsis

We describe in this project the first benchmark of large scale *evolving* FMs, consisting of 5 popular Feature Models (FMs) i.e., Linux Kernel, FreeBSD, eCos, Fiasco and uClinux and their evolutions – synthetically generated following an experimental study of FM evolution.

## Keywords

Benchmark, Software Product Lines, Feature Selection, Evolution

## Motivation

When software architects or engineers are given a list of all the features and their interactions (i.e., a Feature Model or FM) together with stakeholders’ preferences – their task is to find a set of potential products to suggest the decision makers. 

Software Product Lines Engineering (SPLE) consists in optimising those large and highly constrained search spaces according to multiple objectives reflecting the preference of the different stakeholders. SPLE is known to be extremely skill- and labour-intensive and it has been a popular topic of research in the past years.

While *change* and *evolution* of software systems is the common case in the industry, to the best of our knowledge this element has been overlooked in the literature. This project presents the first thorough benchmark related to the problem of evolving software product lines. 

## Publication

This project contains the material described and used in the paper:

* Takfarinas Saber, David Brevet, Goetz Botterweck and Anthony Ventresque. 2017. “[Is Seeding a Good Strategy in Multi-objective Features Selection When Feature Models Evolve?](http://www.sciencedirect.com/science/article/pii/S0950584916304724)” Information and Software Technology.
* David Brevet, Takfarinas Saber, Goetz Botterweck and Anthony Ventresque. 2016. “[Preliminary Study of Multi-objective Features Selection for Evolving Software Product Lines](https://link.springer.com/chapter/10.1007/978-3-319-47106-8_23)”. International Symposium on Search Based Software Engineering 274–280. 


## Content

* paper: the forlder which contains the published paper.
* datasetsGenerator: contains the java source code for the generation of evolved feature model from a given original on and a distance target (i.e., percentage of modified features and percentrage of modified constraints). See the README inside the folder for more details.
* dataset: the original and evolved datasets used in the paper. See the README file inside the folder for more details.
* Additional: the state-of-the-art algorithm *SATIBEA* is [publicly available here](http://research.henard.net/SPL/ICSE_2015/).







## Contributors

**Takfarinas Saber:** 
Lero@UCD, School of Computer Science, University College Dublin, Dublin 4, Ireland
*takfarinas.saber@ucdconnect.ie*

**David Brevet:** Institut Supérieur d’Informatique, de Modelisation et de leurs Applications, Clermont-Ferrand, France
*david.brevet@isima.fr*

**Goetz Botterweck:** Lero@UL, University of Limerick, Ireland 
*goetz.botterweck@lero.ie*

**Anthony Ventresque** Lero@UCD, School of Computer Science, University College Dublin, Dublin 4, Ireland
*anthony.ventresque@ucd.ie*


## Cite (BibTeX)
```
@article{saber2017seeding,
  title={Is seeding a good strategy in multi-objective feature selection when feature models evolve?},
  author={Saber, Takfarinas and Brevet, David and Botterweck, Goetz and Ventresque, Anthony},
  journal={Information and Software Technology},
  year={2017},
  publisher={Elsevier}
}

@inproceedings{brevet2016preliminary,
  title={Preliminary Study of Multi-objective Features Selection for Evolving Software Product Lines},
  author={Brevet, David and Saber, Takfarinas and Botterweck, Goetz and Ventresque, Anthony},
  booktitle={International Symposium on Search Based Software Engineering},
  pages={274--280},
  year={2016},
  organization={Springer}
}

```

## License

The project is licensed under the LGPL license.

