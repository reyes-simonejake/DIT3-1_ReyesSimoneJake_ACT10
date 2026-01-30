package com.example.crossplatformtodolistapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.*

class TaskAdapter(
    private val tasks: MutableList<Task>,
    private val onDeleteClick: (Task) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val taskText: TextView = view.findViewById(R.id.taskText)
        val taskTime: TextView = view.findViewById(R.id.taskTime)
        val deleteButton: FloatingActionButton = view.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.taskText.text = task.text
        
        // Format time
        val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        holder.taskTime.text = timeFormat.format(Date(task.id))
        
        holder.deleteButton.setOnClickListener {
            // Scale out animation
            val scaleOut = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.scale_out)
            holder.itemView.startAnimation(scaleOut)
            
            // Delay deletion for animation
            holder.itemView.postDelayed({
                onDeleteClick(task)
            }, 300)
        }
        
        // Fade and scale in animation for new items
        val fadeScaleIn = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.fade_scale_in)
        holder.itemView.startAnimation(fadeScaleIn)
    }

    override fun getItemCount() = tasks.size
}
