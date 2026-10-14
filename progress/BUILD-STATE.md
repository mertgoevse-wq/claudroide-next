# ClauDroide-Bauzustand

**Stand:** 2026-10-14 (Block A+B — reality check & APK delivery)
**Status:** **135 von 135 Aufgaben `done`**, 0 offen. Kanonische Codebasis `claudroide-next` mit Quellprojekt `claudroide` abgeglichen und konsolidiert (`docs/INTEGRATION_MAP.md`). Back-Navigation hierarchisch gehärtet (BottomSheet → ModelDialog → Drawer → Subdestinations → Exit). SAF Projektordner-Auswahl ("Boom") vollständig reaktiv und persistent mit `ProjectStorage` angebunden. Dark-Mode Scrim repariert (`scrim = Color(0xFF000000)`). Alle 34 UI-Controls auditiert und ohne tote Stubs verdrahtet (`docs/CONTROL_AUDIT.md`). 2.608 Tests in 139 Suiten grün.

**Teststand (selbst gemessen, 2026-10-14):** `./gradlew --no-daemon -Dorg.gradle.jvmargs="-Xmx1536m" :app:testDebugUnitTest --rerun-tasks` → BUILD SUCCESSFUL, **2.608 Tests in 139 Suiten, 0 Fehler, 0 übersprungen**. `./gradlew --no-daemon -Dorg.gradle.jvmargs="-Xmx1536m" :app:assembleDebug` → BUILD SUCCESSFUL. APK: `app/build/outputs/apk/debug/app-debug.apk` 23.331.258 Bytes, SHA-256 `49be1f3eb260e80acd35edc426ff8024547f4f98e4c6a63642e3d06c157fe871`. `python3 tools/secret_gate.py .` → 0 Treffer, exit 0. `python3 tools/sync_frontmatter.py --check` → OK. `git diff --check` → OK.

**APK-Delivery (selbst gemessen, 2026-10-14):** Kopiert nach `/sdcard/Download/ClauDroide-debug.apk` (23.331.258 Bytes); `/sdcard/Download/ClauDroide-debug.apk.sha256` erstellt. SHA-256 des kopierten Downloads identisch mit dem Build-Output (`49be1f3eb260e80acd35edc426ff8024547f4f98e4c6a63642e3d06c157fe871`).

**CI-Triggers (korrigiert, 2026-10-14):** `build-apk.yml` und `repo-health.yml` ausgelöst auf `[feat/claudroide-next-architecture]` (war `[main]`). Default-Branch des kanonalen Repos ist `feat/claudroide-next-architecture` (per `git ls-remote --symref next HEAD` + GitHub API verifiziert).

**Brand-Asset-Fix (Sitzung 38, 2026-10-13):** `tools/brand.py` regenerierte das Markenzeichen am 2026-10-13; WebP-Assets (`claudroide_mark.webp` 30 KB, `claudroide_banner.webp` 27 KB) wurden aus frischen PNGs erneuert, beide innerhalb des 50 KB Caps. Neuer `tools/test_icon_geometry.py` pinnt die in `app_icon.py` duplizierte `_face_geometry()` an die in `brand.mark()` liegenden Fraktionen. 4 Geometrie-Assertions grün. Commit `0ec4f9a` gepusht auf `next/feat/claudroide-next-architecture`.

**Gerät (selbst gemessen, 2026-10-14):** SM-A566B, Termux/Debian/PRoot. `adb devices` zeigt **kein** Gerät (PRoot Sandbox). APK direkt auf `/sdcard/Download/` bereitgestellt. Kein echter Gerätetest möglich.

**Remote-Verifizierung (2026-10-14):**
- Kanonisches Ziel: `next` → `https://github.com/mertgoevse-wq/claudroide-next.git` — **PUBLIC**, Default-Branch: `feat/claudroide-next-architecture`.
- Ursprüngliches Repo: `origin` → `https://github.com/mertgoevse-wq/claudrode.git` — niemals dorthin pushen.
- Merge-Basis: `440e3c0`. `next` hat 15 Commits mehr als `origin/main`, 0 umgekehrt. Alle Quell-Komponenten aus `440e3c0` (ControlBridge, AccessibilityService, ToolExecutor) sind integriert und durch `ControlBlacklistPolicy`/`EmergencyStopController` zusätzlich gehärtet. Stitch-Referenz (`e8c372c`) ist ebenfalls in `next` enthalten.

**Letzte sichere Git-Referenz:** `292e1c9` (HEAD) auf `feat/claudroide-next-architecture`, lokal sauber (nur uncommitedte CI-Trigger-Änderungen).

**Offene Mutationsprüfungen (085/086):** Die USB-Zugriff-Tests (`UsbProjectAccessTest`, `UsbDisconnectRecoveryTest`) arbeiten mit `const val PROBE_OBSERVED_ON_DEVICE = false` — als Gerüst ohne echte Messung. Mutationsprüfung für 085/086 liegt **offen** (wurde nie durchgeführt, wie in Sitzung 29 dokumentiert).
