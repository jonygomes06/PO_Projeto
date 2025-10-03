#!/usr/bin/env pwsh
# ==============================
# Configuration
# ==============================
$JAR = "po-uilib.jar"
$MAIN_CLASS = "bci.app.App"
$SRC_DIR = "."
$BIN_DIR = "."
$TEST_DIR = "D:\01\IST\Cadeiras\Programação com Objetos\PO_Projeto\src\tests"

# CLI argument: show diffs if present
$SHOW_DIFFS = $false
if ($args.Length -gt 0 -and $args[0] -eq "--show-diffs") {
    $SHOW_DIFFS = $true
}

# ==============================
# Helper Functions
# ==============================
function Say($text, $color = "White", $bold = $false) {
    if ($bold) {
        Write-Host $text -ForegroundColor $color -NoNewline
        Write-Host ""
    } else {
        Write-Host $text -ForegroundColor $color
    }
}

function Diff-Files($expected, $actual, $maxLines = 15, $showDiffs = $false) {
    $expectedLines = Get-Content $expected
    $actualLines   = Get-Content $actual

    $differences = Compare-Object -ReferenceObject $expectedLines -DifferenceObject $actualLines

    if ($differences.Count -gt 0) {
        $diffPath = [System.IO.Path]::ChangeExtension($expected, ".diff")
        $diffContent = @()

        $differences | ForEach-Object {
            if ($_.SideIndicator -eq "=>") {
                $diffContent += "+ $($_.InputObject)"
            } elseif ($_.SideIndicator -eq "<=") {
                $diffContent += "- $($_.InputObject)"
            }
        }

        [System.IO.File]::WriteAllLines($diffPath, $diffContent, [System.Text.Encoding]::UTF8)

        if ($showDiffs) {
            $diffContent | Select-Object -First $maxLines | ForEach-Object {
                if ($_ -like "+ *") {
                    Write-Host "    $_" -ForegroundColor Green
                } elseif ($_ -like "- *") {
                    Write-Host "    $_" -ForegroundColor Red
                } else {
                    Write-Host "    $_" -ForegroundColor Gray
                }
            }
        }
        return $false
    } else {
        $oldDiff = [System.IO.Path]::ChangeExtension($expected, ".diff")
        if (Test-Path $oldDiff) { Remove-Item $oldDiff -Force }
    }
    return $true
}

# ==============================
# Compile Java Sources
# ==============================
Say "→ Compiling Java sources..." "Cyan" $true
$javaFiles = Get-ChildItem -Path $SRC_DIR -Recurse -Filter *.java | ForEach-Object { $_.FullName }

if ($javaFiles.Count -eq 0) {
    Say "⚠ No Java source files found." "Yellow"
    exit 1
}

$classpath = "$JAR;."
& javac -cp $classpath -d $BIN_DIR $javaFiles
if ($LASTEXITCODE -ne 0) {
    Say "✗ Compilation failed. Fix errors and rerun." "Red"
    exit 1
}

Say "✓ Compilation successful." "Green"
Write-Host ""

# ==============================
# Run Tests
# ==============================
$total = 0
$passed = 0
$failedTests = @()

Say "→ Running tests in '$TEST_DIR'..." "Cyan" $true
Write-Host ""

Get-ChildItem -Path $TEST_DIR -Filter *.in | ForEach-Object {
    $inputFile = $_.FullName
    $baseName = $_.BaseName

    $importFile = Join-Path $TEST_DIR "$baseName.import"
    $expectedFile = Join-Path $TEST_DIR "$baseName.out"
    $outputFile = Join-Path $TEST_DIR "$baseName.outhyp"

    # Run program
    if (Test-Path $importFile) {
        & java -cp $classpath `
            "-Dimport=$importFile" `
            "-Din=$inputFile" `
            "-DwriteInput=true" `
            "-Dout=$outputFile" `
            $MAIN_CLASS *> $null
    } else {
        & java -cp $classpath `
            "-Din=$inputFile" `
            "-DwriteInput=true" `
            "-Dout=$outputFile" `
            $MAIN_CLASS *> $null
    }

    if (-not (Test-Path $outputFile)) {
        Say "⚠ ${baseName}: No output produced." "Yellow"
        $failedTests += "$baseName (no output)"
        $total++
        return
    }

    # Compare outputs
    $same = Diff-Files $expectedFile $outputFile
    if (-not $same) {
        Say "✗ $baseName" "Red"
        $failedTests += $baseName
        if ($SHOW_DIFFS) {
            Say "--- Diff snippet ---" "Yellow"
            Diff-Files $expectedFile $outputFile 15 true
            Say "--------------------" "Yellow"
        }
    } else {
        Say "✓ $baseName" "Green"
        $passed++
        Remove-Item -ErrorAction SilentlyContinue $outputFile
    }

    $total++
}

# Cleanup saved* files
Get-ChildItem -Path . -Filter "saved*" -ErrorAction SilentlyContinue | Remove-Item -Force

# ==============================
# Summary
# ==============================
Write-Host ""
Say "==============================" "White" $true
Say "📊 TEST SUMMARY" "White" $true
Write-Host "Total tests : $total"
Write-Host ("Passed      : " + ($passed.ToString())) -ForegroundColor Green
Write-Host ("Failed      : " + (($total - $passed).ToString())) -ForegroundColor Red
$pct = if ($total -gt 0) { [int](100 * $passed / $total) } else { 0 }
Write-Host ("Success     : $pct%")
Say "==============================" "White" $true

# ==============================
# Cleanup .class files
# ==============================
Say "→ Cleaning up .class files..." "Cyan" $true
Get-ChildItem -Path $BIN_DIR -Recurse -Filter *.class | Remove-Item -Force
Say "✓ All .class files removed." "Green"
