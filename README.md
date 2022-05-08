# <img src="https://raw.githubusercontent.com/IUTInfoAix-M2105/Syllabus/master/assets/logo.png" alt="class logo" class="logo"/> Développement d'application avec IHM

## IUT d’Aix-Marseille – Département Informatique Aix-en-Provence

- **Ressource :** [R2.02](https://cache.media.enseignementsup-recherche.gouv.fr/file/SPE4-MESRI-17-6-2021/35/5/Annexe_17_INFO_BUT_annee_1_1411355.pdf)
- **Responsables :**
  - [Sébastien Nedjar](mailto:sebastien.nedjar@univ-amu.fr)
- **Enseignants :**
  - [Cyril Pain-Barre](mailto:cyril.pain-barre@univ-amu.fr)
  - [Sophie Nabitz](mailto:sophie.nabitz@univ-avignon.fr)
- **Besoin d'aide ?**
  - Consulter et/ou créer des [issues](https://github.com/IUTInfoAix-R203/tp1-git/issues).
  - [Email](mailto:sebastien.nedjar@univ-amu.fr) pour une question d'ordre privée, ou pour convenir d'un rendez-vous physique.

## Tutoriel de découverte de JPA

L’objectif de ce tutoriel est de vous présenter rapidement JPA 3.1 et de vous le faire tester sur un projet extrêmement simple. Pour rappel, Jakarta Persistence API est une spécification standard de Java permettant aux développeurs de gérer et manipuler des données relationnelles dans leurs applications. Cette API, bien qu’originaire du monde Jakarta EE (Jakarta Entreprise Edition), peut être utilisée aussi bien dans une application Java SE (Java Standard Edition) que dans un conteneur d’application Jakarta EE.

JPA étant uniquement une spécification, il peut en exister plusieurs implémentations différentes. Si le code d’un projet se conforme parfaitement à la spécification, il est possible de passer d’une implémentation à une autre sans trop de difficulté. Comme Maven, JPA utilise la philosophie "Convention plutôt que configuration". L’idée est de faire décroître le nombre de décisions qu’un développeur doit prendre en lui proposant une convention adaptée au cas d’utilisation le plus classique qu’il pourra amender pour correspondre à ce qu’il veut faire précisément. Ainsi la configuration n’est plus la norme, mais l’exception.

Pour effectuer la configuration de la persistance, JPA utilise soit le mécanisme des annotations soit un fichier XML. Nous n’étudierons que les annotations, car elles sont plus simples à mettre en œuvre et à maintenir en cohérence avec le code. Les annotations sont des méta-informations qui sont rajoutées aux classes métiers pour indiquer à JPA le travail qu’il doit faire pour les rendre persistantes. Le code de ces classes n’étant pas modifié, chacune d’elles reste un POJO ("Plain Old Java Object" que l’on pourrait traduire par "Bons Vieux Objets Java") que l’on pourra facilement tester (voir le tutoriel sur JUnit) comme n’importe quel POJO.

## Mise en place de l’environnement de travail

Pour simplifier au maximum l’utilisation de JPA 3.1 dans ce tutoriel, Maven sera utilisé pour la construction et la gestion des dépendances. Il est donc requis de faire le tutoriel Maven avant d’attaquer celui-ci.

### Installation

Voici la liste des outils qui seront utilisés pour la suite de ce tutoriel :

- Maven : Outil de gestion du cycle de vie d’un projet de développement logiciel.

- JPA 3.1 : Plus besoin de le présenter.

- EclipseLink : Framework open source et implémentation de référence de JPA.

- Derby : Apache Derby est un système de gestion de base de données relationnelle qui peut être embarqué dans un programme Java. Sa faible empreinte mémoire (moins de 2Mo) lui permet d’être utilisé dans un grand nombre de contextes (test unitaire dans ce tutoriel).

- Postgres : Un SGBD-R open source.

- JUnit : Framework de test unitaire java.

- DbUnit : Extension de JUnit pour les applications orientées BD.

Mis à par Maven et Postgres, tous les autres outils seront installés automatiquement grâce au système de gestion de dépendances de Maven.

### Création du projet

Pour commencer, nous allons créer le projet de test que l’on nommera `tutojpa` et qui sera dans le package `fr.univ_amu.iut`. Pour se faire on utilise la commande Maven suivante :

```sh
mvn archetype:generate -DinteractiveMode=false -DarchetypeArtifactId=maven-archetype-quickstart -DgroupId=fr.univ_amu.iut.tutojpa -DartifactId=tutojpa
```

Une fois cette commande exécutée, il faut modifier le fichier `pom.xml` pour lui rajouter les dépendances nécessaires à un projet JPA 3.1 :

```XML
<project xmlns="https://maven.apache.org/POM/4.0.0" 
         xmlns:xsi="https://www.w3.org/2001/XMLSchema-instance"
           xsi:schemaLocation="https://maven.apache.org/POM/4.0.0 
                               https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    
    <groupId>fr.univ_amu.iut</groupId>
    <artifactId>tutojpa</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <packaging>jar</packaging>
    <name>tutojpa</name>
    
    <url>http://maven.apache.org</url>
    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
        <junit.jupiter.version>5.4.2</junit.jupiter.version>
        <eclipselink.version>4.0.0-M3</eclipselink.version>
        <jakarta.persistence.version>3.1.0</jakarta.persistence.version>
    </properties>
    
    <dependencies>
        <dependency>
          <groupId>org.junit.jupiter</groupId>
          <artifactId>junit-jupiter-api</artifactId>
          <version>${junit.jupiter.version}</version>
          <scope>test</scope>
        </dependency>
  
        <dependency>
          <groupId>org.junit.jupiter</groupId>
          <artifactId>junit-jupiter-engine</artifactId>
          <version>${junit.jupiter.version}</version>
          <scope>test</scope>
        </dependency>
      
        <dependency>
          <groupId>jakarta.persistence</groupId>
          <artifactId>jakarta.persistence-api</artifactId>
          <version>${jakarta.persistence.version}</version>
        </dependency>
  
        <dependency>
          <groupId>org.eclipse.persistence</groupId>
          <artifactId>org.eclipse.persistence.jpa</artifactId>
          <version>${eclipselink.version}</version>
        </dependency>
  
        <dependency>
          <groupId>org.eclipse.persistence</groupId>
          <artifactId>eclipselink</artifactId>
          <version>${eclipselink.version}</version>
        </dependency>
    </dependencies>
</project>
```

D’autres dépendances seront ajoutées au fur et à mesure de leur nécessité.

### Création des classes métiers

L’exemple utilisé dans ce paragraphe est similaire à celui présenté dans le cours. Il modélise les employés d’une entreprise et les départements auxquels ils appartiennent. Il y aura donc 2 entités : `Employe` et `Departement`. L’adresse d’un employé sera modélisée par une classe intégrée. Dans le modèle de persistance JPA 3.1, un *entity bean* est une classe java (un Pojo) complétée par de simples annotations :

```Java
package fr.univ_amu.iut.tutojpa;
import jakarta.persistence.*;

@Entity //1
public class Employe {
    @Id //2
    @GeneratedValue//3
    private int id;
    @Column(length=50) //4
    private String nom;
    private long salaire;
    @Embedded //5
    private Adresse adresse;
    @ManyToOne //6
    private Departement departement;
    
    public Employe() {}
    public Employe(int id) { this.id = id; }
    public int getId() { return id; }
    // private void setId(int id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public long getSalaire() { return salaire; }
    public void setSalaire(long salaire) { this.salaire =salaire; }
    public Adresse getAdresse() { return adresse; }
    public void setAdresse(Adresse adresse) { this.adresse = adresse; }
    public Departement getDepartement() { return departement; }
    public void setDepartement(Departement departement) { 
        this.departement = departement; 
    }
}
```

Notez la présence d’annotations à plusieurs endroits dans la classe `Employe` :

1. Tout d’abord, l’annotation `@jakarta.persistence.Entity` permet à JPA de reconnaître cette classe comme une classe persistante (une entité) et non comme une simple classe Java.

2. L’annotation `@jakarta.persistence.Id`, quant à elle, définit l’identifiant unique de l’objet. Elle donne à l’entité une identité en mémoire en tant qu’objet, et en base de données via une clé primaire. Les autres attributs seront rendus persistants par JPA en appliquant la convention suivante : le nom de la colonne est identique à celui de l’attribut et le type String est converti en `VARCHAR(255)`.

3. L’annotation `@jakarta.persistence.GeneratedValue` indique à JPA qu’il doit gérer automatiquement la génération automatique de la clef primaire.

4. L’annotation `@jakarta.persistence.Column` permet de préciser des informations sur une colonne de la table : changer son nom (qui par défaut porte le même nom que l’attribut), préciser son type, sa taille et si la colonne autorise ou non la valeur null.

5. L’annotation `@jakarta.persistence.Embedded` précise que la donnée membre devra être intégrée dans l’entité.

6. L’annotation `@jakarta.persistence.ManyToOne` indique à JPA que la donnée membre est une association N:1.

La classe `Departement` est, elle aussi, transformée en entité :

```Java
package fr.univ_amu.iut.tutojpa;
import jakarta.persistence.*;

@Entity
public class Departement {
    @Id
    @GeneratedValue
    private long id;
    private String nom;
    private String telephone;
    public Departement() {}
    public Departement(long id, String nom, String telephone) {
        this.id = id;
        this.nom = nom;
        this.telephone = telephone;
    }
    
    public long getId() { return id; }
    public String getNom() { return nom; }
    public String getTelephone() { return telephone; }
}
```

La classe `Adresse` doit être annotée par l’annotation `@jakarta.persistence.Embeddable` pour pouvoir être intégrée dans la classe `Employe` :

```Java
package fr.univ_amu.iut.tutojpa;
import jakarta.persistence.*;

@Embeddable
public class Adresse {
    private int numero;
    private String rue;
    private String codePostal;
    private String ville;
        
    public Adresse() {}
    public Adresse(int numero, String rue, String codePostal, String ville) {
        this.numero = numero;
        this.rue = rue;
        this.codePostal = codePostal;
        this.ville = ville;
    }
    public int getNumero() { return numero; }
    public String getRue() { return rue; }
    public String getCodePostal() { return codePostal; }
    public String getVille() { return ville; }
}
```

### Contexte de persistance
Dans l'exemple suivant utilise le serveur MySQL local à l'image Gitpod. Si vous avez ouvert ce dépôt localement, utilisez plutôt les paramètres de votre connexion PostgresSQL de chez [Elephant SQL](https://www.elephantsql.com/plans.html). Pour Postgres, le nom complet de la classe pilote est `org.postgresql.Driver`.

Les paramètres de la connexion à la base de données sont définis dans le fichier `persistence.xml`. Ce fichier doit être situé dans le dossier `META-INF` du `jar` de l’application. Ces paramètres seront utilisés par la suite par le gestionnaire d’entités pour établir la connexion au SGBD.

Pour que Maven place ce fichier au bon endroit à la construction du `jar`, il le faut mettre dans le dossier `src/main/resources/META-INF`.

```XML
<?xml version="1.0" encoding="UTF-8" ?>

<persistence xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
 xsi:schemaLocation="http://java.sun.com/xml/ns/persistence/persistence_2_0.xsd" 
 version="2.0" xmlns="http://java.sun.com/xml/ns/persistence">
  <persistence-unit name="employePU" transaction-type="RESOURCE_LOCAL">
    <provider>org.eclipse.persistence.jpa.PersistenceProvider</provider>
    <class>fr.univ_amu.iut.tutojpa.Employe</class>
    <class>fr.univ_amu.iut.tutojpa.Departement</class>
    <class>fr.univ_amu.iut.tutojpa.Adresse</class>
    <properties>
      <property name="jakarta.persistence.jdbc.driver" value="com.mysql.jdbc.Driver"/>
      <property name="jakarta.persistence.jdbc.url" value="jdbc:mysql://localhost:3306/employeBD"/>
      <property name="jakarta.persistence.jdbc.user"  value="monUser"/>
      <property name="jakarta.persistence.jdbc.password"  value="monPassword"/>
      <property name="eclipselink.logging.level" value="FINE" />
      <property name="eclipselink.ddl-generation"  value="create-tables"/>
    </properties>
  </persistence-unit>
</persistence>
```

D’après le fichier `persistence.xml` l’application se connectera à la base `employeBD` d'un serveur MySQL local avec l’utilisateur `"monUser"` et le mot de passe `"monPassword"`. Pour paramétrer correctement ce serveur local, il faut exécuter les commandes suivantes :

```sh
mysql --user=root --password=mysql --execute="create database employeBD"
mysql --user=root --password=mysql --execute="grant all privileges on employeBD.* to monUser@localhost identified by 'monPassword'"
mysql --user=root --password=mysql --execute="show databases"
+--------------------+
| Database           |
+--------------------+
| information_schema |
| employeBD          |
| mysql              |
+--------------------+

mysql --user=monUser --password=monPassword
Welcome to the MySQL monitor.  Commands end with ; or \g.
Your MySQL connection id is 43
Server version: 5.1.58

Copyright (c) 2000, 2010, Oracle and/or its affiliates. All rights reserved.
This software comes with ABSOLUTELY NO WARRANTY. This is free software,
and you are welcome to modify and redistribute it under the GPL v2 license

Type 'help;' or '\h' for help. Type '\c' to clear the current input statement.

mysql> use employeBD;
Database changed
mysql> show tables;
Empty set (0.00 sec)
```

Comme on peut le voir pour l’instant notre base de données est totalement vide.

### Programme principal

Maintenant que l’entité `Employe` est développée et compilée, nous allons écrire une classe principale qui permettra de créer un objet `Employe` et de le rendre persistant. Pour cela, nous avons besoin d’initialiser le `EntityManager` par une factory, de démarrer une transaction, créer une instance de l’objet, définir le nom et le salaire de l’employé, utilisez `EntityManager.persist()` pour l’insérer dans la base de données, valider la transaction et fermer l’`EntityManager`.

```Java
package fr.univ_amu.iut.tutojpa;
import jakarta.persistence.*;

public class App 
{
    public static void main(String[] args) {
        // Initializes the Entity manager
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("employePU");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        // Creates a new object and persists it
        Employe employe = new Employe();
        employe.setNom("Dupont");
        employe.setSalaire(5000);
        tx.begin();
        em.persist(employe);
        tx.commit();

        em.close();
        emf.close();
    }
}
```

Avant de lancer ce programme, il faut ajouter dans le fichier `pom.xml` les dépendances au connecteur MySQL, Postgres et Derby. Pour ajouter ces dépendances, il faut rajouter les lignes suivantes à l'interieur de la balise `dependencies`:

```XML
<dependency>
  <groupId>mysql</groupId>
  <artifactId>mysql-connector-java</artifactId>
  <version>8.0.29</version>
</dependency>

<dependency>
  <groupId>org.postgresql</groupId>
  <artifactId>postgresql</artifactId>
  <version>42.3.5</version>
</dependency>

<dependency>
  <groupId>org.apache.derby</groupId>
  <artifactId>derby</artifactId>
  <version>${derby.version}</version>
  <scope>test</scope>
</dependency>

<dependency>
  <groupId>org.apache.derby</groupId>
  <artifactId>derbyclient</artifactId>
  <version>${derby.version}</version>
  <scope>test</scope>
</dependency>
```

Maintenant nous allons utiliser la commande Maven `clean compile` pour compiler notre projet et le plugin `exec` pour lancer la classe principale.

```sh
mvn clean compile
mvn exec:java -Dexec.mainClass="fr.univ_amu.iut.tutojpa.TestJPA"
```

Lorsque nous lançons la classe `fr.univ_amu.iut.tutojpa.TestJPA` plusieurs choses vont se produire :

- Comme la propriété `eclipselink.ddl-generation` est initialisée à `create-tables` dans le fichier `persistence.xml`, les différentes tables sont créées si nécessaire.

- L’employé "Dupont" est inséré dans la base de données (avec un identifiant automatiquement généré).

Regardons l’état de la base de données pour comprendre ce qui s’est passé.

```sh
mysql --user=monUser --password=monPassword employeBD
Reading table information for completion of table and column names
You can turn off this feature to get a quicker startup with -A

Welcome to the MySQL monitor.  Commands end with ; or \g.
Your MySQL connection id is 54
Server version: 5.1.58-1ubuntu1 (Ubuntu)

Copyright (c) 2000, 2010, Oracle and/or its affiliates. All rights reserved.
This software comes with ABSOLUTELY NO WARRANTY. This is free software,
and you are welcome to modify and redistribute it under the GPL v2 license

Type 'help;' or '\h' for help. Type '\c' to clear the current input statement.

mysql> show tables;
+---------------------+
| Tables_in_employeBD |
+---------------------+
| DEPARTEMENT         |
| EMPLOYE             |
| SEQUENCE            |
+---------------------+
3 rows in set (0.00 sec)

mysql> describe EMPLOYE;
+----------------+--------------+------+-----+---------+-------+
| Field          | Type         | Null | Key | Default | Extra |
+----------------+--------------+------+-----+---------+-------+
| ID             | bigint(20)   | NO   | PRI | NULL    |       |
| NOM            | varchar(50)  | YES  |     | NULL    |       |
| SALAIRE        | bigint(20)   | YES  |     | NULL    |       |
| CODEPOSTAL     | varchar(255) | YES  |     | NULL    |       |
| NUMERO         | int(11)      | YES  |     | NULL    |       |
| RUE            | varchar(255) | YES  |     | NULL    |       |
| VILLE          | varchar(255) | YES  |     | NULL    |       |
| DEPARTEMENT_ID | bigint(20)   | YES  | MUL | NULL    |       |
+----------------+--------------+------+-----+---------+-------+
8 rows in set (0.00 sec)

mysql> select * from EMPLOYE;
+-----+--------+---------+------------+--------+------+-------+----------------+
| ID  | NOM    | SALAIRE | CODEPOSTAL | NUMERO | RUE  | VILLE | DEPARTEMENT_ID |
+-----+--------+---------+------------+--------+------+-------+----------------+
|   1 | Dupont |    5000 | NULL       |   NULL | NULL | NULL  |           NULL |
+-----+--------+---------+------------+--------+------+-------+----------------+
1 rows in set (0.00 sec)
```

JPA associe une relation à chaque classe marquée par l’annotation `@Entity` et les données membres sont converties en attribut. Il gère aussi les associations N:1 en créant la clef étrangère dont le nom est construit à partir du nom de la clef et de la table liée. La classe `Adresse` est directement intégrée dans la table de l’entité `Employe`. Pour les clefs autogénérées, JPA utilise une table nommée `SEQUENCE` pour mémoriser les identifiants déjà attribués.
