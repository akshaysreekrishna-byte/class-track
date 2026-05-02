---
name: Predictive Academic Pulse
colors:
  surface: '#fdf8ff'
  surface-dim: '#ddd8e3'
  surface-bright: '#fdf8ff'
  surface-container-lowest: '#ffffff'
  surface-container-low: '#f7f1fd'
  surface-container: '#f1ecf7'
  surface-container-high: '#ece6f1'
  surface-container-highest: '#e6e0eb'
  on-surface: '#1c1a22'
  on-surface-variant: '#494551'
  inverse-surface: '#312f37'
  inverse-on-surface: '#f4effa'
  outline: '#7a7582'
  outline-variant: '#cbc4d2'
  surface-tint: '#6750a4'
  primary: '#4f378a'
  on-primary: '#ffffff'
  primary-container: '#6750a4'
  on-primary-container: '#e0d2ff'
  inverse-primary: '#cfbcff'
  secondary: '#625b71'
  on-secondary: '#ffffff'
  secondary-container: '#e8def9'
  on-secondary-container: '#686177'
  tertiary: '#633b48'
  on-tertiary: '#ffffff'
  tertiary-container: '#7d5260'
  on-tertiary-container: '#ffcbda'
  error: '#ba1a1a'
  on-error: '#ffffff'
  error-container: '#ffdad6'
  on-error-container: '#93000a'
  primary-fixed: '#e9ddff'
  primary-fixed-dim: '#cfbcff'
  on-primary-fixed: '#22005d'
  on-primary-fixed-variant: '#4f378a'
  secondary-fixed: '#e8def9'
  secondary-fixed-dim: '#ccc2dc'
  on-secondary-fixed: '#1e192b'
  on-secondary-fixed-variant: '#4a4358'
  tertiary-fixed: '#ffd9e3'
  tertiary-fixed-dim: '#eeb8c8'
  on-tertiary-fixed: '#31111d'
  on-tertiary-fixed-variant: '#633b48'
  background: '#fdf8ff'
  on-background: '#1c1a22'
  surface-variant: '#e6e0eb'
typography:
  display-lg:
    fontFamily: Lexend
    fontSize: 57px
    fontWeight: '400'
    lineHeight: 64px
    letterSpacing: -0.25px
  headline-lg:
    fontFamily: Lexend
    fontSize: 32px
    fontWeight: '600'
    lineHeight: 40px
  headline-md:
    fontFamily: Lexend
    fontSize: 28px
    fontWeight: '600'
    lineHeight: 36px
  title-lg:
    fontFamily: Lexend
    fontSize: 22px
    fontWeight: '500'
    lineHeight: 28px
  title-md:
    fontFamily: Inter
    fontSize: 16px
    fontWeight: '500'
    lineHeight: 24px
    letterSpacing: 0.15px
  body-lg:
    fontFamily: Inter
    fontSize: 16px
    fontWeight: '400'
    lineHeight: 24px
    letterSpacing: 0.5px
  body-md:
    fontFamily: Inter
    fontSize: 14px
    fontWeight: '400'
    lineHeight: 20px
    letterSpacing: 0.25px
  label-lg:
    fontFamily: Inter
    fontSize: 14px
    fontWeight: '500'
    lineHeight: 20px
    letterSpacing: 0.1px
  label-sm:
    fontFamily: Inter
    fontSize: 11px
    fontWeight: '500'
    lineHeight: 16px
    letterSpacing: 0.5px
rounded:
  sm: 0.25rem
  DEFAULT: 0.5rem
  md: 0.75rem
  lg: 1rem
  xl: 1.5rem
  full: 9999px
spacing:
  base: 4px
  xs: 4px
  sm: 8px
  md: 16px
  lg: 24px
  xl: 32px
  margin-mobile: 16px
  gutter: 16px
---

## Brand & Style

The design system is rooted in a **Modern Corporate** aesthetic, heavily influenced by Google’s Material Design 3 (Material You) philosophy. It aims to evoke a sense of reliability and foresight, positioning the app not just as a tracker, but as a predictive companion for university students. 

