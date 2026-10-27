The factory works this way:

It receives a property object that represent the properties of the repositories that are about to be loaded, and it keeps those properties.

After that, to create a user repository, the program loads the mode of the repository(file,memory or database), and then it creates the
repository in the desired form, using the validator and the properties that the caller gave to the constructor.

If the mandatory properties for the creation of the repository aren't there, then the program will throw a ConfigException.

The creation of the friendship repository is similar, but the function also needs the user repository to potentially connect it to the
new friendship repository.

For changing to the database mode, you only need to change the repository.type parameter from the config file to DATABASE, and it will
start the application on database mode.


If one of the files used by the repositories doesn't exist, it is created and the repository will start with no entries in it, because
the file is empty.

If one of the files used by the repositories has a corrupt line, then the program will print the corrupt lines of the file in the console,
and the corrupt lines will not be loaded in the repository. T


SOLID Principles:

SRP(Single Responsibility Principle): The classes in the project all have a defined purpose, and only one reason to change. For example, the
main modeling classes(User and Friendship) only have the purpose to represent that concept, the repositories are separated so they have a single
responsibility(Repository to define what is a repository, MemoryRepository to define a repository in memory, etc.).

Also, the classes in service and UI are separated so they don't have more than one responsibility, and the factory is separated into
two classes exactly so that the Property generation is separated from the Repository generation, so that they have a single responsibility.

OCP(Open Closed Principle): All the layers in this application have been made so that they are as extensible as possible, with a lot of classes
that are extended, like the 3 layers of the repository that can be extended. Or, the domain objects which inherit everything from Entity, and
these abstract classes make adding features without modifying existing code pretty easy.

However, in the factory method, to add a new persistence type, you need to modify the code in the RepositoryType enum and the RepositoryFactory class
to add that new persistence type, but I kept this structure because actively modifying the retention type is easy, as you only have to change
the config file.

If we instead had created an interface for the factory and an implementation for each type of repository, the classes would be easier to extend
so that we create new persistence types, but the catch is that we still need to make a conditional clause to change the repository in another
place, which would still violate OCP. Still, the second method is better if we solely look at the OCP, but it also has the disadvantage that
it creates a lot of classes which still need switching.

LSP(Liskov Substitution Principle): As there is a lot of inheritance in this project, you can substitute in most cases a class with a subclass,
but the FileRepository class cannot be a substitute for superclasses, because that class is abstract. Otherwise, this principle is respected
everywhere in the project.

ISP(Interface Segregation Principle): In this project, the interfaces for every layer don't have any more functions than needed, and there
are very few interfaces anyway, so this principle is respected.

DIP(Dependency Inversion Principle): Everywhere in this project, I depend on interfaces so that this principle is respected,
though there are a few places where there are necessary dependencies on classes, not interfaces, like how the User repositories depend
on the User class, as that is a model for a user in my application and the whole purpose of the user repository is to handle Users.

Design patterns used in this project:

Template Method:
Used in FileRepository with the functions lineToEntity and entityToLine, which are defined in a subclass.

Strategy:
Used in FileRepository as the User and Friendship repositories are concrete strategies for FileRepository.
Used in MemoryRepository as FileRepository and the 2 concrete repositories are strategies for MemoryRepository.
Used in Repository as MemoryRepository and the 2 concrete database repositories are strategies for Repository.

Also, the strategy pattern can be implemented in the Service classes or other class structures where there currently would only
be a single strategy for the superclass.

Observer:
Used the Friendship repositories as observers and the User repositories as observables, so that the friendships can be modified when the user
list is changed, without too much coupling.

Simple Factory:
Used in PropertyFactory and in RepositoryFactory, to generate properties and repositories respectively.
