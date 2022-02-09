import react.*
import react.dom.*
import kotlinx.coroutines.*
import kotlinx.html.js.onClickFunction

private val scope = MainScope()

val app = fc<Props> { _ ->
    var shoppingList by useState(emptyList<ShoppingListItem>())

    useEffectOnce {
        scope.launch {
            shoppingList = getShoppingList()
        }
    }

    h1 {
        +"Full-Stack Shopping List"
    }
    ul {
        shoppingList.sortedByDescending(ShoppingListItem::priority).forEach { item ->
            li {
                key = item.toString()
                +"[${item.priority}] ${item.desc} "
                attrs.onClickFunction = {
                    scope.launch {
                        deleteShoppingListItem(item)
                        shoppingList = getShoppingList()
                    }
                }
            }
        }
    }
    child(inputComponent) {
        attrs.onSubmit = { input ->
            val cartItem = ShoppingListItem(input.replace("!", ""), input.count { it == '!' })
            scope.launch {
                addShoppingListItem(cartItem)
                shoppingList = getShoppingList()
            }
        }
    }
}