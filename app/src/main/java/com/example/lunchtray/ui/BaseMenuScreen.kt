package com.example.lunchtray.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.example.lunchtray.R
import com.example.lunchtray.model.MenuItem

@Composable
fun BaseMenuScreen(
    options: List<MenuItem>,
    modifier: Modifier = Modifier,
    onCancelButtonClicked: () -> Unit = {},
    onNextButtonClicked: () -> Unit = {},
    onSelectionChanged: (MenuItem) -> Unit
) {
    var selectedItem by rememberSaveable { mutableStateOf<MenuItem?>(null) }

    Column(modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium))) {
        options.forEach { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = selectedItem?.name == item.name,
                        onClick = {
                            selectedItem = item
                            onSelectionChanged(item)
                        }
                    )
                    .padding(vertical = dimensionResource(id = R.dimen.padding_small)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selectedItem?.name == item.name,
                    onClick = {
                        selectedItem = item
                        onSelectionChanged(item)
                    }
                )
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.padding_small)))
                Column {
                    Text(text = item.name, style = MaterialTheme.typography.headlineSmall)
                    Text(text = item.description, style = MaterialTheme.typography.bodyLarge)
                    Text(text = item.getFormattedPrice(), style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_medium)))
        MenuScreenButtonGroup(
            selectedItemName = selectedItem?.name ?: "",
            onCancelButtonClicked = onCancelButtonClicked,
            onNextButtonClicked = onNextButtonClicked,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun MenuScreenButtonGroup(
    selectedItemName: String,
    onCancelButtonClicked: () -> Unit,
    onNextButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
    ) {
        OutlinedButton(
            modifier = Modifier.weight(1f),
            onClick = onCancelButtonClicked
        ) {
            Text(text = stringResource(id = R.string.cancel).uppercase())
        }
        Button(
            modifier = Modifier.weight(1f),
            enabled = selectedItemName.isNotEmpty(),
            onClick = onNextButtonClicked
        ) {
            Text(text = stringResource(id = R.string.next).uppercase())
        }
    }
}
