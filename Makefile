# ==============================
# Configuration
# ==============================
SRC_DIR     := src
BIN_DIR     := bin
PROJ_BIN    := proj_bin
MAIN_CLASS  := bci.app.App
OUTPUT_JAR  := proj.jar
TEST_DIR    := tests
TEST_SCRIPT := run-tests.sh

# Find all .java sources
SOURCES := $(shell find $(SRC_DIR) -name "*.java")
CLASSPATH := $(BIN_DIR)

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
	@mkdir -p "$(BIN_DIR)"
	@javac -cp "$(CLASSPATH)" -d "$(BIN_DIR)" $(SOURCES)
	@touch "$@"
	@echo "✅ Compilation finished."

# ==============================
# Build JAR (with manifest)
# ==============================
.PHONY: build-jar
build-jar:
	@echo "📦 Creating JAR..."
	@rm -f "$(OUTPUT_JAR)"
	@jar cf "$(OUTPUT_JAR)" -C "$(SRC_DIR)" .
	@echo "✅ JAR created: $(OUTPUT_JAR)"

# ==============================
# Run Main Program
# ==============================
.PHONY: run
run: compile
	@java -cp "$(CLASSPATH)" $(ARGS) "$(MAIN_CLASS)"

# ==============================
# Run Tests
# ==============================
.PHONY: test
test: compile
	@chmod +x "$(TEST_SCRIPT)"
	@"./$(TEST_SCRIPT)"

# ==============================
# Clean build artifacts
# ==============================
.PHONY: clean
clean:
	@echo "🧹 Cleaning up..."
	@rm -rf "$(BIN_DIR)" "$(PROJ_BIN)" "$(OUTPUT_JAR)"
	@find "$(TEST_DIR)" -type f \( -name "*.outhyp" -o -name "*.diff" \) -delete
	@rm -f saved* "$(SRC_DIR)"/*.class
	@rm -f cumpridor faltoso requisicao
	@echo "✅ Clean complete."
