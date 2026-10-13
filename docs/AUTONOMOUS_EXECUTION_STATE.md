# ClauDroide — Autonomous Execution State

## Current Project State
- **Project path:** `/home/mert/claudroide-next`
- **Branch:** `feat/claudroide-next-architecture`
- **Git remote:** `next` → `https://github.com/mertgoevse-wq/claudroide-next.git`
- **Last safe Git reference:** `ed9dc52`
- **Current sprint:** Claude-faithful Android UI implementation & crash hardening

## Phase 0 — Crash Root Cause Analysis
1. **Device & Environment:** Samsung Galaxy A56 5G (SM-A566B) running Debian PRoot in Termux, Linux 6.17.0.
2. **Storage Resource Constraint:** Root filesystem is **98% full** with only 2.5 GB free. Large cache generation or uncontrolled build temp files risk triggering disk space exhaustion.
3. **RAM & Process Pressure:** 7.4 GB total RAM with ~400 MB free and 3.3 GB swap used. Daemon memory leaks easily cause PRoot / Termux process termination.
4. **Gradle Stabilization:** Builds enforced with `-Dorg.gradle.jvmargs="-Xmx1536m" --no-daemon` to prevent daemon accumulation and memory thrashing.
5. **App Runtime Startup:** Historical `NoSuchMethodException` during `ChatViewModel` instantiation was resolved via `AndroidViewModel` + `@JvmOverloads` + `Factory` in `MainActivity`.
6. **Design System Reconciliation:** Unified on `theme_compat` (`CcTheme`, `CcColors`, `CcType`) resolving prior runtime conflicts where components read tokens not set by the root theme.

## Phase 1 — Build & Artifact Status
- **Build task:** `./gradlew :app:assembleDebug` → **BUILD SUCCESSFUL**
- **Test task:** `./gradlew :app:testDebugUnitTest` → **BUILD SUCCESSFUL**, 2587+ unit tests passing, 0 failures, 0 skipped
- **Generated APK:** `app/build/outputs/apk/debug/app-debug.apk` (23 MB)
- **Delivered APK:** `/sdcard/Download/ClauDroide-debug.apk` (23 MB, verified with sha256 checksum)
- **Package ID:** `org.claudroide.next.debug` (VersionCode: 1, VersionName: 0.1.0)
- **Security scan:** `python3 tools/secret_gate.py .` → 0 matches (exit code 0)
- **Task consistency:** `python3 tools/sync_frontmatter.py --check` → OK (all 135 task files consistent)

## Phase 2 — Claude-Faithful UI Implementation
1. **Visual Reference:** 4 high-resolution screenshots from the official Claude Android app on the user's Galaxy A56:
   - Empty Home Screen (`Screenshot_20261010_133740_Claude.jpg`)
   - Navigation Drawer (`Screenshot_20261010_133751_Claude.jpg`)
   - Active Chat & Tool Execution (`Screenshot_20261010_133802_Claude.jpg`)
   - Project Creation Sheet (`Screenshot_20261010_133808_Claude.jpg`)
2. **Empty / Home State (`ChatScreen.kt`):**
   - Clean top bar with navigation drawer hamburger toggle on left and profile avatar on right.
   - Centered Claude-style brand mark (`BrandMark`, 64 dp).
   - Prominent Serif headline: `TimeBasedGreeting` ("Guten Tag, Mert" / "Good afternoon, Mert") in 30sp serif typography.
   - Removed generic `AutoAwesome` sparkle icon and removed bottom FAB.
3. **Active Chat Transcript (`ChatScreen.kt`):**
   - User messages: right-aligned warm dark bubble (`RoundedCornerShape(20.dp)`).
   - Assistant responses: left-aligned canvas display without bubble enclosing, rendered via `StreamingText` with full Markdown formatting and `CodeBlock` syntax highlighting.
4. **Floating Bottom Composer Card:**
   - Elevated pill container (`RoundedCornerShape(26.dp)`) with subtle border.
   - Clean multiline text field with `"Mit ClauDroide chatten…"` (DE) / `"Reply to ClauDroide…"` (EN) placeholder.
   - Bottom row: circular `+` attachment button, model selector pill, and dual voice/action button (circular white button with black icon).
5. **Native Modal Navigation Drawer (`MainActivity.kt` & `NavigationDrawer.kt`):**
   - Removed unwanted `NavigationSuiteScaffold` bottom tabs.
   - Replaced with Material 3 `ModalNavigationDrawer`.
   - Serif title "ClauDroide", clean section items (Chats, Projekte, Code, Artefakte, Settings), recent chats section, and bottom row featuring user avatar ("MZ") and white "+ Neuer Chat" pill button.
6. **Attachment Bottom Sheet:**
   - Fully wired to `ChatViewModel.addAttachment()`, supporting file, photo, camera, project context, and logcat staging.

## Phase 5 — Device QA & Screen Control
- `adb devices` shows no connected device from inside PRoot (Termux/Debian environment without direct USB/wireless ADB pairing).
- System Logcat is restricted ("Operation not permitted").
- Internal diagnostics preserved via `StartupDiagnostics` at `/sdcard/startup_diagnostics.log`.
- APK deployed directly to accessible storage: `/sdcard/Download/ClauDroide-debug.apk`.

## Next Immediate Action
Commit and push verified changes to `feat/claudroide-next-architecture` on GitHub.
