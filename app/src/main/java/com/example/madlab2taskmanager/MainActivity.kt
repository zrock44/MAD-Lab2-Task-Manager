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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
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

// Main composable. This composable handles the state of both the text in the "new task" input
// and the list of tasks that the app shows.
@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    var inputText by remember { mutableStateOf("") }
    val listOfTasks = remember { mutableStateListOf<Task>() }

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
                    listOfTasks.add(Task(name = inputText.trim(), completed = false))
                    inputText = ""
                },
                buttonEnabled = if (inputText.trim() != "") true else false
            )
            TaskCount(
                taskCount = listOfTasks.size,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            TaskList(
                listOfTasks = listOfTasks
            )
        }
    }
}

// Composable that puts together TaskInput and TaskButton within a row that is within a card.
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

// The input box at the top of the app for adding a new task. This along with TaskButton are made
// to be customizable when called, because I felt like doing it that way for some reason.
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

// The button that adds a new task to the list. It is disabled when there is nothing in the
// TaskInput field or when only whitespace is present. The icon in the button is colored the same
// as the button itself when the button is disabled, to give the illusion that the icon is only
// present when the button is enabled.
@Composable
fun TaskButton(
    icon: ImageVector,
    onClick: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    OutlinedIconButton(
        onClick = onClick,
        colors = IconButtonColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            contentColor = MaterialTheme.colorScheme.tertiary,
            disabledContainerColor = MaterialTheme.colorScheme.primaryContainer,
            disabledContentColor = MaterialTheme.colorScheme.primaryContainer
        ),
        enabled = enabled,
        shape = RoundedCornerShape(0.dp),
        modifier = Modifier.size(56.dp),
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.outline)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
        )
    }
}

// This is a little extra thing I threw in. This is simply a card that contains text which shows
// a count of how many tasks are in the list.
@Composable
fun TaskCount(
    taskCount: Int,
    modifier: Modifier = Modifier
) {
    Card(
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.outline),
        modifier = modifier
    ) {
        Text(
            text = "Tasks: $taskCount",
            modifier = Modifier.padding(vertical = 3.dp, horizontal = 6.dp)
        )
    }
}

// Creates the lazy column for the task items.
@Composable
fun TaskList(
    listOfTasks: SnapshotStateList<Task>,
    modifier: Modifier = Modifier
) {
    LazyColumn {
        items(listOfTasks) {
            TaskItem(
                task = it,
                onDeleteClick = { listOfTasks.remove(it) }
            )
        }
    }
}

// Composable for each individual task item that gets listed. This handles the checkboxes in an...
// interesting way. It has a local variable named "checked" held in state for the completion status
// of the task. Each task also has a boolean property named "completed" that does the same. Checked
// is immediately set to the same value as completed, then checked is the value manipulated within
// the checkbox composable. Whenever the value is flipped, completed gets set to whatever the new
// value is. I did it this way because doing it with only a local variable or a property caused it
// to not work correctly in differing ways.
@Composable
fun TaskItem(
    task: Task,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var checked by remember { mutableStateOf(false) }
    checked = task.completed

    Card(
        modifier = modifier.padding(bottom = 8.dp),
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.outline)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = checked,
                onCheckedChange = {
                    checked = !checked
                    task.completed = checked
                },
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
                onClick = onDeleteClick
            ) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = stringResource(R.string.delete_button_description)
                )
            }
        }
    }
}

// Light and dark mode previews.
@Preview(showBackground = true)
@Composable
fun TaskManagerPreview() {
    MADLab2TaskManagerTheme(darkTheme = false) {
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