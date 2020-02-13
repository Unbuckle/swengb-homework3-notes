package at.fh.swengb.verbic

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_note.view.*

class NoteAdapter(private val clickListener: (note: Note) -> Unit): RecyclerView.Adapter<NoteViewHolder>() {

        private var noteList = listOf<Note>()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val noteItemView = inflater.inflate(R.layout.item_note, parent, false)
            return NoteViewHolder(noteItemView, clickListener)
        }

        override fun getItemCount(): Int {
            return noteList.count()
        }

        override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
            holder.bindItem(noteList[position])
        }


        fun updateList(newList: List<Note>) {
            noteList = newList
            notifyDataSetChanged()
        }
    }

class NoteViewHolder(itemView: View, val clickListener: (note: Note) -> Unit): RecyclerView.ViewHolder(itemView) {
    fun bindItem(note: Note) {
        itemView.item_note_title.text = note.title
        itemView.item_note_text.text = note.text


        itemView.setOnClickListener{
            clickListener(note)
        }
    }
}