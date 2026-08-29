JAVAC := javac
JAVA := java
PYTHON := python3

SRC_DIR := src
BUILD_DIR := build
LIB_DIR := libs

MAIN := s3.base.Main

SEED ?= 387
MAP := maps/generate_map.xml

SOURCES := $(shell find $(SRC_DIR) -name '*.java')
CLASSPATH := $(BUILD_DIR):$(LIB_DIR)/*

.PHONY: all compile map run archers footmen ai clean

all: compile

compile:
	@mkdir -p $(BUILD_DIR)
	$(JAVAC) -cp "$(LIB_DIR)/*" -d $(BUILD_DIR) $(SOURCES)

map:
	$(PYTHON) generate_map.py --seed $(SEED) --output $(MAP)

run:  compile
	$(JAVA) -cp "$(CLASSPATH)" $(MAIN) -m $(MAP)

archers:  compile
	$(JAVA) -cp "$(CLASSPATH)" $(MAIN) -m $(MAP) \
		-p "1|player1|ai-archers-rush" \
		-p "1|player2|ai-archers-rush"

footmen:  compile
	$(JAVA) -cp "$(CLASSPATH)" $(MAIN) -m $(MAP) \
		-p "1|player1|ai-footmen-rush" \
		-p "1|player2|ai-footmen-rush"

ai:  compile
	$(JAVA) -cp "$(CLASSPATH)" $(MAIN) -m $(MAP) \
		-p "1|player1|ai-footmen-rush" \
		-p "1|player2|ai-archers-rush"

clean:
	rm -rf $(BUILD_DIR)
