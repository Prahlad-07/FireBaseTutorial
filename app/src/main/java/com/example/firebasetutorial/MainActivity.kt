package com.example.firebasetutorial

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.firebasetutorial.ui.theme.FireBaseTutorialTheme
import com.google.firebase.database.FirebaseDatabase

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FireBaseTutorialTheme {
                RealTimeDatabase()
            }
        }
    }
}

@Composable
fun RealTimeDatabase() {
    var rollNumber by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var courseName by remember { mutableStateOf("") }
    var showMessage by remember { mutableStateOf(false) }
    var messageText by remember { mutableStateOf("") }
    val context = LocalContext.current

    val db = FirebaseDatabase.getInstance().reference.child("students")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Student Information", fontSize = 22.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = rollNumber,
            onValueChange = { rollNumber = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Enter Roll Number", fontSize = 14.sp, color = Color.Gray) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Enter Name", fontSize = 14.sp, color = Color.Gray) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = courseName,
            onValueChange = { courseName = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Enter Course", fontSize = 14.sp, color = Color.Gray) }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val buttonModifier = Modifier.weight(1f)

            // Insert
            OutlinedButton(onClick = {
                if (rollNumber.isNotEmpty() && name.isNotEmpty() && courseName.isNotEmpty()) {
                    val student = StudentsInfo(
                        rollNumber = rollNumber.toString(),
                        studentName = name,
                        courseName = courseName
                    )
                    db.child(rollNumber).setValue(student)
                        .addOnSuccessListener {
                            showMessage = true
                            messageText = "Data inserted successfully!"
                        }
                        .addOnFailureListener {
                            showMessage = true
                            messageText = "Insert failed!"
                        }
                } else {
                    Toast.makeText(context, "Please insert all values first", Toast.LENGTH_SHORT).show()
                }
            }, modifier = buttonModifier) {
                Text("Insert", color = Color.Red)
            }

            // Display (Placeholder)
            OutlinedButton(onClick = {
                Toast.makeText(context, "Display function to be implemented", Toast.LENGTH_SHORT).show()
            }, modifier = buttonModifier) {
                Text("Display", color = Color.Red)
            }

            // Update
            OutlinedButton(onClick = {
                if (rollNumber.isNotEmpty()) {
                    val updateData = mapOf<String, Any>(
                        "studentName" to name,
                        "courseName" to courseName
                    )
                    db.child(rollNumber).updateChildren(updateData)
                        .addOnSuccessListener {
                            showMessage = true
                            messageText = "Data updated successfully!"
                        }
                        .addOnFailureListener {
                            showMessage = true
                            messageText = "Update failed!"
                        }
                } else {
                    Toast.makeText(context, "Enter roll number to update", Toast.LENGTH_SHORT).show()
                }
            }, modifier = buttonModifier) {
                Text("Update", color = Color.Red)
            }

            // Delete
            OutlinedButton(onClick = {
                if (rollNumber.isNotEmpty()) {
                    db.child(rollNumber).removeValue()
                        .addOnSuccessListener {
                            showMessage = true
                            messageText = "Data deleted successfully!"
                            name = ""
                            courseName = ""
                        }
                        .addOnFailureListener {
                            showMessage = true
                            messageText = "Delete failed!"
                        }
                } else {
                    Toast.makeText(context, "Enter roll number to delete", Toast.LENGTH_SHORT).show()
                }
            }, modifier = buttonModifier) {
                Text("Delete", color = Color.Red)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        AnimatedVisibility(visible = showMessage) {
            Text(
                text = messageText,
                color = Color(0xFF4CAF50),
                fontSize = 16.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewRealTimeDatabase() {
    FireBaseTutorialTheme {
        RealTimeDatabase()
    }
}
