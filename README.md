# <img src="https://raw.githubusercontent.com/IUTInfoAix-M2105/Syllabus/master/assets/logo.png" alt="class logo" class="logo"/> Développement d'application avec IHM

## IUT d’Aix-Marseille – Département Informatique Aix-en-Provence

- **Ressource :** [R2.02](https://cache.media.enseignementsup-recherche.gouv.fr/file/SPE4-MESRI-17-6-2021/35/5/Annexe_17_INFO_BUT_annee_1_1411355.pdf)
- **Responsables :**
  - [Sébastien Nedjar](mailto:sebastien.nedjar@univ-amu.fr)
- **Enseignants :**
  - [Cyril Pain-Barre](mailto:cyril.pain-barre@univ-amu.fr)
  - [Sophie Nabitz](mailto:sophie.nabitz@univ-avignon.fr)
- **Besoin d'aide ?**
  - Consulter et/ou créer des [issues](https://github.com/IUTInfoAix-R202/cours/issues).
  - [Email](mailto:sebastien.nedjar@univ-amu.fr) pour une question d'ordre privée, ou pour convenir d'un rendez-vous physique.

## Tutoriel de découverte de JPA

L’objectif de ce tutoriel est de vous présenter rapidement JPA 3.1 et de vous le faire tester sur un projet extrêmement simple. Pour rappel, Jakarta Persistence API est une spécification standard de Java permettant aux développeurs de gérer et manipuler des données relationnelles dans leurs applications. Cette API, bien qu’originaire du monde Jakarta EE (Jakarta Entreprise Edition), peut être utilisée aussi bien dans une application Java SE (Java Standard Edition) que dans un conteneur d’application Jakarta EE.

JPA étant uniquement une spécification, il peut en exister plusieurs implémentations différentes. Si le code d’un projet se conforme parfaitement à la spécification, il est possible de passer d’une implémentation à une autre sans trop de difficulté. Comme Maven, JPA utilise la philosophie "Convention plutôt que configuration". L’idée est de faire décroître le nombre de décisions qu’un développeur doit prendre en lui proposant une convention adaptée au cas d’utilisation le plus classique qu’il pourra amender pour correspondre à ce qu’il veut faire précisément. Ainsi la configuration n’est plus la norme, mais l’exception.

Pour effectuer la configuration de la persistance, JPA utilise soit le mécanisme des annotations soit un fichier XML. Nous n’étudierons que les annotations, car elles sont plus simples à mettre en œuvre et à maintenir en cohérence avec le code. Les annotations sont des méta-informations qui sont rajoutées aux classes métiers pour indiquer à JPA le travail qu’il doit faire pour les rendre persistantes. Le code de ces classes n’étant pas modifié, chacune d’elles reste un POJO ("Plain Old Java Object" que l’on pourrait traduire par "Bons Vieux Objets Java") que l’on pourra facilement tester (voir le tutoriel sur JUnit) comme n’importe quel POJO.

## Mise en place de l’environnement de travail

Pour simplifier au maximum l’utilisation de JPA 3.1 dans ce tutoriel, Maven sera utilisé pour la construction et la gestion des dépendances. 

### Installation

Voici la liste des outils qui seront utilisés pour la suite de ce tutoriel :

- Maven : Outil de gestion du cycle de vie d’un projet de développement logiciel.

- JPA 3.1 : Plus besoin de le présenter.

- EclipseLink : Framework open source et implémentation de référence de JPA.

- Derby : Apache Derby est un système de gestion de base de données relationnelle qui peut être embarqué dans un programme Java. Sa faible empreinte mémoire (moins de 2Mo) lui permet d’être utilisé dans un grand nombre de contextes (test unitaire dans ce tutoriel).

- Postgres : Un SGBD-R open source.

- MySQL : Un autre SGBD-R open source.

- JUnit : Framework de test unitaire java.

Mis à par Maven, le JDK et les SGBD-R, tous les autres outils seront installés automatiquement grâce au système de gestion de dépendances de Maven.

### Création des classes métiers

L’exemple utilisé dans ce paragraphe est similaire à celui présenté dans le cours. Il modélise les employés d’une entreprise et les départements auxquels ils appartiennent. Il y aura donc 2 entités : `Employe` et `Departement`. L’adresse d’un employé sera modélisée par une classe intégrée. Dans le modèle de persistance JPA 3.1, un *entity bean* est une classe java (un Pojo) complétée par de simples annotations :

```Java
package fr.univ_amu.iut.tutojpa;

import jakarta.persistence.*;

@Entity
public class Employee {
  @Id
  @GeneratedValue
  private int id;
  private String name;
  private long salary;

  @Embedded
  private Adresse adresse;

  @ManyToOne(cascade = CascadeType.ALL)
  private Department department;

  public Adresse getAdresse() {
    return adresse;
  }

  public void setAdresse(Adresse adresse) {
    this.adresse = adresse;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public long getSalary() {
    return salary;
  }

  public void setSalary(long salary) {
    this.salary = salary;
  }

  public Department getDepartment() {
    return department;
  }

  public void setDepartment(Department department) {
    this.department = department;
  }

  public String toString() {
    return "Employee id: " + getId() + " name: " + getName() + " with " + getDepartment();
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

La classe `Department` est, elle aussi, transformée en entité :

```Java
package fr.univ_amu.iut.tutojpa;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Collection;

@Entity
public class Department {
  @Id
  @GeneratedValue
  private int id;
  private String name;
  @OneToMany(mappedBy="department")
  private Collection<Employee> employees;

  public Department() {
    employees = new ArrayList<>();
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String deptName) {
    this.name = deptName;
  }

  public void addEmployee(Employee employee) {
    if (!getEmployees().contains(employee)) {
      getEmployees().add(employee);
      if (employee.getDepartment() != null) {
        employee.getDepartment().getEmployees().remove(employee);
      }
      employee.setDepartment(this);
    }
  }

  public Collection<Employee> getEmployees() {
    return employees;
  }

  public String toString() {
    return "Department id: " + getId() +
            ", name: " + getName();
  }
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
Si vous avez ouvert ce dépôt localement, utilisez plutôt les paramètres de votre connexion PostgresSQL de chez [Elephant SQL](https://www.elephantsql.com/plans.html) ou une base de données MySQL sur votre espace [Pedaweb](https://dud.univ-amu.fr/votre-espace-web-pedagogique-amu) d'AMU. Que ce soit sur l'une ou l'autre des solutions, une fois votre instance créée, vous pourrez récupérer votre URL de connexion dans votre console d'administration.

Les paramètres de la connexion à la base de données sont définis dans le fichier `persistence.xml`. Ce fichier doit être situé dans le dossier `META-INF` du `jar` de l’application. Ces paramètres seront utilisés par la suite par le gestionnaire d’entités pour établir la connexion au SGBD.

Pour que Maven place ce fichier au bon endroit à la construction du `jar`, il le faut mettre dans le dossier `src/main/resources/META-INF`.

```XML
<?xml version="1.0" encoding="UTF-8" ?>

<persistence xmlns="https://jakarta.ee/xml/ns/persistence" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             version="3.0">
  <persistence-unit name="employePU" transaction-type="RESOURCE_LOCAL">
    <provider>org.eclipse.persistence.jpa.PersistenceProvider</provider>

    <class>fr.univ_amu.iut.tutojpa.Employee</class>
    <class>fr.univ_amu.iut.tutojpa.Department</class>
    <class>fr.univ_amu.iut.tutojpa.Adresse</class>

    <properties>
      <!-- database connection properties -->
      <property name="jakarta.persistence.jdbc.url" value="jdbc:derby:memory:employeBD;create=true"/>
      <property name="jakarta.persistence.jdbc.user" value=""/>
      <property name="jakarta.persistence.jdbc.password" value=""/>

      <property name="eclipselink.logging.level" value="INFO"/>
      <property name="eclipselink.logging.level.sql" value="FINE"/>
      <property name="eclipselink.logging.parameters" value="true"/>

      <property name="eclipselink.ddl-generation.output-mode" value="database"/>
      <property name="eclipselink.ddl-generation" value="create-tables"/>
    </properties>
  </persistence-unit>
</persistence>
```

D’après le fichier `persistence.xml` l’application se connectera à la base `employeBD` d'une base de données embarquée avec l’utilisateur `""` et le mot de passe `""`. 

Pour l’instant notre base de données est totalement vide.

### Programme principal

Maintenant que l’entité `Employe` est développée et compilée, nous allons écrire une classe principale qui permettra de créer un objet `Employe` et de le rendre persistant. Pour cela, nous avons besoin d’initialiser le `EntityManager` par une factory, de démarrer une transaction, créer une instance de l’objet, définir le nom et le salaire de l’employé, utilisez `EntityManager.persist()` pour l’insérer dans la base de données, valider la transaction et fermer l’`EntityManager`.

```Java
package fr.univ_amu.iut.tutojpa;

import jakarta.persistence.*;

public class TestJPA {
  public static void main(String[] args) {
    // Initializes the Entity manager
    try (EntityManagerFactory emf = Persistence.createEntityManagerFactory("employePU")) {
      try (EntityManager em = emf.createEntityManager()) {
        EntityTransaction tx = em.getTransaction();

        Department informatique = new Department();
        informatique.setName("Informatique");

        Employee dupont = new Employee();
        dupont.setName("Dupont");
        dupont.setDepartment(informatique);
        dupont.setSalary(5000);

        tx.begin();
        em.persist(dupont);
        tx.commit();
      }
    }
  }
}
```

Utiliser la commande Maven `clean compile` pour compiler le projet et le plugin `exec` pour lancer la classe principale.

```sh
mvn clean compile
mvn exec:java -Dexec.mainClass="fr.univ_amu.iut.tutojpa.TestJPA"
```

Lorsque nous lançons la classe `fr.univ_amu.iut.tutojpa.TestJPA` plusieurs choses vont se produire :

- Comme la propriété `eclipselink.ddl-generation` est initialisée à `create-tables` dans le fichier `persistence.xml`, les différentes tables sont créées si nécessaire.

- L’employé "Dupont" est inséré dans la base de données (avec un identifiant automatiquement généré).

Après l'exécution de votre programme, aller voir dans votre console SQL les différentes tables créées ainsi que leur contenu. Comme vous le verrez, en plus des relations associées à chaque entité, il y a aussi une séquence qui permet de gérer les clés autogénérées.

JPA associe une relation à chaque classe marquée par l’annotation `@Entity` et les données membres sont converties en attribut. Il gère aussi les associations N:1 en créant la clef étrangère dont le nom est construit à partir du nom de la clef et de la table liée. La classe `Adresse` est directement intégrée dans la table de l’entité `Employe`.

Maintenant que vous avez fait marcher cet exemple, vous pouvez passer au TP en prenant soin de conserver l'URL de connexion à la base de données.
