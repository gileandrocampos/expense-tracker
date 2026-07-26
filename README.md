# 💸 Expense Tracker

A simple command-line expense manager written in **Kotlin**. It lets you add, list, remove, and summarize expenses, with persistence to a local JSON file.

Project based on the [Expense Tracker](https://roadmap.sh/projects/expense-tracker) challenge from roadmap.sh.

## ✨ Features

- ➕ Add expenses with a description and amount
- 📋 List all expenses in a formatted table
- 🗑️ Delete expenses by ID
- 📊 Generate a summary of total expenses (overall or filtered by month)
- 💾 Automatic persistence to a JSON file (`data/expenses.json`)

## 🧰 Tech Stack

- [Kotlin](https://kotlinlang.org/) 2.3.0 (JVM)
- [Gradle](https://gradle.org/) with Kotlin DSL
- [kotlinx.serialization](https://github.com/Kotlin/kotlinx.serialization) for reading/writing JSON
- JDK 21 (via `jvmToolchain`)

## 📂 Project Structure

```
expense-tracker/
├── build.gradle.kts
├── settings.gradle.kts
├── data/
│   └── expenses.json        # Persistence file (created automatically)
└── src/
    └── main/
        └── kotlin/
            ├── Expense.kt          # Expense data model
            ├── ExpenseManager.kt   # Business logic (add, list, delete, summary)
            ├── ExpenseStorage.kt   # JSON read/write handling
            └── Main.kt             # Entry point and CLI argument parsing
```

## ✅ Prerequisites

- JDK 21 installed
- No need to install Gradle manually — the project already includes the **Gradle Wrapper** (`gradlew` / `gradlew.bat`)

## 🚀 Getting Started

Clone the repository and run commands through the Gradle Wrapper from the project root:

```bash
git clone https://github.com/your-username/expense-tracker.git
cd expense-tracker
```

### Add an expense

```bash
./gradlew run --args='add --description "github copilot" --amount 50'
```

Expected output:

```
Expense added successfully with ID: 1
```

### List expenses

```bash
./gradlew run --args='list'
```

Expected output:

```
+----+------------+------------------+--------+
| ID | Date       | Description      | Amount |
+----+------------+------------------+--------+
| 1  | 2026-07-26 | github copilot   | 50.0   |
+----+------------+------------------+--------+
```

### Delete an expense

```bash
./gradlew run --args='delete --id 1'
```

Expected output:

```
Expense deleted successfully: 1
```

### View expense summary

Overall summary:

```bash
./gradlew run --args='summary'
```

Summary filtered by month (1 to 12):

```bash
./gradlew run --args='summary --month 7'
```

Expected output:

```
Total expenses for July: $50.0
```

### View all available commands

```bash
./gradlew run --args='commands'
```

Expected output:

```
Available commands:
- add --description "example" --amount 20: Adds a new expense with the given description.
- delete --id <id>: Removes an expense;
- list: Lists all expenses.
- summary <month>: Shows the expense summary, optionally filtered by month (1-12).
- commands: Shows the list of available commands.
```

## 📖 Command Reference

| Command    | Arguments                                     | Description                                                |
|------------|------------------------------------------------|-------------------------------------------------------------|
| `add`      | `--description "<text>"` `--amount <value>`    | Adds a new expense                                           |
| `list`     | —                                                | Lists all expenses in a table                                |
| `delete`   | `--id <id>`                                     | Removes the expense with the given ID                        |
| `summary`  | `--month <1-12>` *(optional)*                   | Shows the total expenses, overall or filtered by month        |
| `commands` | —                                                | Shows the list of available commands                          |

## 💾 Data Persistence

Expenses are stored in `data/expenses.json`, in the project's working directory. The file is created automatically on the first run if it doesn't already exist.

Example content:

```json
[
    {
        "id": 1,
        "description": "github copilot",
        "date": "2026-07-26T14:32:10.123456Z",
        "amount": 50.0
    }
]
```

## ⚠️ Validation Rules

- The expense description cannot be blank.
- The expense amount must be greater than zero.
- Trying to delete an expense with a non-existent ID throws an error (`Expense with ID <id> not found.`).