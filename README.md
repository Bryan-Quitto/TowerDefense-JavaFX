# Tower-Defense-JavaFX
Tower Defense Game made with JFX

Game Model/Logic:

1) Have classes, abstract classes, and interfaces. 
Alien is abstract and implements an interface. 
It has 2 subclasses, FastAlien and SlowAlien. 
Each of these implements an attack method and inherit methods from Alien. 
There is also a Base and Turret. 
The base and Alien has a health and the Turret and Aliens have attack.
2) There is a good framework with multiple classes.
3) There are good object-oriented principles
4) There are good uses of interfaces and abstract classes
5) It properly utilizes inheritance and polymorphism

Functionality:

1) You can place turrets
2) Enemies move across screen
3) Turrets shoot
4) Enemies deal damage to “home base”

Graphics:

1) Animation (enemies move)
2) multiple distinct elements (turrets, aliens, bombs)

MVC:

1) have models (enemy classes, turret classes),
2) controller (Game),

App Structure & Packaging:

1) makes use of at least two packages that make sense (characters & interfaces)
2) include an ANT buildfile that can be used to compile and run your code (ant ... compile, run, checkstyle, javadoc)

Javadoc & Checkstyle:

1) All code is thoroughly and meaningfully javadoc’d
2) All code passes checkstyle with zero errors.
