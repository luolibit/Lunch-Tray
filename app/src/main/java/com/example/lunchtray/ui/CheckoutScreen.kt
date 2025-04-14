package com.example.lunchtray.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.lunchtray.R
import com.example.lunchtray.datasource.DataSource
import com.example.lunchtray.model.MenuItem
import com.example.lunchtray.model.OrderUiState

@Composable
fun CheckoutScreen(
    orderUiState: OrderUiState,
    onNextButtonClicked: () -> Unit,
    onCancelButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(dimensionResource(id = R.dimen.padding_medium))
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_small))
    ) {
        Text(
            text = stringResource(id = R.string.order_summary),
            fontWeight = FontWeight.Bold
        )
        ItemSummary(item = orderUiState.entree, modifier = Modifier.fillMaxWidth())
        ItemSummary(item = orderUiState.sideDish, modifier = Modifier.fillMaxWidth())
        ItemSummary(item = orderUiState.accompaniment, modifier = Modifier.fillMaxWidth())

        Divider(
            thickness = dimensionResource(id = R.dimen.thickness_divider),
            modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.padding_small))
        )

        OrderSubCost(
            resourceId = R.string.subtotal,
            price = orderUiState.itemTotalPrice.formatPrice(),
            modifier = Modifier.align(Alignment.End)
        )
        OrderSubCost(
            resourceId = R.string.tax,
            price = orderUiState.orderTax.formatPrice(),
            modifier = Modifier.align(Alignment.End)
        )
        Text(
            text = stringResource(id = R.string.total, orderUiState.orderTotalPrice.formatPrice()),
            modifier = Modifier.align(Alignment.End),
            fontWeight = FontWeight.Bold
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.padding_medium)),
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
        ) {
            OutlinedButton(modifier = Modifier.weight(1f), onClick = onCancelButtonClicked) {
                Text(stringResource(id = R.string.cancel).uppercase())
            }
            Button(
                modifier = Modifier.weight(1f),
                onClick = onNextButtonClicked
            ) {
                Text(stringResource(id = R.string.submit).uppercase())
            }
        }
    }
}

@Composable
fun ItemSummary(
    item: MenuItem?,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = item?.name ?: "")
        Text(text = item?.getFormattedPrice() ?: "")
    }
}

@Composable
fun OrderSubCost(
    @StringRes resourceId: Int,
    price: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = stringResource(id = resourceId, price),
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun CheckoutScreenPreview() {
    CheckoutScreen(
        orderUiState = OrderUiState(
            entree = DataSource.entreeMenuItems[0],
            sideDish = DataSource.sideDishMenuItems[0],
            accompaniment = DataSource.accompanimentMenuItems[0],
            itemTotalPrice = 15.00,
            orderTax = 1.00,
            orderTotalPrice = 16.00
        ),
        onNextButtonClicked = {},
        onCancelButtonClicked = {},
        modifier = Modifier
            .padding(dimensionResource(id = R.dimen.padding_medium))
            .verticalScroll(rememberScrollState())
    )
}
