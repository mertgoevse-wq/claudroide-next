# CLAUDROIDE — Aktueller Projektstand

> **Zweck dieser Datei:** Sie ist der **erste Ort**, den ein neuer Lauf liest (neue Claude-Code-Sitzung, ChatGPT, Freebuff). Aus ihr muss ohne Rückfragen klar sein, wo die Arbeit steht und was als Nächstes automatisch passiert.
> **Pflicht:** Nach **jedem** abgeschlossenen Aufgabenblock aktualisieren — zusammen mit `progress/BUILD-STATE.md`.
> **Sprache:** Englische Fachbegriffe erlaubt, aber jede Zeile so, dass der Nutzer sie versteht (einfaches Deutsch).
> **Spec-Lage (2026-10-08, zusammengeführt):** `claudroide-spec.md` ist die **einzige gültige Gesamtspezifikation** (Interviews 1–11, inkl. §29 Start/Wiederaufnahme). Alte Teil-Specs liegen unter `archive/`. Wiederaufnahme-Wort nach Absturz: **`WEITERARBEITEN`** (§29.8).

---

## 0. Kopfzeile (immer zuerst füllen)

**⚠️ Wiederherstellungs-Hinweis (2026-10-08, abends):** Die Spec wurde in dieser Sitzung durch einen fehlerhaften Zusammenbau-Befehl versehentlich auf 48 Zeilen gekürzt. Sie wurde **aus dem Backup (`archive/claudroide-spec-v2-backup.md`, Stand Interview 4) + den wörtlich vorliegenden Interview-5/6/7-Inhalten dieser Sitzung** vollständig rekonstruiert: 1094 Zeilen / 118.652 Bytes, alle Abschnitte §0–§25 + Anhang A/B/C, Format-Prüfung bestanden (Tabellen, fences, doppelte Überschriften). Anhang A/B enthalten die Interview-1-3-Protokolle aus dem Backup; die Interview-5/6/7-Entscheidungen stehen in §20/§21/§25. Wer die Geschichte prüfen will: `archive/claudroide-spec-v2-backup.md` ist die Vor-Rekonstruktions-Basis.

| Feld | Wert |
|---|---|
| **Stand** | `2026-10-14` (135/135 Tasks done; Block A+B abgeschlossen) |
| **Aktuelle Phase** | Implementation & Verification (alle 135 Tasks erledigt) |
| **Nächster Task** | Produktweiterentwicklung außerhalb des Task-Systems: P0 — Vertrauenswürdige Basis (README-Korrekturen, Dokumentationskonsistenz, CI-Verifikation) |
| **Letzter Schritt in diesem Lauf** | Block A (Reality Check) & Block B (APK-Build, Tests, Delivery) abgeschlossen: 2.608 Tests grün, APK gebaut und nach `/sdcard/Download/` kopiert, CI-Trigger korrigiert, README/Dokumentation auf Messwerte aktualisiert. |
| **Letzte abgeschlossene Tasks** | Alle 135 Tasks (`done`) — kanonische Codebasis `claudroide-next` mit Quellprojekt `claudroide` abgeglichen |
| **Läuft gerade** | nichts — kein Build, kein Shell-Job, kein Subagent |
| **Not-Aus-Status** | nicht ausgelöst |
| **Nachweis-Datei** | `progress/BUILD-STATE.md` (technischer Zwilling dieser Datei) |

---

## 1. Was fertig ist — mit Beleg

| Task | Ergebnis | Beleg (Test/Screenshot/Messung) | Datum |
|---|---|---|---|
| 001–135 | Alle 135 Tasks `done` | `sync_frontmatter.py --check` → OK | 2026-10-14 |
| Tests | 2.608 Tests, 0 Fehler, 0 übersprungen | XML-Auswertung aller 139 Suiten | 2026-10-14 |
| Build | `./gradlew assembleDebug` → BUILD SUCCESSFUL | 23.331.258 Bytes APK | 2026-10-13 |
| APK-Delivery | Kopiert nach `/sdcard/Download/ClauDroide-debug.apk`, `.sha256`-Sidecar | SHA-256 `49be1f3e...` (identisch mit Build-Output) | 2026-10-14 |
| CI-Trigger | `build-apk.yml` & `repo-health.yml` auf `[feat/claudroide-next-architecture]` | Git-Diff verifiziert | 2026-10-14 |
| Brand-Assets | mark-1024/256/96.png, banner.png, ic_launcher in 5 Dichten | `tools/test_icon_geometry.py` → 4 Assertions grün | 2026-10-13 |

**Regel:** Eine Zeile darf nur hier stehen, wenn der Beleg existiert und im `BUILD-STATE.md` verlinkt ist. Keine Wunschliste.

---

## 2. Offene Probleme und Blocker

