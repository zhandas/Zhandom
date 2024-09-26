package com.example.shoppinglisttwooseven

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MainScreen(){

    var index by remember {
        mutableIntStateOf(1)
    }

    var listOfShoppingItems by remember {
        mutableStateOf(listOf<ShoppingItem>())
    }

    var isAdding by remember {
        mutableStateOf(false)
    }

    if (isAdding){
        AddDialog(
            index = index,
          onDismissRequest = {
              isAdding = false
          },
            onAddClicked = {
                listOfShoppingItems += it
                index++
                isAdding = false
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(red = 209, green = 89, blue = 246))
            .padding(top = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            modifier = Modifier.fillMaxWidth().background(Color(red = 236, green = 89, blue = 246)),
            onClick = {
                isAdding = true
            }
        ) {
            Text("Добавить")
        }
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(listOfShoppingItems){
                    element ->
                if (element.isEditing){
                    EditShoppingItemCard(
                        shoppingItem = element,
                        onEditCompleted = { name, description, quantity ->
                            listOfShoppingItems = listOfShoppingItems.map { it.copy(isEditing = false) }
                            val editedElement = listOfShoppingItems.find { it.id == element.id }
                            editedElement?.let {
                                it.title = name
                                it.description = description
                                it.quantity = quantity
                            }
                        },
                        onDismissRequest = {element.isEditing = false}
                    )
                }
                else{
                    ShoppingItemCard(
                        shoppingItem = element,
                        onEditClicked = {
                            listOfShoppingItems = listOfShoppingItems.map { el-> el.copy(isEditing = el.id == element.id) }
                        },
                        onDeleteClicked = {listOfShoppingItems -= it}
                    )
                }
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDialog(index: Int, onDismissRequest: () -> Unit, onAddClicked: (ShoppingItem) -> Unit){

    var title by remember {
        mutableStateOf("")
    }

    var description by remember {
        mutableStateOf("")
    }

    var quantity by remember {
        mutableIntStateOf(1)
    }

    BasicAlertDialog(
        modifier = Modifier.background(
            color = Color.White,
            shape = RoundedCornerShape(8.dp)
        ),
        onDismissRequest = onDismissRequest
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(8.dp)
                ),
        ){
            TextField(
                placeholder = { Text("Title") },
                value = title,
                onValueChange = {
                    title = it
                }
            )
            Spacer(Modifier.height(8.dp))
            TextField(
                placeholder = { Text("Description") },
                value = description,
                onValueChange = {
                    description = it
                }
            )
            Spacer(Modifier.height(8.dp))
            TextField(
                placeholder = { Text("Description") },
                value = quantity.toString(),
                onValueChange = {
                    quantity = it.toInt()
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        onDismissRequest()
                    }
                ) {
                    Text("Cancel")
                }
                Button(
                    onClick = {
                        onAddClicked(
                            ShoppingItem(
                                id = index,
                                title = title,
                                description = description,
                                quantity = quantity
                            )
                        )
                        onDismissRequest()
                    }
                ) {
                    Text("Add")
                }
            }
        }
    }
}

@Composable
fun ShoppingItemCard(shoppingItem: ShoppingItem, onEditClicked: () -> Unit, onDeleteClicked: (ShoppingItem) -> Unit){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().background(Color(red = 252, green = 113, blue = 243)),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .weight(1f)
            ) {
                Text(
                    text = shoppingItem.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = shoppingItem.description,
                    fontSize = 12.sp,
                    color = Color.Black
                )
            }
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .weight(0.4f)
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Quantity:",
                    textAlign = TextAlign.End,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = shoppingItem.quantity.toString(),
                    textAlign = TextAlign.End,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
            Column {
                IconButton(onClick = onEditClicked) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "")
                }
                IconButton(onClick = { onDeleteClicked(shoppingItem) }) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditShoppingItemCard(
    shoppingItem: ShoppingItem,
    onEditCompleted: (String, String, Int) -> Unit,
    onDismissRequest: () -> Unit
){
    var editedName by remember {
        mutableStateOf(shoppingItem.title)
    }

    var editedDescription by remember {
        mutableStateOf(shoppingItem.description)
    }

    var editedQuantity by remember {
        mutableIntStateOf(shoppingItem.quantity)
    }

    BasicAlertDialog(
        modifier = Modifier.background(
            color = Color.White,
            shape = RoundedCornerShape(8.dp)
        ),
        onDismissRequest = onDismissRequest
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(8.dp)
                ),
        ){
            TextField(
                placeholder = { Text("Title") },
                value = editedName,
                onValueChange = {
                    editedName = it
                }
            )
            Spacer(Modifier.height(8.dp))
            TextField(
                placeholder = { Text("Description") },
                value = editedDescription,
                onValueChange = {
                    editedDescription = it
                }
            )
            Spacer(Modifier.height(8.dp))
            TextField(
                placeholder = { Text("Description") },
                value = editedQuantity.toString(),
                onValueChange = {
                    editedQuantity = it.toInt()
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        onDismissRequest()
                    }
                ) {
                    Text("Cancel")
                }
                Button(
                    onClick = {
                        onEditCompleted(editedName, editedDescription, editedQuantity)
                        onDismissRequest()
                    }
                ) {
                    Text("Edit")
                }
            }
        }
    }
}

@Preview
@Composable
fun Preview(){
    ShoppingItemCard(ShoppingItem(1, "1", "1", 1), onEditClicked = {}) { }
}