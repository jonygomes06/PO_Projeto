# ==============================
# build.ps1 - PowerShell equivalent of Makefile
# ==============================
param(
    [switch]$Compile,
    [switch]$Run,
    [string]$RunArgs,
    [switch]$Test,
    [switch]$Clean,
    [switch]$BuildJar
)

# ==============================
# Configuration
# ==============================
$SRC_DIR     = "src"
$BIN_DIR     = "bin"
$PROJ_BIN    = "proj_bin"
$MAIN_CLASS  = "bci.app.App"
$OUTPUT_JAR  = "proj.jar"
$TEST_DIR    = "tests"
$TEST_SCRIPT = ".\run-tests.ps1"
$CLASSPATH   = "$BIN_DIR"
$JAR_TOOL = "C:\Program Files\Java\jdk-24\bin\jar.exe"

# ==============================
# Helper Function
# ==============================
function Say($msg, $color="White") {
    Write-Host $msg -ForegroundColor $color
}

# ==============================
# Compilation
# ==============================
if ($Compile -or $Run -or $Test) {
    if (-not (Test-Path $BIN_DIR)) {
        New-Item -ItemType Directory -Path $BIN_DIR | Out-Null
    }

    Say "→ Compiling Java sources..." "Cyan"
    $sources = Get-ChildItem -Path $SRC_DIR -Recurse -Filter *.java | ForEach-Object { $_.FullName }

    if ($sources.Count -eq 0) {
        Say "⚠ No Java source files found." "Yellow"
        exit 1
    }

    & javac -cp $CLASSPATH -d $BIN_DIR $sources
    if ($LASTEXITCODE -ne 0) {
        Say "✗ Compilation failed." "Red"
        exit 1
    }

    # Touch marker file
    $compiledMarker = Join-Path $BIN_DIR ".compiled"
    New-Item -ItemType File -Path $compiledMarker -Force | Out-Null
    Say "✅ Compilation finished." "Green"
}

# ==============================
# Build JAR (with manifest)
# ==============================
if ($BuildJar) {
    Say "📦 Creating JAR..." "Cyan"

    if (Test-Path $OUTPUT_JAR) {
        Remove-Item $OUTPUT_JAR -Force
    }

    & $JAR_TOOL cf $OUTPUT_JAR -C $SRC_DIR .
    if ($LASTEXITCODE -ne 0) {
        Say "✗ Failed to create JAR." "Red"
        exit 1
    }

    Say "✅ JAR created: $OUTPUT_JAR" "Green"
}

# ==============================
# Run Main Program
# ==============================
if ($Run) {
    Say "→ Running main program..." "Cyan"
    & java -cp $CLASSPATH $RunArgs $MAIN_CLASS
}

# ==============================
# Run Tests
# ==============================
if ($Test) {
    Say "→ Running tests..." "Cyan"
    if (-not (Test-Path $TEST_SCRIPT)) {
        Say "⚠ Test script not found: $TEST_SCRIPT" "Yellow"
        exit 1
    }
    & $TEST_SCRIPT
}

# ==============================
# Clean build artifacts
# ==============================
if ($Clean) {
    Say "🧹 Cleaning up..." "Cyan"
    if (Test-Path $BIN_DIR) { Remove-Item -Recurse -Force $BIN_DIR }
    if (Test-Path $PROJ_BIN) { Remove-Item -Recurse -Force $PROJ_BIN }
    if (Test-Path $OUTPUT_JAR) { Remove-Item -Force $OUTPUT_JAR }

    Get-ChildItem -Path $TEST_DIR -Recurse -Include *.outhyp, *.diff -ErrorAction SilentlyContinue | Remove-Item -Force
    Get-ChildItem -Path . -Filter "saved*" -ErrorAction SilentlyContinue | Remove-Item -Force
    Get-ChildItem -Path $SRC_DIR -Filter "*.class" -ErrorAction SilentlyContinue | Remove-Item -Force

    Say "✅ Clean complete." "Green"
}
