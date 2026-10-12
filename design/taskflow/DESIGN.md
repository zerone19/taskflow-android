---
name: TaskFlow
colors:
  surface: '#faf8ff'
  surface-dim: '#d2d9f4'
  surface-bright: '#faf8ff'
  surface-container-lowest: '#ffffff'
  surface-container-low: '#f2f3ff'
  surface-container: '#eaedff'
  surface-container-high: '#e2e7ff'
  surface-container-highest: '#dae2fd'
  on-surface: '#131b2e'
  on-surface-variant: '#464555'
  inverse-surface: '#283044'
  inverse-on-surface: '#eef0ff'
  outline: '#777587'
  outline-variant: '#c7c4d8'
  surface-tint: '#4d44e3'
  primary: '#3525cd'
  on-primary: '#ffffff'
  primary-container: '#4f46e5'
  on-primary-container: '#dad7ff'
  inverse-primary: '#c3c0ff'
  secondary: '#006a61'
  on-secondary: '#ffffff'
  secondary-container: '#86f2e4'
  on-secondary-container: '#006f66'
  tertiary: '#004c76'
  on-tertiary: '#ffffff'
  tertiary-container: '#00659a'
  on-tertiary-container: '#bedfff'
  error: '#ba1a1a'
  on-error: '#ffffff'
  error-container: '#ffdad6'
  on-error-container: '#93000a'
  primary-fixed: '#e2dfff'
  primary-fixed-dim: '#c3c0ff'
  on-primary-fixed: '#0f0069'
  on-primary-fixed-variant: '#3323cc'
  secondary-fixed: '#89f5e7'
  secondary-fixed-dim: '#6bd8cb'
  on-secondary-fixed: '#00201d'
  on-secondary-fixed-variant: '#005049'
  tertiary-fixed: '#cce5ff'
  tertiary-fixed-dim: '#93ccff'
  on-tertiary-fixed: '#001d31'
  on-tertiary-fixed-variant: '#004b73'
  background: '#faf8ff'
  on-background: '#131b2e'
  surface-variant: '#dae2fd'
typography:
  display-lg:
    fontFamily: Inter
    fontSize: 40px
    fontWeight: '700'
    lineHeight: 48px
    letterSpacing: -0.02em
  display-lg-mobile:
    fontFamily: Inter
    fontSize: 32px
    fontWeight: '700'
    lineHeight: 40px
    letterSpacing: -0.02em
  headline-md:
    fontFamily: Inter
    fontSize: 28px
    fontWeight: '600'
    lineHeight: 36px
    letterSpacing: -0.01em
  headline-md-mobile:
    fontFamily: Inter
    fontSize: 24px
    fontWeight: '600'
    lineHeight: 32px
    letterSpacing: -0.01em
  title-lg:
    fontFamily: Inter
    fontSize: 22px
    fontWeight: '600'
    lineHeight: 28px
  title-md:
    fontFamily: Inter
    fontSize: 16px
    fontWeight: '600'
    lineHeight: 24px
    letterSpacing: 0.01em
  title-sm:
    fontFamily: Inter
    fontSize: 14px
    fontWeight: '600'
    lineHeight: 20px
    letterSpacing: 0.01em
  body-lg:
    fontFamily: Inter
    fontSize: 16px
    fontWeight: '400'
    lineHeight: 24px
    letterSpacing: 0.01em
  body-md:
    fontFamily: Inter
    fontSize: 14px
    fontWeight: '400'
    lineHeight: 20px
    letterSpacing: 0.015em
  body-sm:
    fontFamily: Inter
    fontSize: 12px
    fontWeight: '400'
    lineHeight: 16px
    letterSpacing: 0.02em
  label-lg:
    fontFamily: Inter
    fontSize: 14px
    fontWeight: '500'
    lineHeight: 20px
    letterSpacing: 0.01em
  label-md:
    fontFamily: Inter
    fontSize: 12px
    fontWeight: '500'
    lineHeight: 16px
    letterSpacing: 0.02em
  label-sm:
    fontFamily: Inter
    fontSize: 11px
    fontWeight: '500'
    lineHeight: 16px
    letterSpacing: 0.03em
