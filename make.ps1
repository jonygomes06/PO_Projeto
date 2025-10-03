# ==============================
# build.ps1 - Windows PowerShell Makefile equivalent
# ==============================
param(
    [switch]$Compile,
    [switch]$Run,
    [string]$Args,
    [switch]$Test,
    [switch]$Clean
)

# ==============================
# Configuration
# ==============================
$SRC_DIR = "src"
$BIN_DIR = "bin"
$MAIN_CLASS = "bci.app.App"
$JAR = "po-uilib.jar"
$TEST_DIR = "tests"
$TEST_SCRIPT = ".\run-tests.ps1"  # PowerShell version

# ==============================
# Helper Functions
# ==============================
function Say($text, $color="White") {
    Write-Host $text -ForegroundColor $color
}

# ==============================
# Compilation (fast)
# ==============================
if ($Compile) {
    if (-not (Test-Path $BIN_DIR)) {
        New-Item -ItemType Directory -Path $BIN_DIR | Out-Null
    }

    Say "→ Compiling Java sources..." "Cyan"

    # Find all .java files recursively
    $javaFiles = Get-ChildItem -Path $SRC_DIR -Recurse -Filter *.java | ForEach-Object { $_.FullName }

    if ($javaFiles.Count -eq 0) {
        Say "⚠ No Java source files found." "Yellow"
        exit 1
    }

    # Determine which files need compilation
    $filesToCompile = @()
    foreach ($file in $javaFiles) {
        # Map package path to correct .class file in bin/
        $relativePath = Resolve-Path $file | ForEach-Object {
            $_.Path.Substring((Resolve-Path $SRC_DIR).Path.Length).TrimStart('\','/')
        }
        $classFile = Join-Path $BIN_DIR ([IO.Path]::ChangeExtension($relativePath, ".class"))

        if (-not (Test-Path $classFile) -or (Get-Item $file).LastWriteTime -gt (Get-Item $classFile).LastWriteTime) {
            $filesToCompile += $file
        }
    }

    if ($filesToCompile.Count -eq 0) {
        Say "✅ All classes are up-to-date." "Green"
    } else {
        $classpath = "$JAR;$BIN_DIR"
        & javac -cp $classpath -d $BIN_DIR $filesToCompile
        if ($LASTEXITCODE -ne 0) {
            Say "✗ Compilation failed." "Red"
            exit 1
        } else {
            foreach ($f in $filesToCompile) { Say "Compiled $f" "Green" }
            Say "✅ Compilation finished." "Green"
        }
    }
}

# ==============================
# Run Main Program
# ==============================
if ($Run) {
    Say "→ Running program..." "Cyan"
    $argsToPass = if ($Args) { $Args } else { "" }
    & java -cp "$JAR;$BIN_DIR" $MAIN_CLASS $argsToPass
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
    Say "→ Cleaning build artifacts..." "Cyan"
    # Remove bin directory entirely
    if (Test-Path $BIN_DIR) {
        Remove-Item -Recurse -Force $BIN_DIR
    }
    # Remove test outputs and diffs
    Get-ChildItem -Path $TEST_DIR -Recurse -Filter *.outhyp -ErrorAction SilentlyContinue | Remove-Item -Force
    Get-ChildItem -Path $TEST_DIR -Recurse -Filter *.diff -ErrorAction SilentlyContinue | Remove-Item -Force
    # Remove saved* files
    Get-ChildItem -Path . -Filter "saved*" -ErrorAction SilentlyContinue | Remove-Item -Force

    Say "✅ Clean complete." "Green"
}
