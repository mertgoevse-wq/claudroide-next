# ClauDroide-Bauzustand

**Stand:** 2026-10-14 (Block A–F — Autonome Fortsetzung: WebP-Assets, Keystore StrongBox minSdk 26, Mutationsprüfung 085/086, APK Build & Delivery)
**Status:** **135 von 135 Aufgaben `done`**, 0 offen. Kanonische Codebasis `claudroide-next` mit Quellprojekt `claudroide` abgeglichen und konsolidiert (`docs/INTEGRATION_MAP.md`). Back-Navigation hierarchisch gehärtet (BottomSheet → ModelDialog → Drawer → Subdestinations → Exit). SAF Projektordner-Auswahl ("Boom") vollständig reaktiv und persistent mit `ProjectStorage` angebunden. Dark-Mode Scrim repariert (`scrim = Color(0xFF000000)`). Alle 34 UI-Controls auditiert und ohne tote Stubs verdrahtet (`docs/CONTROL_AUDIT.md`). 2.612 Tests in 139 Suiten grün.

**Teststand (selbst gemessen, 2026-10-14):** `./gradlew --no-daemon -Dorg.gradle.jvmargs="-Xmx1536m" :app:testDebugUnitTest` → BUILD SUCCESSFUL, **2.612 Tests in 139 Suiten, 0 Fehler, 0 übersprungen**. `./gradlew --no-daemon -Dorg.gradle.jvmargs="-Xmx1536m" :app:assembleDebug` → BUILD SUCCESSFUL. APK: `app/build/outputs/apk/debug/app-debug.apk` 23.331.258 Bytes, SHA-256 `cd33110ea5d6f618462cb248afdd8bcbcb40935b9558c1a0084442e985115d60`. `python3 tools/secret_gate.py .` → 0 Treffer, exit 0. `python3 tools/sync_frontmatter.py --check` → OK. `git diff --check` → OK.

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

**Letzte sichere Git-Referenz:** `e482179` auf `feat/claudroide-next-architecture`.
