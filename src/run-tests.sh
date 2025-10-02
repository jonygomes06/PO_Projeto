#!/usr/bin/env bash

# ==============================
# Configuration
# ==============================
JAR="po-uilib.jar"
MAIN_CLASS="bci.app.App"
SRC_DIR="."        # Root of Java sources
BIN_DIR="."        # Where .class files will go
TEST_DIR="tests"

# CLI argument: show diffs if present
SHOW_DIFFS=false
if [[ "$1" == "--show-diffs" ]]; then
    SHOW_DIFFS=true
fi

# Colors
RED=$(tput setaf 1)
GREEN=$(tput setaf 2)
YELLOW=$(tput setaf 3)
CYAN=$(tput setaf 6)
BOLD=$(tput bold)
RESET=$(tput sgr0)

# ==============================
# Compile Java Sources
# ==============================
echo "${CYAN}${BOLD}→ Compiling Java sources...${RESET}"
find "$SRC_DIR" -name "*.java" | xargs javac -cp "$JAR:." -d "$BIN_DIR"
if [ $? -ne 0 ]; then
    echo "${RED}✗ Compilation failed. Fix errors and rerun.${RESET}"
    exit 1
fi
echo "${GREEN}✓ Compilation successful.${RESET}"
echo

# ==============================
# Run Tests
# ==============================
total=0
passed=0
failed_tests=()

echo "${CYAN}${BOLD}→ Running tests in '$TEST_DIR'...${RESET}"
echo

for input_file in "$TEST_DIR"/*.in; do
    test_name=$(basename "${input_file%.in}")

    import_file="${input_file%.in}.import"
    expected_file="${input_file%.in}.out"
    output_file="${input_file%.in}.outhyp"
    diff_file="${input_file%.in}.diff"

    # Run program
    if [ -e "$import_file" ]; then
        java -cp "$JAR:." -Dimport="$import_file" -Din="$input_file" -DwriteInput=true -Dout="$output_file" "$MAIN_CLASS" >/dev/null 2>&1
    else
        java -cp "$JAR:." -Din="$input_file" -DwriteInput=true -Dout="$output_file" "$MAIN_CLASS" >/dev/null 2>&1
    fi

    # Check output exists
    if [ ! -f "$output_file" ]; then
        echo -e "${YELLOW}⚠ $test_name: No output produced.${RESET}"
        failed_tests+=("$test_name (no output)")
        ((total++))
        continue
    fi

    # Compare outputs
    if ! diff -u "$expected_file" "$output_file" > "$diff_file"; then
        echo -e "${RED}✗ $test_name${RESET}"
        failed_tests+=("$test_name")
        if [ "$SHOW_DIFFS" = true ]; then
            echo "${YELLOW}--- Diff snippet ---${RESET}"
            head -n 15 "$diff_file" | sed \
                -e "s/^+/$(tput setaf 2)+/; s/^-/$(tput setaf 1)-/" \
                -e "s/^/    /"
            echo "${YELLOW}--------------------${RESET}"
        fi
    else
        echo -e "${GREEN}✓ $test_name${RESET}"
        ((passed++))
        rm -f "$diff_file" "$output_file"
    fi

    ((total++))
done

# Cleanup saved files
rm -f saved* 2>/dev/null

# ==============================
# Summary
# ==============================
echo
echo "${BOLD}==============================${RESET}"
echo "${BOLD}📊 TEST SUMMARY${RESET}"
echo "Total tests : $total"
echo "Passed      : ${GREEN}$passed${RESET}"
echo "Failed      : ${RED}$((total - passed))${RESET}"
pct=$(( total > 0 ? 100 * passed / total : 0 ))
echo "Success     : ${BOLD}$pct%${RESET}"
echo "=============================="

# ==============================
# Cleanup .class files
# ==============================
echo "${CYAN}${BOLD}→ Cleaning up .class files...${RESET}"
find "$BIN_DIR" -name "*.class" -delete
echo "${GREEN}✓ All .class files removed.${RESET}"
