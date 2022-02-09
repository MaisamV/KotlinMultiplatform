import io.ktor.http.*
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import kotlinx.browser.window

val endpoint = window.location.origin // only needed until https://youtrack.jetbrains.com/issue/KTOR-453 is resolved. fixed in 2.0.0

val jsonClient = HttpClient {
    install(JsonFeature) { serializer = KotlinxSerializer() }
}

suspend fun getShoppingList(): List<ShoppingListItem> {
    val list = jsonClient.get<List<ShoppingListItem>>(endpoint + ShoppingListItem.path)
    println(list.size)
    return list
}

suspend fun addShoppingListItem(shoppingListItem: ShoppingListItem) {
    jsonClient.post<Unit>(endpoint + ShoppingListItem.path) {
        contentType(ContentType.Application.Json)
        body = shoppingListItem
    }
}

suspend fun deleteShoppingListItem(shoppingListItem: ShoppingListItem) {
    jsonClient.delete<Unit>(endpoint + ShoppingListItem.path + "/${shoppingListItem.id}")
}