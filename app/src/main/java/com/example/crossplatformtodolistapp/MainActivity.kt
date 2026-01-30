package com.example.crossplatformtodolistapp

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var taskInput: EditText
    private lateinit var addButton: FloatingActionButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var emptyState: LinearLayout
    private lateinit var taskCount: TextView
    private val tasks = mutableListOf<Task>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        taskInput = findViewById(R.id.taskInput)
        addButton = findViewById(R.id.addButton)
        recyclerView = findViewById(R.id.recyclerView)
        emptyState = findViewById(R.id.emptyState)
        taskCount = findViewById(R.id.taskCount)

        taskAdapter = TaskAdapter(tasks) { task ->
            deleteTask(task)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = taskAdapter

        // Animate FAB on start
        addButton.postDelayed({
            val bounceAnim = AnimationUtils.loadAnimation(this, R.anim.bounce_in)
            addButton.startAnimation(bounceAnim)
        }, 300)

        addButton.setOnClickListener {
            addTask()
        }

        taskInput.setOnEditorActionListener { _, _, _ ->
            addTask()
            true
        }

        updateEmptyState()
    }

    private fun addTask() {
        val taskText = taskInput.text.toString().trim()
        if (taskText.isEmpty()) {
            taskInput.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake))
            Toast.makeText(this, "Please enter a task 📝", Toast.LENGTH_SHORT).show()
            return
        }

        val task = Task(text = taskText)
        tasks.add(0, task) // Add to top
        taskAdapter.notifyItemInserted(0)
        taskInput.text.clear()
        recyclerView.smoothScrollToPosition(0)
        
        updateEmptyState()
        updateTaskCount()
        
        // Vibrate effect
        Toast.makeText(this, "Task added! 🎉", Toast.LENGTH_SHORT).show()
    }

    private fun deleteTask(task: Task) {
        val position = tasks.indexOf(task)
        if (position != -1) {
            tasks.removeAt(position)
            taskAdapter.notifyItemRemoved(position)
            
            updateEmptyState()
            updateTaskCount()
            
            Toast.makeText(this, "Task completed! ✓", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateEmptyState() {
        if (tasks.isEmpty()) {
            emptyState.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
            
            // Animate empty state
            val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_scale_in)
            emptyState.startAnimation(fadeIn)
        } else {
            emptyState.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }
    }

    private fun updateTaskCount() {
        val count = tasks.size
        taskCount.text = when (count) {
            0 -> "0 Active"
            1 -> "1 Active"
            else -> "$count Active"
        }
        
        // Animate count change
        val scaleAnim = AnimationUtils.loadAnimation(this, R.anim.fade_scale_in)
        taskCount.startAnimation(scaleAnim)
    }
}
