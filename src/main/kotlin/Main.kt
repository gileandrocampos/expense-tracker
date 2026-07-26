fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("Nenhum comando fornecido, digite commands")
        return
    }

    val command = args[0].lowercase()
    val storage = ExpenseStorage()
    val manager = ExpenseManager(storage)

    val commandLine = args.joinToString(" ")

    when (command) {
        "add" -> {
            val description = extractArgument(commandLine, """--description\s+(?:"([^"]+)"|([^-]+))""")
            val amount = extractArgument(commandLine, """--amount\s+([0-9]+(?:\.[0-9]+)?)""")?.toDouble()

            if(amount == null) {
                println(getAvailableCommands())
                return
            }

            if(description == null) {
                println(getAvailableCommands())
                return
            }

            val result = manager.add(
                description = description,
                amount = amount
            )

            println("Expense added successfully with ID: ${result.id}")
            return
        }

        "list" -> {
            val result = manager.list()
            println(result)
            return
        }

        "delete" -> {
            val id = extractArgument(commandLine, """--id\s+([0-9]+)""")?.toIntOrNull()
            if(id == null) {
                println(getAvailableCommands())
                return
            }
            val result = manager.delete(id)
            println("Expense deleted successfully: ${result.id}")
            return
        }

        "summary" -> {
            val month = extractArgument(commandLine, """--month\s+([0-9]{1,2})""")?.toIntOrNull()
            if(month == null) {
                val result = manager.summary(null)
                println(result)
                return
            }
            val result = manager.summary(month)
            println(result)
            return
        }

        "commands" -> println(getAvailableCommands())
    }
}

private fun extractArgument(commandLine: String, regexPattern: String): String? {
    val match = regexPattern.toRegex().find(commandLine) ?: return null

    val extractedValue = match.groupValues.getOrNull(1)?.takeIf { it.isNotEmpty() }
        ?: match.groupValues.getOrNull(2)

    return extractedValue?.trim()
}

private fun getAvailableCommands(): String = """
    Comandos disponíveis:
    - add --description "example" --amount 20: Adiciona uma nova despesa com a descrição fornecida.
    - delete --id <id>: Remove uma despesa;
    - list: Lista todas as despesas.
    - summary <month>: Exibe o resumo das despesas, opcionalmente filtrando por mês (passando o número do mês 1-12).
    - commands: Exibe a lista de comandos disponíveis.
""".trimIndent()
