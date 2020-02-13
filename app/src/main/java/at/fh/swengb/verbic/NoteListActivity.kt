package at.fh.swengb.verbic

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kotlinx.android.synthetic.main.activity_list.*
import java.util.*

class NoteListActivity : AppCompatActivity() {
    companion object{
        const val TOKEN = "TOKEN"
        const val LAST_SYNC = "LAST_SYNC"
        const val NOTE_ID = "NOTE_ID"
        const val EXTRA_ADDED_OR_EDITED_RESULT = 0
    }

    private val notesAdapter = NoteAdapter(){
        val intent = Intent(this, AddOrEditNoteActivity::class.java)
        intent.putExtra(NOTE_ID, it.id)
        startActivityForResult(intent, EXTRA_ADDED_OR_EDITED_RESULT)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        val sharedPreferences = getSharedPreferences(packageName, Context.MODE_PRIVATE)
        val token = sharedPreferences.getString(TOKEN, null)
        val lastSync = sharedPreferences.getLong(LAST_SYNC, 0)
        if (token != null){
            NoteRepository.getNotes(
                token,
                lastSync,
                success = {
                    it.notes.map { NoteRepository.addNote(this, it) }
                    sharedPreferences.edit().putLong(LAST_SYNC, it.lastSync).apply()
                    notesAdapter.updateList(NoteRepository.getNotesAll(this))
                },
                error = {
                    Log.e("Error", it)
                    notesAdapter.updateList(NoteRepository.getNotesAll(this))
                })
            note_recycler_view.layoutManager = StaggeredGridLayoutManager(2,1)
            note_recycler_view.adapter = notesAdapter

        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item?.itemId) {
            R.id.logout -> {
                val sharedPreferences = getSharedPreferences(packageName, Context.MODE_PRIVATE)
                sharedPreferences.edit().clear().apply()
                NoteRepository.clearDb(this)
                finish()
            true}
            R.id.newnote -> {
                val uuidString = UUID.randomUUID().toString()
                val intent = Intent(this, AddOrEditNoteActivity::class.java)
                intent.putExtra(NOTE_ID, uuidString)
                startActivityForResult(intent, EXTRA_ADDED_OR_EDITED_RESULT)
            true}
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.e("ACTIVITY_RESULT","Resulted Activity")
        if (requestCode == EXTRA_ADDED_OR_EDITED_RESULT  && resultCode == Activity.RESULT_OK){
            notesAdapter.updateList(NoteRepository.getNotesAll(this))
            note_recycler_view.layoutManager = StaggeredGridLayoutManager(2,1)
            note_recycler_view.adapter = notesAdapter
        }
    }
}
