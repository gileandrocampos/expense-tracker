import kotlinx.serialization.Serializable

@Serializable
data class Expense(
    val id: Int,
    val description: String,
    val date: String,
    var amount: Double,
) {
    init {
        require(description.isNotEmpty()) { "Description cannot be blank" }
        require(amount > 0) { "Amount must be greater than zero" }
    }
}