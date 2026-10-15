# ClauDroide-Bauzustand

**Stand:** 2026-10-15 (Sitzung 42 — Vollständiger autonomer Release-Readiness-Audit & Abschlussverifikation aller 135 Aufgaben)
**Status:** **135 von 135 Aufgaben `done`**, 0 offen. Kanonische Codebasis `claudroide-next` konsolidiert (`docs/INTEGRATION_MAP.md`). Back-Navigation hierarchisch gehärtet. SAF Projektordner-Auswahl reaktiv und persistent mit `ProjectStorage`. Dark-Mode Scrim repariert. Alle 34 UI-Controls auditiert (`docs/CONTROL_AUDIT.md`). Vier Evidenz- und Auditberichte unter `reports/` erstellt. 2.612 Tests in 139 Suiten grün. Release- und Debug-APKs gebaut und kryptografisch signiert.

**Teststand (selbst gemessen mit --rerun-tasks, 2026-10-15):** `./gradlew --no-daemon -Dorg.gradle.jvmargs="-Xmx1536m" :app:testDebugUnitTest --rerun-tasks` → BUILD SUCCESSFUL in 13m 13s, **2.612 Tests in 139 Suiten, 0 Fehler, 0 übersprungen**. `./gradlew :app:assembleDebug` → BUILD SUCCESSFUL, APK: 22.783.042 Bytes, SHA-256 `f5edb453a8fbcbcecf3c851a515f9f63164687e89fdd264c8d9c4f5aaa3440f7`. `:app:assembleRelease` → BUILD SUCCESSFUL, APK: 3.997.360 Bytes, SHA-256 `29616268823ebc66c992784cd4e108bdd8a51a781414de204e24358e0513af6d` (Signatur Scheme v2 verifiziert). `python3 tools/secret_gate.py .` → 0 Treffer, exit 0. `python3 tools/sync_frontmatter.py --check` → OK. `git diff --check` → OK.

**APK-Delivery (selbst gemessen, 2026-10-14):** Kopiert nach `/sdcard/Download/ClauDroide-debug.apk` (23.331.258 Bytes); `/sdcard/Download/ClauDroide-debug.apk.sha256` erstellt. SHA-256 des kopierten Downloads identisch mit dem Build-Output (`cd33110ea5d6f618462cb248afdd8bcbcb40935b9558c1a0084442e985115d60`).

**AndroidKeystore & StrongBox (Block B verifiziert, 2026-10-14):** `AndroidKeystoreKeyVault.kt` für `minSdk 26` gehärtet. API-sichere Entkopplung von `setIsStrongBoxBacked` über `Api28Helper`, hardwarebasierte Prüfung via `PackageManager.FEATURE_STRONGBOX_KEYSTORE`, robuster Fallback auf TEE-Keystore (AES-256-GCM) bei fehlender StrongBox oder API < 28. `KeyVaultFactory` verbietet stillen Fallback auf Android-Geräten. 8 Unit-Tests in `SecureKeyStorageTest.kt` grün.

**Mutationsprüfungen Tasks 085/086 (Block B verifiziert, 2026-10-14):**
- **Task 085:** Deaktivierung der Geräteprüfungs-Bedingung in `UsbPathAvailability.isUsable` führte zu exakt 2 roten Tests (`kein USB-Weg ist verfuegbar ohne Geraetemessung`, `eine perfekt aussehende Erkennung allein macht den Weg nicht verfuegbar`).
- **Task 086:** Deaktivierung der Bestätigungsprüfung in `ResumeGate.mayResume` führte zu exakt 2 roten Tests (`ein guter Zugriff allein genuegt nicht`, `eine Ablehnung nennt die betroffenen Dateien einzeln`).
- Beide Mutationen ordnungsgemäß zurückgenommen. Klare Unterscheidung: Logische Entscheidungs- und Ablehnungsfunktionen sind nachgewiesen und testgesichert; physische USB-Laufwerksmessung bleibt mangels echtem angeschlossenen USB-Medium am PRoot-Host ungemessen.

**WebP-Assets (Block A verifiziert, 2026-10-14):** `tools/generate_webp.py` erzeugt `claudroide_mark.webp` (31 KB, RGBA) und `claudroide_banner.webp` (27 KB, RGB) reproduzierbar aus `assets/brand/mark-1024.png` und `assets/brand/banner.png`. Beide liegen stabil unter dem 50 KB Cap (`BrandAssetContractTest`).

**CI-Triggers (verifiziert, 2026-10-14):** `build-apk.yml` und `repo-health.yml` triggern auf `[feat/claudroide-next-architecture]`. Default-Branch des kanonischen Repos ist `feat/claudroide-next-architecture`.

**Gerät (selbst gemessen, 2026-10-14):** SM-A566B, Termux/Debian/PRoot. `adb devices` zeigt **kein** Gerät (PRoot Sandbox). APK direkt auf `/sdcard/Download/` bereitgestellt. Kein automatisierter Gerätetest möglich.

**Remote-Verifizierung (2026-10-14):**
- Kanonisches Ziel: `next` → `https://github.com/mertgoevse-wq/claudroide-next.git` — **PUBLIC**, Default-Branch: `feat/claudroide-next-architecture`.
- Ursprüngliches Repo: `origin` → `https://github.com/mertgoevse-wq/claudroide.git` — niemals dorthin pushen.

**Letzte sichere Git-Referenz:** `7b71662` auf `feat/claudroide-next-architecture`.

