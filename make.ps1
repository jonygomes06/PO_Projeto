# ==============================
# build.ps1 - Windows PowerShell Makefile equivalent
# ==============================
param(
    [switch]$Compile,
    [switch]$Run,
    [string]$Args,
    [switch]$Test,
    [switch]$Clean,
    [switch]$BuildJar
)

# ==============================
# Configuration
# ==============================
$SRC_DIR = "src"
$BIN_DIR = "bin"
$MAIN_CLASS = "bci.app.App"
$JAR = "po-uilib.jar"
$OUTPUT_JAR = "proj.jar"
$JAR_TOOL = "C:\Program Files\Java\jdk-24\bin\jar.exe"
$MANIFEST_FILE = "manifest.mf"
$TEST_DIR = "tests"
$TEST_SCRIPT = ".\run-tests.ps1"

# ==============================
# Helper Functions
# ==============================
function Say($text, $color="White") {
    Write-Host $text -ForegroundColor $color
}

# ==============================
# Compilation (fast)
# ==============================
if ($Compile -or $BuildJar) {
    if (-not (Test-Path $BIN_DIR)) {
        New-Item -ItemType Directory -Path $BIN_DIR | Out-Null
    }

    Say "→ Compiling Java sources..." "Cyan"

    $javaFiles = Get-ChildItem -Path $SRC_DIR -Recurse -Filter *.java | ForEach-Object { $_.FullName }

    if ($javaFiles.Count -eq 0) {
        Say "⚠ No Java source files found." "Yellow"
        exit 1
    }

    $filesToCompile = @()
    foreach ($file in $javaFiles) {
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
# Build JAR (with manifest)
# ==============================
if ($BuildJar) {
    Say "→ Building JAR (with manifest)..." "Cyan"

    if (-not (Test-Path $JAR_TOOL)) {
        Say "✗ jar tool not found at: $JAR_TOOL" "Red"
        exit 1
    }

    if (-not (Test-Path $BIN_DIR)) {
        Say "✗ No compiled classes found. Run with -Compile first." "Red"
        exit 1
    }

    # Create manifest file
    @"
Manifest-Version: 1.0
Main-Class: $MAIN_CLASS

"@ | Out-File -Encoding ASCII $MANIFEST_FILE

    # Remove old jar if it exists
    if (Test-Path $OUTPUT_JAR) {
        Remove-Item $OUTPUT_JAR -Force
    }

    # Build the JAR with the manifest
    Push-Location $BIN_DIR
    & "$JAR_TOOL" cfm "../$OUTPUT_JAR" "../$MANIFEST_FILE" *
    Pop-Location

    if ($LASTEXITCODE -eq 0) {
        Say "✅ JAR created with manifest: $OUTPUT_JAR" "Green"
    } else {
        Say "✗ Failed to create JAR." "Red"
        exit 1
    }

    # Clean up manifest
    if (Test-Path $MANIFEST_FILE) {
        Remove-Item $MANIFEST_FILE -Force
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
    if (Test-Path $BIN_DIR) {
        Remove-Item -Recurse -Force $BIN_DIR
    }
    if (Test-Path $OUTPUT_JAR) {
        Remove-Item $OUTPUT_JAR -Force
    }
    if (Test-Path $MANIFEST_FILE) {
        Remove-Item $MANIFEST_FILE -Force
    }
    Get-ChildItem -Path $TEST_DIR -Recurse -Filter *.outhyp -ErrorAction SilentlyContinue | Remove-Item -Force
    Get-ChildItem -Path $TEST_DIR -Recurse -Filter *.diff -ErrorAction SilentlyContinue | Remove-Item -Force
    Get-ChildItem -Path . -Filter "saved*" -ErrorAction SilentlyContinue | Remove-Item -Force

    Say "✅ Clean complete." "Green"
}
