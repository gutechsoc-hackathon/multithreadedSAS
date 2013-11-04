COMP = javac
FLAGS = -g
Kosaraju.class: Kosaraju.java Node.java
	$(COMP) $(FLAGS) Kosaraju.java Node.java