The visual language balances the "efficiency" of a productivity tool with the "modern" sensibilities of a student-centric app. It utilizes high-contrast semantic states to drive quick decision-making. The interaction model is fluid and responsive, emphasizing a "predictive" nature through the use of prominent data visualizations and smart status updates.

## Colors

This design system employs a dynamic color palette that prioritizes functional clarity through semantic color-coding. 

- **Primary & Secondary:** Follow standard M3 tonal palettes, allowing for system-level personalization (Material You).
- **Success (Green):** Indicates "Safe to Bunk." Used when attendance is well above the required threshold.
- **Error/Critical (Red):** Indicates "Do Not Bunk." Used when attendance is at or below the critical threshold.
- **Warning (Yellow):** Indicates "Cancelled" or "Borderline." Used for class cancellations or when one more absence will trigger a critical state.
- **Neutral (Grey):** Indicates "Pending" or "Upcoming" status.

The use of color should be purposeful: semantic colors are applied to progress rings, status icons, and card accents to provide instant cognitive recognition of attendance health.

## Typography

The typography system uses **Lexend** for headlines and KPIs to leverage its high readability and "active" personality, ideal for quick data scanning. **Inter** is utilized for body text and labels to maintain a clean, functional UI.

- **KPI Hierarchy:** Numerical attendance data (percentages) should use `headline-lg` or `display-lg` to anchor the dashboard.
- **Predictive Text:** Subtle labels such as "You can skip 2 more classes" should use `label-lg` with semantic coloring to ensure they stand out as actionable insights.

## Layout & Spacing

This design system utilizes a **Fluid Grid** based on an 8pt rhythm, optimized for mobile viewports.

- **Grid Model:** A 4-column layout for mobile devices with 16px side margins and 16px gutters.
- **Rhythm:** Spacing follows a strict base-4 increment. Use `md` (16px) for standard padding within cards and `lg` (24px) for vertical section spacing.
- **Predictive Cards:** These should span the full width of the 4-column grid to maximize the visibility of progress indicators and CTA buttons.

## Elevation & Depth

In alignment with Material 3, depth is expressed through **Tonal Layers** and **Ambient Shadows**.

- **Surface Levels:** The background sits at Level 0. Elevated cards use a light shadow (Elevation 1) and a primary-tinted surface overlay to indicate interactability.
- **Outlined Cards:** Used for secondary information or individual class logs to prevent visual clutter. These utilize a 1px stroke in the `neutral_variant` color.
- **FAB & Navigation:** The Floating Action Button occupies the highest elevation (Elevation 3) to remain prominent above scrolling content. The Bottom Navigation bar uses a slight tonal elevation to separate it from the content area.

## Shapes

The design system follows a **Rounded** shape language to feel approachable yet modern.

- **Cards:** Use `rounded-lg` (16px) to create a soft, contained look for class data.
- **Buttons & FAB:** Standard buttons use a fully rounded (pill) shape, while the FAB utilizes the M3 standard `rounded-xl` (24px) for a distinctive "squircle" appearance.
- **Progress Indicators:** Circular indicators should maintain a clean, geometric stroke without rounded caps to emphasize precision.

## Components

- **Elevated & Outlined Cards:** Use Elevated cards for the "Daily Summary" or "High Priority" classes. Use Outlined cards for the general schedule to maintain hierarchy.
- **Circular Progress Indicators:** Centralized in subject cards. The stroke color must map to the semantic states (Green/Yellow/Red) based on the calculated attendance percentage.
- **Bottom Navigation Bar:** Contains four destinations: Home, Schedule, Analytics, and Profile. Uses active indicator pills for the selected state.
- **Floating Action Button (FAB):** A large, primary-colored FAB for "Quick Check-in." Include a small icon + text label (Extended FAB) when at the top of the feed.
- **Location-based Icons:** Small status chips within class cards indicating "In-range" (verified by GPS) or "Remote" class types, using the `label-sm` style.
- **Attendance Chips:** Small, interactive chips to toggle status (Present, Absent, Excused) with immediate color feedback.