| Blocker | Seit | Versuche | Warum noch offen | Nächster Versuch |
|---|---|---|---|---|
| Kein Handy per Kabel (`adb devices` leer) | 2026-10-08 | 1 | Gerätebeweise nicht möglich | Nutzer schließt Gerät an oder aktiviert kabellose Fehlersuche |
| NPU-Zugriff unbelegt | 2026-10-08 | 0 | Forschungsblock Phase 9 | Task 9.1 |
---

## 3. Wichtige Entscheidungen (Kurzform, Langform in `docs/DECISIONS.md`)

| Datum | Entscheidung | Grund | Quelle |
|---|---|---|---|
| 2026-10-08 | Eigene Maschine **und** echte Claude-Code-Maschine; echte automatisch, wenn Anthropic-Schlüssel vorhanden | Nutzerwunsch | Spec §21 |
| 2026-10-08 | `claudroide-spec.md` = einzige gültige Gesamtspezifikation; alte Teil-Specs in `archive/`; §29 regelt Start (§29.7) und Wiederaufnahme mit `WEITERARBEITEN` (§29.8) | Nutzerentscheidung (Zusammenführung) | Spec §29, `docs/DECISIONS.md` |
| 2026-10-08 | Wöchentliche lesbare Sicherung an **zwei Orten**; Commit+Push nach jedem Block mit Secret-Scan | Nutzerbestätigung (Klärungsrunde) | Spec §5.4/§19.3, `docs/DECISIONS.md` |
| 2026-10-08 | Anschlüsse: **20128 nur OmniRoute**, **20130 Claudroide**, 8787 DroidRoute, 5037 der eine adb-Server | Nutzerentscheidung | Spec §3.1 |
| 2026-10-08 | Kleine Modelle: **GGUF** Standard, **LiteRT** für NPU; Reihenfolge **Grafik → Prozessor → NPU** | Nutzerentscheidung | Spec §7.4b/§23 |
| 2026-10-08 | Eigenes Code-Material **ohne** Herkunftsangabe; Fremdteile mit kurzer Zeile; fremde Gesamt-Repos bleiben privat | Nutzerwunsch + Lizenzpflicht | Spec §2.4 |
| 2026-10-08 | Nur **1 Subagent**, **1 Shell**, **1 Build** gleichzeitig (A56 stürzt sonst ab) | gemessene Gerätegrenze | Spec §4.2 |

---

## 4. Genutzte Quellen in diesem Lauf

| Quelle | Pfad/Link | Wofür benutzt | Ergebnis |
|---|---|---|---|
| — | — | noch kein Lauf | — |

**Regel:** Hier steht nur, was **tatsächlich** in diesem Lauf gelesen oder geklont wurde — nicht die ganze Liste (die steht in `docs/SOURCE_INDEX.md`).

---

## 5. Tests und Nachweise

| Prüfung | Befehl | Ergebnis | Datum |
|---|---|---|---|
| — | — | noch nicht gelaufen | — |

**Regel:** Immer der **echte** Befehl, die **echte** Zahl, das **echte** Datum. Bei Fehlschlag: Fehlschlag eintragen, nicht weglassen.

---

## 6. Bekannte Einschränkungen (ehrlich)

- Bau läuft auf einem Handy in einer PRoot-Umgebung: **~2,2 GB freier Arbeitsspeicher**, **22 GB freier Speicher**, 8 Kerne → langsame Builds, ein Build gleichzeitig.
- **Kein Kabel** → Gerätebeweise (Bildschirmsteuerung, APK-Installation) noch offen.
- **NPU** für Apps normalerweise gesperrt; Wege sind recherchiert, aber **nicht belegt** (Spec §23).
- **Kein Anthropic-Schlüssel** vorhanden → die echte Claude-Maschine ist vorbereitet, aber ungetestet.

---

## 7. Was als Nächstes automatisch passiert

1. P0 — Vertrauenswürdige Basis: README-Badges korrigiert, BUILD-STATE/CLAUDROIDE_CURRENT_STATE synchronisiert, CI-Trigger committet und verifiziert.
2. Brand-Asset-Korrektur: WebP-Assets (`claudroide_mark.webp`, `claudroide_banner.webp`) sind veraltet (114 KB, 149 KB — über dem 50 KB Cap). Regenerierung aus frischen PNGs geplant.
3. Dokumentations-Grafiken: Infografik "How the Autonomous Loop Works", Architecture-Diagramm, Assets Pipeline — benötigen Media Bridge (AGY nicht konfiguriert, siehe Blocker).
4. Anhalten nur bei: fehlendem Nutzerentscheid, fehlendem Schlüssel/Gerät (dann weiterbauen und Lücke offen markieren), oder zwei gescheiterten Versuchen.

---

## 8. Übergabe-Hinweis für ein anderes Programm

Wenn ChatGPT oder eine neue Sitzung übernimmt: **Zuerst diese Datei lesen, dann `docs/TASKS.md` (Phase und nächster Task), dann `progress/BUILD-STATE.md` (Belege).** Widersprüche zwischen Dateien werden zugunsten der **neuesten Messung** entschieden und sofort hier eingetragen.
