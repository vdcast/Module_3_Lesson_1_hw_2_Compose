package com.module_3_lesson_1_hw_2_compose

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.module_3_lesson_1_hw_2_compose.ui.theme.Mint90
import com.module_3_lesson_1_hw_2_compose.ui.theme.Module_3_Lesson_1_hw_2_ComposeTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {

    private val petsState =  mutableStateOf(emptyList<Pet>())
    private val textFieldValueLeftSearchBarState = mutableStateOf("Search")
    private val textFieldValueRightSearchBarState = mutableStateOf("Search")


    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Module_3_Lesson_1_hw_2_ComposeTheme {


                val focusManager = LocalFocusManager.current


                LaunchedEffect(Unit) {
                    val tasks = withContext(Dispatchers.IO) {


                        val db = App.instance.database
                        val petDao = db.petDao
                        val pets = petDao.allPet


                        if (pets.isNotEmpty()){
                            Log.d("MYLOG", "DB loaded 1.")

                            petsState.value = pets
                        } else {
                            Log.d("MYLOG", "DB is empty.")
                        }




                    }
                }




                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Row(){
                        TextField(
                            value = textFieldValueLeftSearchBarState.value,
                            onValueChange = {newValue ->

                                textFieldValueLeftSearchBarState.value = newValue

                                if (newValue == ""){
                                    lifecycleScope.launch(Dispatchers.IO){
                                        updateTasksState()
                                    }
                                } else {
                                    lifecycleScope.launch(Dispatchers.IO){
                                        searchByName(textFieldValueLeftSearchBarState.value)
                                    }
                                }
                            },
                            maxLines = 1,
                            modifier = Modifier.fillMaxWidth(0.5f),
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    focusManager.clearFocus()
                                }
                            ),
                        )
                        TextField(
                            value = textFieldValueRightSearchBarState.value,
                            onValueChange = {newValue ->

                                textFieldValueRightSearchBarState.value = newValue

                                if (newValue == ""){
                                    lifecycleScope.launch(Dispatchers.IO){
                                        updateTasksState()
                                    }
                                } else {
                                    lifecycleScope.launch(Dispatchers.IO){
                                        searchByType(textFieldValueRightSearchBarState.value)
                                    }
                                }
                            },
                            maxLines = 1,
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    focusManager.clearFocus()
                                }
                            ),
                        )
                    }

                    
                    LazyColumn(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 72.dp)
                    ) {
                        itemsIndexed(petsState.value){ index, pet ->

                            val textFieldValueNameState = remember { mutableStateOf(pet.name) }
                            val textFieldValueHeightState = remember { mutableStateOf(pet.height) }
                            val textFieldValueWeightState = remember { mutableStateOf(pet.weight) }
                            val isEditingState = remember { mutableStateOf(false) }


                            val petsTypesList = listOf(
                                "Dog", "Cat", "GOAT", "Rabbit", "Chicken", "Cow",
                                "Hamster", "Parrot", "Turtle", "Guinea Pig", "Fish",
                            )
                            var selectedItemType by rememberSaveable() { mutableStateOf(pet.type) }

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(if (isEditingState.value) 288.dp else 96.dp)
                                    .padding(start = 8.dp, end = 8.dp, top = 4.dp, bottom = 4.dp),
                                shape = RoundedCornerShape(16.dp),
                                elevation = CardDefaults.cardElevation(
                                    defaultElevation = 5.dp
                                ),
                                onClick = {
                                    if (isEditingState.value) {
                                        isEditingState.value = false
                                        lifecycleScope.launch(Dispatchers.IO){
                                            saveFromEdit(
                                                pet.id,
                                                textFieldValueNameState.value,
                                                selectedItemType,
                                                textFieldValueHeightState.value,
                                                textFieldValueWeightState.value
                                            )
                                            updateTasksState()
                                        }
                                    } else {
                                        isEditingState.value = true
                                    }
                                }
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxHeight()
                                ) {
                                    if (isEditingState.value){
                                        Column(
                                            modifier = Modifier
                                                .padding(start = 24.dp)
                                                .fillMaxWidth(0.6f)
                                        ) {
                                            TextField(
                                                value = textFieldValueNameState.value,
                                                onValueChange = {newValue ->
                                                    textFieldValueNameState.value = newValue

                                                },
                                                keyboardOptions = KeyboardOptions(
                                                    imeAction = ImeAction.Done
                                                ),
                                                keyboardActions = KeyboardActions(
                                                    onDone = {
                                                        focusManager.clearFocus()
                                                    }
                                                ),
                                                modifier = Modifier,
                                                textStyle = TextStyle(fontSize = 16.sp)
                                            )






                                            Spinner(
                                                modifier = Modifier.padding(vertical = 8.dp),
                                                petsTypesList,
                                                selectedItemType,
                                                onItemTypeSelected = { selectedItemType = it }
                                            )



                                            TextField(
                                                value = textFieldValueHeightState.value,
                                                onValueChange = {newValue ->
                                                    textFieldValueHeightState.value = newValue

                                                },
                                                keyboardOptions = KeyboardOptions(
                                                    imeAction = ImeAction.Done
                                                ),
                                                keyboardActions = KeyboardActions(
                                                    onDone = {
                                                        focusManager.clearFocus()
                                                    }
                                                ),
                                                modifier = Modifier,
                                                textStyle = TextStyle(fontSize = 16.sp)
                                            )
                                            TextField(
                                                value = textFieldValueWeightState.value,
                                                onValueChange = {newValue ->
                                                    textFieldValueWeightState.value = newValue

                                                },
                                                keyboardOptions = KeyboardOptions(
                                                    imeAction = ImeAction.Done
                                                ),
                                                keyboardActions = KeyboardActions(
                                                    onDone = {
                                                        focusManager.clearFocus()
                                                    }
                                                ),
                                                modifier = Modifier,
                                                textStyle = TextStyle(fontSize = 16.sp)
                                            )
                                        }
                                        Column(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ){
                                            Image(
                                                Icons.Default.Delete,
                                                contentDescription = "delete",
                                                modifier = Modifier
                                                    .size(32.dp)
                                                    .offset(x = (32).dp, y = (0).dp)
                                                    .clickable {
                                                        isEditingState.value = false

                                                        runBlocking {
                                                            launch(Dispatchers.IO) {

                                                                deleteTask(pet.id)
                                                                updateTasksState()
                                                            }
                                                        }
                                                    }
                                            )
                                        }

                                    } else {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth(0.5f)
                                                .padding(start = 16.dp)
                                        ) {
                                            Text(
                                                text = petsState.value[index].name,
                                                modifier = Modifier,
                                                style = TextStyle(fontSize = 16.sp),
                                            )
                                            Text(
                                                text = petsState.value[index].type,
                                                modifier = Modifier,
                                                style = TextStyle(fontSize = 12.sp),
                                            )
                                        }
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth(0.7f)
                                                .padding(start = 8.dp)
                                        ) {
                                            Text(
                                                text = petsState.value[index].height,
                                                modifier = Modifier,
                                                style = TextStyle(fontSize = 12.sp),
                                            )
                                            Text(
                                                text = petsState.value[index].weight,
                                                modifier = Modifier,
                                                style = TextStyle(fontSize = 12.sp)
                                            )
                                        }
                                        Column(
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            // Image Edit
                                        }

                                    }


                                    Column(
                                        modifier = Modifier.weight(1f)
                                    ) {

                                    }
                                }
                            }
                        }
                    }


                    Button(
                        onClick = {
                            lifecycleScope.launch(Dispatchers.IO){
                                addNewPetFromButton()
                                updateTasksState()
                            }
                        },
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(end = 24.dp, bottom = 24.dp),
                        colors = ButtonDefaults.buttonColors(Mint90),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                    ){
                        Text(text = "Add")
                    }
                }
            }
        }
    }


    private fun updateTasksState() {
        val db = App.instance.database
        val petDao = db.petDao
        val pets = petDao.allPet

        petsState.value = pets

    }

    private fun addNewPetFromButton() {
        val db = App.instance.database
        val petDao = db.petDao

        val lastPet = petDao.allPet.lastOrNull()
        val newId = lastPet?.id?.plus(1) ?: 1

        val newPet = Pet(
            newId,
            "Type of Pet $newId",
            "Name of Pet $newId",
            "Height of Pet $newId",
            "Weight of Pet $newId"
        )
        petDao.insert(newPet)
    }

    private fun saveFromEdit(
        idItemClicked: Int,
        newName: String,
        newType: String,
        newHeight: String,
        newWeight: String
    ) {
        val db = App.instance.database
        val petDao = db.petDao
        val itemById = petDao.getById(idItemClicked)

        itemById.name = newName
        itemById.type = newType
        itemById.height = newHeight
        itemById.weight = newWeight

        petDao.update(itemById)
    }

    private fun deleteTask(idItemClicked: Int) {
        var db = App.instance.database
        var petDao = db.petDao

        val itemById = petDao.getById(idItemClicked)

        petDao.delete(itemById)

    }

    private fun searchByName(textFromSearchBar: String) {
        val db = App.instance.database
        val petDao = db.petDao
        val itemsByName = petDao.getAllByName(textFromSearchBar)

        if (itemsByName != null) {
            petsState.value = itemsByName
        }
    }

    private fun searchByType(textFromSearchBar: String) {
        val db = App.instance.database
        val petDao = db.petDao
        val itemsByType = petDao.getAllByType(textFromSearchBar)

        if (itemsByType != null) {
            petsState.value = itemsByType
        }
    }
}

@Composable
fun Spinner(
    modifier: Modifier,
    itemList: List<String>,
    selectedItemType: String,
    onItemTypeSelected: (selectedItem: String) -> Unit
){
    var tempSelectedItem = selectedItemType

    if (selectedItemType.isBlank() && itemList.isNotEmpty()) {
        onItemTypeSelected(itemList[0])
        tempSelectedItem = itemList[0]
    }

    var expandedType by rememberSaveable() { mutableStateOf(false) }

    OutlinedButton(
        onClick = { expandedType = true },
        modifier = modifier,
        enabled = itemList.isNotEmpty()
    ) {
        Text(
            text = tempSelectedItem,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            modifier = Modifier.weight(1f)
        )

        Icon(
            if (expandedType) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
            contentDescription = null
        )

        DropdownMenu(
            expanded = expandedType,
            onDismissRequest = { expandedType = false }
        ) {
            itemList.forEach {
                DropdownMenuItem(
                    text = { Text(text = it) },
                    onClick = {
                        expandedType = false
                        onItemTypeSelected(it)
                    }
                )
            }
        }
    }

}