rounded:
  sm: 0.25rem
  DEFAULT: 0.5rem
  md: 0.75rem
  lg: 1rem
  xl: 1.5rem
  full: 9999px
spacing:
  gutter: 1rem
  gutter-mobile: 0.75rem
  margin: 1rem
  margin-tablet: 1.5rem
  space-xs: 0.25rem
  space-sm: 0.5rem
  space-md: 1rem
  space-lg: 1.5rem
  space-xl: 2rem
---

## Brand & Style
The design system embodies the focus, precision, and velocity required by modern engineering agents and developers. It fuses technical clarity with Material 3 ergonomics, establishing an uncompromising focus on glanceable hierarchy, low visual friction, and effortless one-handed touch ergonomics.

The visual style is **Corporate / Modern** anchored in contemporary native Android design patterns. It avoids skeuomorphic clutter and artificial flat abstraction in favor of structural clarity, tonal elevation, crisp typography, and high-contrast semantic labeling. The interface feels snappy, trustworthy, and systematic—encouraging uninterrupted task completion and seamless sprint execution.

## Colors
The color architecture adheres to the Material 3 dynamic color scheme, pairing a focused Electric Indigo primary with balancing teal and slate secondary accents. Surfaces avoid pure pitch black or sterile absolute white, relying on warm-tinted slate neutrals to manage eye comfort during long engineering sessions.

### Surface Elevation System
- **Light Mode**:
  - `background`: `#F8FAFC`
  - `surface`: `#FFFFFF`
  - `surface-container-low`: `#F8FAFC`
  - `surface-container`: `#F1F5F9`
  - `surface-container-high`: `#E2E8F0`
- **Dark Mode**:
  - `background`: `#0F172A`
  - `surface`: `#1E293B`
  - `surface-container-low`: `#161F30`
  - `surface-container`: `#243248`
  - `surface-container-high`: `#334155`

### Priority and Semantic Roles
Priority and status indicators must never rely exclusively on color. Every priority level couples its explicit tint with a dedicated glyph and textual tag:
- **Priority High**: `#EF4444` (Icon: Arrow Up Double / `priority_high`, Text: "P0 - High")
- **Priority Medium**: `#F59E0B` (Icon: Drag Handle / `remove`, Text: "P1 - Medium")
- **Priority Low**: `#10B981` (Icon: Arrow Down / `keyboard_arrow_down`, Text: "P2 - Low")
- **Semantic Roles**: Error `#EF4444`, Warning `#F59E0B`, Success `#10B981`, Info `#0284C7`.

## Typography
The typography system is engineered around `Inter`, providing structural clarity, robust numerical tabular figures, and exceptional micro-legibility at compact screen sizes.

- **Display & Headline Levels**: Reserved for key overview screens, sprint status dashboards, and large top app bars. They maintain tight negative tracking for punchy, confident delivery.
- **Title Levels**: Used for task card headers, bottom sheet titles, and group categorizations.
- **Body Levels**: Tuned for readability across task descriptions, pull request summaries, and issue tickets.
- **Label Levels**: Strictly mapped to interactive Material components including action buttons, input prompts, chips, and priority tags.

## Layout & Spacing
The layout relies on standard Android dp increments (multiples of 4dp and 8dp) configured within a flexible content container model.

### Touch Target Guarantees
Every interactive target conforms strictly to the 48x48dp minimum touch target bounding box (`Modifier.defaultMinSize(minWidth = 48.dp, minHeight = 48.dp)`), even when the visible container (e.g., small assist chips or icon-only actions) is compact.

### Form Factor Behavior
- **Mobile (<600dp)**: Single column with 16dp (`margin`) horizontal screen padding and 8dp–12dp card spacing. Modals and editor surfaces appear as modal bottom sheets.
- **Tablet & Foldable (600dp–840dp+)**: Dual-pane adaptive layout. Master task list occupies a permanent or rail drawer (320dp width) while task detail occupies the primary stage. Margins increase to 24dp (`margin-tablet`).

