package org.dieschnittstelle.mobile.android.skeleton;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.dieschnittstelle.mobile.android.skeleton.model.Contacts;
import org.dieschnittstelle.mobile.android.skeleton.model.ToDo;
import org.dieschnittstelle.mobile.android.skeleton.viewmodel.DetailviewViewmodelImpl;

import java.util.List;

public class ContactListDetailViewAdapter extends RecyclerView.Adapter<ContactListDetailViewAdapter.ViewHolder> {

    private Contacts[] contactArray;
    private ToDo todoItem;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            textView = (TextView) view.findViewById(R.id.contact_name);
        }

        public TextView getTextView() {
            return textView;
        }
    }

    /**
     * Initialize the dataset of the Adapter
     *
     * @param contactArray String[] containing the data to populate views to be used
     * by RecyclerView
     */
    public ContactListDetailViewAdapter(Contacts[] contactArray, ToDo todoItem) {
        this.contactArray = contactArray;
        this.todoItem = todoItem;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.contact_listitem_detailview, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getTextView().setText(contactArray[position].getName());
        ImageButton button = (ImageButton) viewHolder.itemView.findViewById(R.id.delete_contact);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String contactId = contactArray[viewHolder.getAdapterPosition()].getContactId();
                Log.i(ContactListDetailViewAdapter.class.getSimpleName(),"before delete-contact: " + String.join(",",todoItem.getContactIds()));
                Log.i(ContactListDetailViewAdapter.class.getSimpleName(),"ID to be deleted: " + contactId);
                todoItem.removeContactId(contactId);
                Log.i(ContactListDetailViewAdapter.class.getSimpleName(),"after delete-contact: " + String.join(",",todoItem.getContactIds()));
                notifyItemRemoved(viewHolder.getAdapterPosition());
                notifyItemRangeChanged(viewHolder.getAdapterPosition(), contactArray.length);
                viewHolder.itemView.setVisibility(View.GONE);

            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return contactArray.length;
    }
}


