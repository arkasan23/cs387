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

clean:
	rm -rf $(BUILD_DIR)
