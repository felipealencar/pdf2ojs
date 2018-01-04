# PDF2OJS

One Paragraph of project description goes here
PDF2OJS is a java tool for extract metadata and content from scientific
articles. This tool analyses the content of a PDF file and attempts to extract
information such as:

* Title of the article
* Journal information (title, etc.)
* Bibliographic information (volume, issue, page numbers, etc.)
* Authors and affiliations
* Keywords
* Abstract
* Bibliographic references

This tool was original design to extract from the Brazilian Computational
Society (SBC) template. As a future work, I would like to add the IEEE
template.

## Getting Started

This project was originaly created with the Netbeans IDE:

```
git clone https://github.com/gustavo978/pdf2ojs.git
Open Netbeans
click on Open Project
Start code :)
```

### Prerequisites

You need to install Netbeans with Java and Maven.

### Installing

You can build using the Netbeans API or using the following command:

```
cd pdf2ojs
mvn clean install
cd target
java -jar pdf2ojs-1.0-SNAPSHOT-jar-with-dependencies.jar
```

## Built With

* [NetBeans](https://netbeans.org/) - Java IDE
* [Maven](https://maven.apache.org/) - Dependency Managemen
* [CERMINE](http://cermine.ceon.pl/index.html) - Content ExtRactor and MINEr


* **Gustavo Araújo** - *Initial work*
  - [PDF2OJS](https://github.com/gustavo978)

