JAVAC = javac
JAVA = java

SRC = src
OUT = out

SOURCES := $(shell find $(SRC) -name "*.java")

all: compile

compile:
	@mkdir -p $(OUT)
	$(JAVAC) -d $(OUT) $(SOURCES)

keyboard: compile
	$(JAVA) -cp $(OUT) test.KeyboardExample

arrive: compile
	$(JAVA) -cp $(OUT) test.ArriveScenario

seek: compile
	$(JAVA) -cp $(OUT) test.SeekScenario

run: compile
ifndef CLASS
	$(error Usage: make run CLASS=test.KeyboardExample)
endif
	$(JAVA) -cp $(OUT) $(CLASS)

clean:
	rm -rf $(OUT)

.PHONY: all compile keyboard arrive seek run clean
