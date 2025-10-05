# ==============================
# Configuration
# ==============================
SRC_DIR := src
BIN_DIR := bin
MAIN_CLASS := bci.app.App
JAR := po-uilib.jar
OUTPUT_JAR := proj.jar
MANIFEST := manifest.mf
TEST_DIR := tests
TEST_SCRIPT := run-tests.sh

# Find all .java sources
SOURCES := $(shell find $(SRC_DIR) -name "*.java")

# ==============================
# Default Target
# ==============================
.PHONY: all
all: compile

# ==============================
# Compilation (optimized)
# ==============================
.PHONY: compile
compile: $(BIN_DIR)/.compiled

$(BIN_DIR)/.compiled: $(SOURCES)
	@mkdir -p $(BIN_DIR)
	@javac -cp $(JAR):$(BIN_DIR) -d $(BIN_DIR) $(SOURCES)
	@touch $@
	@echo "✅ Compilation finished."

# ==============================
# Build JAR (with manifest)
# ==============================
.PHONY: build-jar
build-jar: compile
	@echo "→ Building JAR with manifest..."
	@echo "Manifest-Version: 1.0" > $(MANIFEST)
	@echo "Main-Class: $(MAIN_CLASS)" >> $(MANIFEST)
	@echo "" >> $(MANIFEST)
	@rm -f $(OUTPUT_JAR)
	@cd $(BIN_DIR) && jar cfm ../$(OUTPUT_JAR) ../$(MANIFEST) .
	@rm -f $(MANIFEST)
	@echo "✅ JAR created: $(OUTPUT_JAR)"

# ==============================
# Run Main Program
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
	@rm -rf $(BIN_DIR)
	@rm -f $(OUTPUT_JAR)
	@rm -f $(TEST_DIR)/*.{outhyp,diff} saved*
	@echo "✅ Clean complete."
