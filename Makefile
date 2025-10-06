# ==============================
# Configuration
# ==============================
SRC_DIR := src
BIN_DIR := bin
PROJ_BIN := proj_bin
MAIN_CLASS := bci.app.App
JAR := po-uilib.jar
OUTPUT_JAR := proj.jar
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
	@rm -f $(OUTPUT_JAR)
	@rm -rf $(PROJ_BIN)
	@mkdir $(PROJ_BIN) && cp -r $(BIN_DIR)/bci $(PROJ_BIN)/
	@cd $(PROJ_BIN) && jar cf ../$(OUTPUT_JAR) .
	@rm -rf $(PROJ_BIN)
	@rm -rf $(BIN_DIR)
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
	@rm -rf $(PROJ_BIN)
	@rm -f $(TEST_DIR)/*.{outhyp,diff} saved*
	@rm -f $(SRC_DIR)/*.class
	@echo "✅ Clean complete."
