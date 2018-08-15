package com.abeemukthees.todo

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import com.abeemukthees.domain.action.LoadTasksAction
import com.abeemukthees.domain.action.SetVisibilityFilterAction
import com.abeemukthees.domain.common.VisibilityMode
import com.abeemukthees.domain.entities.Task
import com.abeemukthees.domain.state.*
import com.abeemukthees.domain.statemachine.State
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_chief.*
import kotlinx.android.synthetic.main.content_chief.*
import kotlinx.android.synthetic.main.item_task.view.*
import org.koin.android.architecture.ext.viewModel

class ChiefActivity : AppCompatActivity() {

    private val TAG = ChiefActivity::class.simpleName

    private val todoViewModel by viewModel<TodoViewModel>()

    private val disposables = CompositeDisposable()

    private val taskRecyclerViewAdapter by lazy { TaskRecyclerViewAdapter() }

    private var showAll = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chief)
        setSupportActionBar(toolbar)


        disposables.add(RxView.clicks(fab).doOnNext { showAll = !showAll }.map {
            SetVisibilityFilterAction(if (showAll) VisibilityMode.ALL else VisibilityMode.COMPLETED)
        }.subscribe(todoViewModel.input))

        recyclerView_tasks.adapter = taskRecyclerViewAdapter
        recyclerView_tasks.layoutManager = LinearLayoutManager(this)

        disposables.add(
                Observable.just(LoadTasksAction)
                        .subscribe(todoViewModel.input)
        )

        todoViewModel.state.observe(this, Observer {
            setupViews(it!!)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_chief, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupViews(state: State) {

        when (state) {

            is LoadingTasksState, is FilteringTasksState -> {

                progressBar.visibility = VISIBLE
            }

            is ShowAllTasksState, is ShowActiveTasksState, is ShowCompletedTasksState -> {

                progressBar.visibility = GONE


                (state as? ShowAllTasksState)?.tasks?.let {
                    taskRecyclerViewAdapter.tasks = it
                    taskRecyclerViewAdapter.notifyDataSetChanged()
                }

                (state as? ShowActiveTasksState)?.tasks?.let {
                    taskRecyclerViewAdapter.tasks = it
                    taskRecyclerViewAdapter.notifyDataSetChanged()
                }

                (state as? ShowCompletedTasksState)?.tasks?.let {
                    taskRecyclerViewAdapter.tasks = it
                    taskRecyclerViewAdapter.notifyDataSetChanged()
                }
            }
        }

    }
}

class TaskRecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val title = itemView.text_title!!

}

class TaskRecyclerViewAdapter : RecyclerView.Adapter<TaskRecyclerViewHolder>() {

    var tasks = emptyList<Task>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskRecyclerViewHolder {
        return TaskRecyclerViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false))
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    override fun onBindViewHolder(holder: TaskRecyclerViewHolder, position: Int) {
        holder.title.text = "${tasks[position].text}, : ${tasks[position].isCompleted}"
    }
}



