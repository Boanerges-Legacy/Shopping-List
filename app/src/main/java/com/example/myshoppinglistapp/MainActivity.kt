package com.example.myshoppinglistapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myshoppinglistapp.ui.theme.MyShoppingListAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyShoppingListAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ShoppingList(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

// ─────────────────────────────────────────────
// SHOPPING LIST SCREEN
// ─────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingList(modifier: Modifier = Modifier) {
    var shoppingList by remember { mutableStateOf(listOf<ShoppingItem>()) }
    var showDialog by remember { mutableStateOf(false) }
    var newItemName by remember { mutableStateOf("") }
    var newItemQuantity by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(text = "Shopping List App")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { showDialog = true }) {
            Text(text = "Add Item")
        }
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(shoppingList, key = { it.id }) { item ->
                if (item.isEditing) {
                    ShoppingItemEditor(
                        item = item,
                        onEditComplete = { editedName, editedQuantity ->
                            // FIX #4 & #5: removed trailing comma; now correctly applies
                            // edited values via copy() instead of ignoring them
                            shoppingList = shoppingList.map {
                                if (it.id == item.id)
                                    it.copy(name = editedName, quantity = editedQuantity, isEditing = false)
                                else
                                    it
                            }
                        }
                    )
                } else {
                    // FIX #6: AddShoppingItem now only shown when NOT editing
                    AddShoppingItem(
                        item = item,
                        onEditClick = {
                            shoppingList = shoppingList.map { it.copy(isEditing = it.id == item.id) }
                        },
                        onDeleteClick = {
                            shoppingList = shoppingList - item
                        }
                    )
                }
            }
        }
    }

    if (showDialog) {
        BasicAlertDialog(onDismissRequest = { showDialog = false }) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Text("Add Shopping Item", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = newItemName,
                    onValueChange = { newItemName = it },
                    label = { Text("Item Name") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = newItemQuantity,
                    onValueChange = { newItemQuantity = it },
                    label = { Text("Quantity") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(onClick = { showDialog = false }) {
                        Text("Cancel")
                    }
                    Button(
                        onClick = {
                            if (newItemName.isNotBlank() && newItemQuantity.isNotBlank()) {
                                shoppingList = shoppingList + ShoppingItem(
                                    id = shoppingList.size + 1,
                                    name = newItemName,
                                    quantity = newItemQuantity.toIntOrNull() ?: 1
                                )
                                newItemName = ""
                                newItemQuantity = ""
                                showDialog = false
                            }
                        }
                    ) {
                        Text("Add")
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────
// PREVIEW
// ─────────────────────────────────────────────

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyShoppingListAppTheme {
        ShoppingList()
    }
}

