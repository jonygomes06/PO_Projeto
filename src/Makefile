# ==============================
# Configuration
# ==============================
SRC_DIR := .
BIN_DIR := .
MAIN_CLASS := bci.app.App
JAR := po-uilib.jar
TEST_DIR := testsBoth
TEST_SCRIPT := run-tests.sh

# Find all .java files and map to .class files
SOURCES := $(shell find $(SRC_DIR) -name "*.java")
CLASSES := $(SOURCES:.java=.class)

# ==============================
# Default Target
# ==============================
.PHONY: all
all: compile

# ==============================
# Compilation
# ==============================
.PHONY: compile
compile: $(CLASSES)
	@echo "✅ Compilation finished."

%.class: %.java
	@javac -cp $(JAR):$(SRC_DIR) -d $(BIN_DIR) $<
	@echo "Compiled $<"

# ==============================
# Run Main Program
# Usage: make run ARGS="..."
# ==============================
.PHONY: run
run: compile
	@java -cp $(JAR):$(BIN_DIR) $(MAIN_CLASS) $(ARGS)

# ==============================
# Run Tests
# ==============================
.PHONY: test
test: compile
	@chmod +x $(TEST_SCRIPT)
	@./$(TEST_SCRIPT)

# ==============================
# Clean build artifacts
# ==============================
.PHONY: clean
clean:
	@echo "🧹 Cleaning up..."
	@find $(BIN_DIR) -name "*.class" -delete
	@find $(TEST_DIR) -name "*.outhyp" -delete
	@find $(TEST_DIR) -name "*.diff" -delete
	@rm -f saved*
	@echo "✅ Clean complete."
