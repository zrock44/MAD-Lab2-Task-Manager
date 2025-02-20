package com.example.madlab2taskmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
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
                MainScreen(Modifier.background(MaterialTheme.colorScheme.background))
            }
        }
    }
}

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    var inputText by remember { mutableStateOf("") }
    val listOfTasks = remember { mutableStateListOf<Task>() }

    //FOR TESTING///////////////////////////////////////////////////////////////////////////////////
    var counter by remember { mutableIntStateOf(0) }
    if(counter == 0) {
        listOfTasks.add(Task(name = "Test task 1. Tell me what it is you think you know. You think that impresses me? Not so, my friend. Not so."))
        listOfTasks.add(Task(name = "Test task 2"))
        counter++
    }

    Surface(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column {
            TaskInputField(
                value = inputText,
                onValueChange = { inputText = it },
                onAddButtonClick = {
                    listOfTasks.add(Task(name = inputText.trim()))
                    inputText = ""
                },
                buttonEnabled = if (inputText.trim() != "") true else false
            )
            Spacer(
                modifier = Modifier.size(width = 0.dp, height = 32.dp)
            )
            TaskList(
                taskList = listOfTasks,
                onDeleteClick = {  }
            )
        }
    }
}

@Composable
fun TaskInputField(
    value: String,
    onValueChange: (String) -> Unit,
    onAddButtonClick: () -> Unit,
    buttonEnabled: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        //COLOR USED////////////////////////////////////////////////////////////////////////////////
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.outline)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            TaskInput(
                label = R.string.input_label,
                leadingIcon = Icons.Filled.Create,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.weight(1f)
            )
            TaskButton(
                icon = Icons.Filled.Add,
                onClick = onAddButtonClick,
                enabled = buttonEnabled
            )
        }
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
        },
        modifier = modifier
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
        //COLOR USED////////////////////////////////////////////////////////////////////////////////
        colors = IconButtonColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            contentColor = MaterialTheme.colorScheme.tertiary,
            disabledContainerColor = MaterialTheme.colorScheme.primaryContainer,
            disabledContentColor = MaterialTheme.colorScheme.primaryContainer
        ),
        enabled = enabled,
        shape = RoundedCornerShape(0.dp),
        modifier = Modifier.size(56.dp),
        //COLOR USED////////////////////////////////////////////////////////////////////////////////
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.outline)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
        )
    }
}

@Composable
fun TaskList(taskList: List<Task>, onDeleteClick: () -> Unit, modifier: Modifier = Modifier) {
    LazyColumn {
        items(taskList) {
            TaskItem(
                task = it,
                onDeleteClick = {  }
            )
        }
    }
}

@Composable
fun TaskItem(
    task: Task,
    onDeleteClick: (Task) -> Unit,
    modifier: Modifier = Modifier
) {
    var checked by remember { mutableStateOf(false) }

    Card(
        modifier = modifier.padding(bottom = 8.dp),
        //COLOR USED////////////////////////////////////////////////////////////////////////////////
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.outline)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = checked,
                onCheckedChange = { checked = !checked },
                colors = CheckboxColors(
                    checkedCheckmarkColor = MaterialTheme.colorScheme.tertiary,
                    checkedBoxColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                    checkedBorderColor = MaterialTheme.colorScheme.tertiary,
                    uncheckedCheckmarkColor = MaterialTheme.colorScheme.outline,
                    uncheckedBoxColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                    uncheckedBorderColor = MaterialTheme.colorScheme.outline,
                    disabledCheckedBoxColor = MaterialTheme.colorScheme.background,
                    disabledUncheckedBoxColor = MaterialTheme.colorScheme.background,
                    disabledIndeterminateBoxColor = MaterialTheme.colorScheme.background,
                    disabledBorderColor = MaterialTheme.colorScheme.background,
                    disabledUncheckedBorderColor = MaterialTheme.colorScheme.background,
                    disabledIndeterminateBorderColor = MaterialTheme.colorScheme.background
                )
            )
            Text(
                text = task.name,
                textDecoration = if(checked) TextDecoration.LineThrough else null,
                color = if(checked) {
                    MaterialTheme.colorScheme.outline
                } else {
                    MaterialTheme.colorScheme.onSurface
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 8.dp)
            )
            Spacer(
                modifier = Modifier.weight(0.01f)
            )
            IconButton(
                onClick = { onDeleteClick }
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
        MainScreen(Modifier.background(MaterialTheme.colorScheme.background))
    }
}

@Preview(showBackground = true)
@Composable
fun TaskManagerPreviewDarkMode() {
    MADLab2TaskManagerTheme(darkTheme = true) {
        MainScreen(Modifier.background(MaterialTheme.colorScheme.background))
    }
}