import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.time.Clock
import java.time.Instant

class ExpenseManager(private val storage: ExpenseStorage) {
    val expenses: MutableList<Expense>
        get() = storage.loadExpenses()

    fun add(description: String, amount: Double): Expense {
        val expenses = this.expenses
        val newId = nextId(expenses)

        val timestamp = Clock.System.now().toString()
        val newExpense = Expense(newId, description, timestamp, amount)
        expenses.add(newExpense)
        storage.saveExpenses(expenses)
        return newExpense
    }

    private fun nextId(expenses: List<Expense>): Int {
        return expenses.maxOfOrNull { it.id }?.plus(1) ?: 1
    }

    fun list(): String {
        val expenses = this.expenses

        if(expenses.isEmpty()) {
            return "No expenses found."
        }

        return formatTable(expenses)
    }

    private fun formatTable(list: List<Expense>): String {
        if (list.isEmpty()) return "No expenses found."

        val headers = listOf("ID", "Date", "Description", "Amount")

        val colWidths = headers.mapIndexed { i, h ->
            maxOf(h.length, list.maxOf { task ->
                when (i) {
                    0 -> task.id.toString().length
                    1 -> task.date.length
                    2 -> task.description.length
                    3 -> task.amount.toString().length
                    else -> 0
                }
            })
        }

        fun row(values: List<String>) =
            "| " + values.mapIndexed { i, v -> v.padEnd(colWidths[i]) }.joinToString(" | ") + " |"

        fun separator() =
            "+-" + colWidths.joinToString("-+-") { "-".repeat(it) } + "-+"

        val sb = StringBuilder()
        sb.appendLine(separator())
        sb.appendLine(row(headers))
        sb.appendLine(separator())
        for (expense in list) {
            val date = expense.date
            val instant = Instant.parse(date)
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val dateFormated = instant.atZone(ZoneId.systemDefault()).format(formatter)
            sb.appendLine(row(listOf(
                expense.id.toString(),
                dateFormated,
                expense.description,
                expense.amount.toString()
            )))
        }
        sb.append(separator())
        return sb.toString()
    }

    fun summary(month: Int?): String {
        val expenses = this.expenses
        if(expenses.isEmpty()) {
            return "No expenses found."
        }

        val filteredExpenses = if (month != null) {
            expenses.filter { it.date.substring(5, 7).toIntOrNull() == month }
        } else {
            expenses
        }

        if (filteredExpenses.isEmpty()) {
            return if (month != null) {
                val monthName = mapMonth(month)
                "No expenses found for $monthName."
            } else {
                "No expenses found."
            }
        }

        val totalAmount = filteredExpenses.sumOf { it.amount }

        return if (month != null) {
            val monthName = mapMonth(month)
            "Total expenses for $monthName: $$totalAmount"
        } else {
            "Total expenses: $$totalAmount"
        }
    }

    fun delete(id: Int): Expense {
        val expenses = this.expenses
        val expenseToDelete = expenses.find { it.id == id }
            ?: throw IllegalArgumentException("Expense with ID $id not found.")

        expenses.remove(expenseToDelete)
        storage.saveExpenses(expenses)
        return expenseToDelete
    }

    private fun mapMonth(monthNumber: Int): String {
        return when (monthNumber) {
            1 -> "January"
            2 -> "February"
            3 -> "March"
            4 -> "April"
            5 -> "May"
            6 -> "June"
            7 -> "July"
            8 -> "August"
            9 -> "September"
            10 -> "October"
            11 -> "November"
            12 -> "December"
            else -> "Invalid month"
        }
    }
}