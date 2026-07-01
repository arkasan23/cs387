# Java compiler
JAVAC = javac
JAVA = java

# Directories
SRC = src
OUT = out

# Find all Java files
SOURCES := $(shell find $(SRC) -name "*.java")

# Compile everything
all: compile

compile:
	@mkdir -p $(OUT)
	$(JAVAC) -d $(OUT) $(SOURCES)

# Run KeyboardExample
keyboard: compile
	$(JAVA) -cp $(OUT) test.KeyboardExample

# Run ArriveScenario
arrive: compile
	$(JAVA) -cp $(OUT) test.ArriveScenario

# Run SeekScenario
seek: compile
	$(JAVA) -cp $(OUT) test.SeekScenario

# Generic runner
run: compile
ifndef CLASS
	$(error Usage: make run CLASS=test.KeyboardExample)
endif
	$(JAVA) -cp $(OUT) $(CLASS)

# Delete compiled classes
clean:
	rm -rf $(OUT)

.PHONY: all compile keyboard arrive seek run clean
