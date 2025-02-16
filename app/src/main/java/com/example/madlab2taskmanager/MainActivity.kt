package com.example.madlab2taskmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.madlab2taskmanager.data.Task
import com.example.madlab2taskmanager.ui.theme.MADLab2TaskManagerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MADLab2TaskManagerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    var inputText by remember { mutableStateOf("") }

    Column {
        TaskInputField(
            value = inputText,
            onValueChange = { inputText = it }
        )
        TaskItem(
            task = Task(name = "Test task", completed = false),
            onClick = {},
        )
    }
}

@Composable
fun TaskInputField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        TaskInput(
            label = R.string.input_label,
            leadingIcon = Icons.Filled.Create,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            value = value,
            onValueChange = onValueChange
        )
        TaskButton(
            // MAKE THE ICON DYNAMIC DEPENDING ON STATE OF ENABLED//////////////////////////////////
            icon = Icons.Filled.Send,
            onClick = {},
            enabled = true
        )
    }
}

@Composable
fun TaskInput(
    @StringRes label: Int,
    leadingIcon: ImageVector,
    keyboardOptions: KeyboardOptions,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        keyboardOptions = keyboardOptions,
        leadingIcon = {
            Icon(
                imageVector = leadingIcon,
                contentDescription = null
            )
        },
        label = {
            Text(
                text = stringResource(label)
            )
        }
    )
}

@Composable
fun TaskButton(
    icon: ImageVector,
    onClick: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    OutlinedIconButton(
        onClick = onClick,
        // EDIT THESE LATER/////////////////////////////////////////////////////////////////////////
        colors = IconButtonColors(
            containerColor = Color(0xFF005500),
            contentColor = Color.White,
            disabledContainerColor = Color.DarkGray,
            disabledContentColor = Color.LightGray
        ),
        enabled = enabled,
        shape = RoundedCornerShape(3.dp),
        modifier = Modifier.size(56.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null
        )
    }
}

@Composable
fun TaskList(modifier: Modifier = Modifier) {

}

@Composable
fun TaskItem(
    task: Task,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = task.completed,
                onCheckedChange = { task.completed = it }
            )
            Text(
                text = task.name
            )
            Spacer(
                modifier = Modifier.weight(1f)
            )
            IconButton(
                onClick = onClick
            ) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = stringResource(R.string.delete_button_description)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TaskManagerPreview() {
    MADLab2TaskManagerTheme {
        MainScreen()
    }
}