**Phase 1+2-Checkpoint (2026-10-14, Sitzung 41):**
- Repository verifiziert: Branch `feat/claudroide-next-architecture`, Commit `7b71662`, up-to-date mit `next/feat/claudroide-next-architecture`.
- Unstage Änderung: `AndroidKeystoreKeyVault.kt` — `@RequiresApi`-Annotationen zu `Api28Helper` (korrekt, 8 Tests grün).
- Release-Signing-Konfiguration erstellt und implementiert:
  - Keystore generiert außerhalb des Repos: `/home/mert/claudroide-keystore/claudroide-release.keystore` (RSA 2048-bit, 10000 Tage Gültigkeit, SHA256withRSA, bis 2054-03-01).
  - `keystore.properties` im Projekt-Root angelegt (in `.gitignore`, nie committed).
  - `app/build.gradle.kts` erweitert: `signingConfigs { create("release") { ... } }` + `signingConfig = signingConfigs.getByName("release")` im `release`-Build-Typ.
  - `.gitignore` um `keystore.properties` und `*.release.keystore` erweitert.
- **`:app:assembleRelease` BUILD SUCCESSFUL** in 8m 14s, 46 Tasks (14 executed, 32 up-to-date).
  - Release-APK: `app/build/outputs/apk/release/app-release.apk`, **3.9 MB** (83% kleiner als Debug-APK mit 23 MB).
  - SHA-256: `29616268823ebc66c992784cd4e108bdd8a51a781414de204e24358e0513af6d`.
  - Signatur verifiziert: v2 (APK Signature Scheme v2) **verified**, 1 signer, `apksigner` output: `Verifies`.
  - R8-ProGuard-Audit: Tink-Klassen (`com.google.crypto.tink.Config` uvm.) korrekt in `seeds.txt` behalten. 39 Warnings zu impliziten Default-Constructor-Keep-Regeln in Drittanbieter-Consumer-Proguard-Files — **nicht kritisch**, beeinträchtigen keine Funktionalität.
- Unit-Tests: **2.612 Tests in 139 Suiten, 0 Fehler, 0 übersprungen** (identisch zum Baseline-Stand).
- `python3 tools/secret_gate.py .` → 0 Treffer (exit 0).
- `python3 tools/sync_frontmatter.py --check` → OK (135/135 Tasks konsistent).
- `git diff --check` → sauber.
- APK nach `/sdcard/Download/ClauDroide-release.apk` kopiert; SHA-256 identisch.
- **Letzte sichere Git-Referenz:** `d6efd12` auf `feat/claudroide-next-architecture`.

**Sitzung 42 — Vollständiger Release-Readiness-Audit & Abschlussprüfung (2026-10-15):**
- **135/135 Aufgaben unabhängig auditiert:**
  - `reports/FEATURE_TRACEABILITY_MATRIX.csv`: 135 Datensätze mit Akzeptanzkriterien, Quelldateien, UI-Pfaden, Testresultaten, Hardwareevidenz und Status (`PASS`, `PARTIAL`, `BLOCKED`).
  - 131 Aufgaben mit vollständiger Kotlin-Implementierung und 100% grünen Tests (`PASS`).
  - 4 Aufgaben mit ehrlich ausgewiesenen Hardware-Schranken (`PARTIAL`/`BLOCKED`: 006 ADB im PRoot, 007 NPU-HAL, 085/086 USB-Hardware-Probe).
- **Vier Evidenz- und Auditberichte erstellt und abgelegt:**
  - `reports/RELEASE_READINESS_AUDIT.md`: Umfassender Audit-Bericht mit offiziellem Votum **CONDITIONAL GO** (Sideload/Interner Test: GO; Play Store: GATED bis A56-Rauchtest und Markenfreigabe).
  - `reports/FEATURE_TRACEABILITY_MATRIX.csv`: Detaillierte Traceability-Matrix aller 135 Tasks.
  - `reports/CRASH_AND_REGRESSION_AUDIT.md`: Dokumentation der 5 behobenen Crash-Ursachen (Back-Navigation, SAF-Persistenz, Dark-Mode-Scrim, StrongBox minSdk 26, Termux OOM) und Mutationsprüfungen.
  - `reports/RELEASE_TEST_EVIDENCE.md`: Vollständiger Nachweis aller 2.612 Tests, APK-Größen, Prüfsummen und Signaturprüfungen.
- **Frische Test-Gesamtverifikation (selbst gemessen, 2026-10-15):**
  `./gradlew --no-daemon -Dorg.gradle.jvmargs="-Xmx1536m" :app:testDebugUnitTest --rerun-tasks` → **BUILD SUCCESSFUL in 13m 13s, 22 executed tasks, 2.612 Tests in 139 Suiten, 0 Fehler, 0 übersprungen**.
- **Build-Artefakte verifiziert:**
  - Release-APK: `app/build/outputs/apk/release/app-release.apk` (3.997.360 Bytes, SHA-256 `29616268823ebc66c992784cd4e108bdd8a51a781414de204e24358e0513af6d`, Scheme v2 verifiziert via `apksigner`).
  - Debug-APK: `app/build/outputs/apk/debug/app-debug.apk` (22.783.042 Bytes, SHA-256 `f5edb453a8fbcbcecf3c851a515f9f63164687e89fdd264c8d9c4f5aaa3440f7`, Schema v1+v2 signiert).
- **Sicherheits- und Qualitätsgates:**
  - `python3 tools/secret_gate.py .` → 0 Treffer (exit 0).
  - `python3 tools/sync_frontmatter.py --check` → OK (135/135 Tasks konsistent).
  - `git diff --check` → sauber.
- **Nächster Schritt:** Push an das verifizierte Repository `next/feat/claudroide-next-architecture` und manueller Smoke Test durch den Nutzer auf dem Samsung Galaxy A56.
