JAVAC := javac
JAVA := java
SRC_DIR := src
BUILD_DIR := build
LIB_DIR := libs
MAIN := s3.base.Main

SOURCES := $(shell find $(SRC_DIR) -name '*.java')
CLASSPATH := $(BUILD_DIR):$(LIB_DIR)/*

.PHONY: all compile run clean

all: compile

compile:
	@mkdir -p $(BUILD_DIR)
	$(JAVAC) -cp "$(LIB_DIR)/*" -d $(BUILD_DIR) $(SOURCES)

run: compile
	$(JAVA) -cp "$(CLASSPATH)" $(MAIN)


archers: compile
	$(JAVA) -cp "$(CLASSPATH)" $(MAIN) \
		-p "1|player1|ai-archers-rush" \
		-p "1|player2|ai-archers-rush"

footmen: compile
	$(JAVA) -cp "$(CLASSPATH)" $(MAIN) \
		-p "1|player1|ai-footmen-rush" \
		-p "1|player2|ai-footmen-rush"

ai: compile
	$(JAVA) -cp "$(CLASSPATH)" $(MAIN) \
		-p "1|player1|ai-footmen-rush" \
		-p "1|player2|ai-archers-rush"


clean:
	rm -rf $(BUILD_DIR)
