package com.example.myshoppinglistapp

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ShoppingListItem(
    item: ShoppingItem,
    modifier: Modifier = Modifier,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border( border = BorderStroke(1.dp, Color.Blue),
                shape = RoundedCornerShape(8.dp))
    ) {
        Text(
            text = "${item.name} (x${item.quantity})",
            modifier = Modifier.padding( 8.dp)
        )
    }
}