## Elevation & Depth
Depth adheres strictly to Material 3 tonal elevation paradigms rather than heavy skeuomorphic cast shadows.

- **Level 0 (Default Surface)**: Flat background `#F8FAFC` (light) or `#0F172A` (dark). Zero elevation.
- **Level 1 (Cards & List Items)**: Tinted using `surface-container-low` with an ambient blur shadow: `0dp 1dp 3dp rgba(15, 23, 42, 0.08)`. Dark mode substitutes cast shadows with a +5% surface lightness shift.
- **Level 2 (Scaffold TopAppBar on Scroll & Filter Chips)**: Tinted using `surface-container`. Clean separation achieved via a subtle 1dp border (`rgba(226, 232, 240, 0.8)` in light, `rgba(51, 65, 85, 0.8)` in dark).
- **Level 3 (Floating Action Button & Modals)**: Tinted using `surface-container-high` accompanied by `0dp 4dp 12dp rgba(79, 70, 229, 0.18)` highlight drop for primary calls-to-action.

## Shapes
The shape system matches the Material 3 corner scale, reinforcing a modern, approachable, and disciplined tool feel:

- **Extra Small (4dp)**: Status indicators, code tags, and small badges.
- **Small (8dp)**: Input text fields (`OutlinedTextField`), dropdown menus, and standard chips (`FilterChip`, `AssistChip`).
- **Medium (12dp–16dp)**: Interactive task cards (`ElevatedCard`), bottom sheet content sheets, and snackbars.
- **Large (24dp–28dp)**: Floating action buttons (FABs), search anchor bars, and alert dialogs (`Material 3 AlertDialog`).
- **Full (Pill / 9999dp)**: Priority pill badges and circular avatar containers.

## Components

### TopAppBar (Material 3 Scaffold)
- Integrated via `CenterAlignedTopAppBar` or `MediumTopAppBar`.
- Background transitions smoothly from transparent on resting state to `surface-container` upon list scroll.
- Action icons enforce 48dp touch bounds with 24dp visual icons.

### Floating Action Button (FAB)
- Uses an extended or standard container with 24dp rounded corners (`CornerRadius = 16dp–24dp`).
- Container color defaults to Primary (`#4F46E5`) with high-contrast text and icon color (`#FFFFFF`).
- Elevation rests at 3dp, scaling to 6dp on press with haptic feedback confirmation.

### FilterChip & AssistChip
- Height: 32dp with 8dp rounded corners.
- Inactive state: 1dp border in `outline-variant` (`#CBD5E1` light / `#475569` dark) over transparent/surface background.
- Selected state: Filled with `primary-container` (`#EEF2FF` light / `#312E81` dark) with active Indigo text and checkmark indicator.

### Task Cards (ElevatedCard)
- Visual structure: 12dp–16dp corner radius, 16dp internal padding.
- Anatomy: Top row contains task category label and priority chip; middle block hosts `title-md` task name and optional description (`body-md`); bottom row houses assignee avatar, due date badge, and checkbox.
- Interaction: 0.98x micro-scale press state with subtle tonal lift on touch down.

### Checkboxes & Selection Controls
- Checkboxes use an 8dp corner radius with a 2dp border stroke.
- Checked state transitions instantly to Primary Indigo fill with a white checkmark.
- Touch target extends symmetrically to 48x48dp.

### Input Fields (OutlinedTextField)
- Height: 56dp resting with 8dp corner radius.
- Active border stroke: 2dp Primary (`#4F46E5`). Inactive border: 1dp Slate (`#94A3B8`).
- Floating placeholder labels transition smoothly into `label-sm` positions. Supporting error text sits strictly 4dp below the outline in crimson (`#EF4444`).

### Material 3 AlertDialog
- Rounded corners at 28dp (`Large Shape`) with `surface-container-high` fill.
- Actions: Dismissive and affirmative buttons positioned in a right-aligned horizontal row with 8dp separation and `label-lg` typography.