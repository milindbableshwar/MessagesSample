package com.milin.messages.view;

import android.content.DialogInterface;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;
import com.milin.messages.R;
import com.milin.messages.model.CompleteMessage;
import com.milin.messages.viewModel.MessagesViewModel;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class MessagesActivity extends AppCompatActivity {

    private RecyclerView messageRecyclerView;

    private MessagesViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        messageRecyclerView = findViewById(R.id.messagesList);
        messageRecyclerView.setHasFixedSize(true);

        viewModel = ViewModelProviders.of(MessagesActivity.this).get(MessagesViewModel.class);

        MessagesAdapter adapter = new MessagesAdapter(MessageWithUserDiffCallback, attachmentLongPressListener);
        adapter.setHasStableIds(true);
        messageRecyclerView.setAdapter(adapter);
        viewModel.getPagedMessagesList().observe(MessagesActivity.this, adapter::submitList);
        addSwipeToDeleteListener();
    }

    private void addSwipeToDeleteListener() {
        new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                return makeMovementFlags(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                if(viewHolder instanceof MessageViewHolder) {
                    final CompleteMessage completeMessage = ((MessageViewHolder)viewHolder).message;
                    viewModel.removeMessage(((MessageViewHolder)viewHolder).message);
                    Snackbar.make(messageRecyclerView, R.string.message_deleted, Snackbar.LENGTH_LONG)
                            .setAction(R.string.undo, view -> viewModel.addMessage(completeMessage))
                            .show();
                }

            }
        }).attachToRecyclerView(messageRecyclerView);
    }

    private MessageViewHolder.AttachmentLongPressListener attachmentLongPressListener = attachment -> {
        new AlertDialog.Builder(MessagesActivity.this)
                .setTitle(getResources().getString(R.string.delete_attachment_question))
                .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        viewModel.removeAttachment(attachment);
                        Snackbar.make(messageRecyclerView, R.string.attachment_deleted, Snackbar.LENGTH_LONG)
                                .show();
                    }
                })
                .setNegativeButton(getResources().getString(R.string.no), null)
                .show();
    };

    private DiffUtil.ItemCallback<CompleteMessage> MessageWithUserDiffCallback = new DiffUtil.ItemCallback<CompleteMessage>() {
        @Override
        public boolean areItemsTheSame(CompleteMessage oldItem, CompleteMessage newItem) {
            return oldItem.message.id == newItem.message.id;
        }

        @Override
        public boolean areContentsTheSame(CompleteMessage oldItem, CompleteMessage newItem) {
            return oldItem.message.content.contentEquals(newItem.message.content) &&
                    oldItem.attachments.size() == newItem.attachments.size();
        }
    };
}
