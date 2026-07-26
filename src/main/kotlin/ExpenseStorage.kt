import java.io.File
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ExpenseStorage {
    private val appDir = File(System.getProperty("user.dir"))
    private val expenseFile = File(appDir, "data/expenses.json")
    private val jsonFormat = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
    }

    val file: File
        get() {
            expenseFile.parentFile.mkdirs()
            if (!expenseFile.exists() || expenseFile.length() == 0L) {
                expenseFile.writeText("[]")
            }
            return expenseFile
        }

    fun loadExpenses(): MutableList<Expense> {
        if (!file.exists()) {
            return mutableListOf()
        }

        return jsonFormat.decodeFromString(file.readText())
    }

    fun saveExpenses(expenses: List<Expense>) {
        val serializedExpenses = jsonFormat.encodeToString(expenses)
        file.writeText(serializedExpenses)
    }
}