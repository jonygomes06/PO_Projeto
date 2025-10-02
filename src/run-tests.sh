#!/usr/bin/env bash

# ==============================
#  Configuration
# ==============================
JAR="po-uilib.jar"
MAIN_CLASS="bci.app.App"
TEST_DIR="tests"
SRC_DIR="."   # Root directory for Java sources
CLASS_DIR="." # Output directory for compiled classes

# ==============================
#  Colors and Formatting
# ==============================
RED=$(tput setaf 1)
GREEN=$(tput setaf 2)
YELLOW=$(tput setaf 3)
CYAN=$(tput setaf 6)
BOLD=$(tput bold)
RESET=$(tput sgr0)

# ==============================
#  Compile if needed
# ==============================
echo "${CYAN}${BOLD}→ Compiling Java sources...${RESET}"
find "$SRC_DIR" -name "*.java" | xargs javac -cp "$JAR:." -d "$CLASS_DIR"
if [ $? -ne 0 ]; then
    echo "${RED}✗ Compilation failed. Fix errors and rerun.${RESET}"
    exit 1
fi
echo "${GREEN}✓ Compilation successful.${RESET}"
echo

# ==============================
#  Run Tests
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

    # Check if output was generated
    if [ ! -f "$output_file" ]; then
        echo -e "${YELLOW}⚠ Test $test_name: Program did not produce output.${RESET}"
        failed_tests+=("$test_name (no output)")
        ((total++))
        continue
    fi

    # Compare with expected output
    diff -cwB "$expected_file" "$output_file" > "$diff_file"
    if [ -s "$diff_file" ]; then
        echo -e "${RED}✗ $test_name${RESET}"
        failed_tests+=("$test_name")
    else
        echo -e "${GREEN}✓ $test_name${RESET}"
        ((passed++))
        rm -f "$diff_file" "$output_file"
    fi

    ((total++))
done

# Cleanup leftover files
rm -f saved* 2>/dev/null

# ==============================
#  Summary
# ==============================
echo
echo "${BOLD}==============================${RESET}"
echo "${BOLD}📊 TEST SUMMARY${RESET}"
echo "Total tests : $total"
echo "Passed      : ${GREEN}$passed${RESET}"
echo "Failed      : ${RED}$((total - passed))${RESET}"
if [ $total -gt 0 ]; then
    pct=$(( 100 * passed / total ))
else
    pct=0
fi
echo "Success     : ${BOLD}$pct%${RESET}"
echo "=============================="

if [ ${#failed_tests[@]} -gt 0 ]; then
    echo
    echo "${RED}${BOLD}Failed tests:${RESET}"
    for t in "${failed_tests[@]}"; do
        echo "  - $t"
    done
    echo
    echo "Check the corresponding .diff files for details."
fi

echo "${CYAN}${BOLD}Done.${RESET}"
