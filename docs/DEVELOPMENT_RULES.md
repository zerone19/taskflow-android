# Development Rules

## General

- Understand before changing.
- Inspect before implementing.
- Plan before large changes.
- Keep changes focused.
- Prefer the smallest correct change.
- Do not modify unrelated files.

## Kotlin

- Prefer immutable data.
- Use `val` by default.
- Avoid unnecessary `!!`.
- Use sealed types/enums/data classes where appropriate.
- Use coroutines with structured concurrency.

## Compose

- Prefer stateless reusable composables.
- Hoist state appropriately.
- Keep business logic out of composables.
- Use Material 3 and project design tokens.
- Avoid excessive `remember` and side effects.

## Gradle

- Do not randomly change AGP, Kotlin, Gradle, SDK, or dependency versions.
- Add dependencies only when justified.
- Verify compatibility before major build configuration changes.

## Git

Prefer focused commits such as:

- `chore: initialize project`
- `feat: add task list`
- `fix: handle empty task state`
- `test: add task viewmodel tests`
- `refactor: extract task card`

Do not rewrite Git history or perform destructive Git operations without explicit authorization.

## Verification

Never claim a result that has not been verified.
