package com.milin.messages.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.milin.messages.R;
import com.milin.messages.model.CompleteMessage;

import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;

public class MessagesAdapter extends PagedListAdapter<CompleteMessage, MessageViewHolder> {
    private static final int VIEW_TYPE_OTHER = 0;
    public static final int VIEW_TYPE_THIS = 1;
    private MessageViewHolder.AttachmentLongPressListener attachmentLongPressListener;

    MessagesAdapter(DiffUtil.ItemCallback<CompleteMessage> diffCallback, MessageViewHolder.AttachmentLongPressListener attachmentLongPressListener) {
        super(diffCallback);
        this.attachmentLongPressListener = attachmentLongPressListener;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = viewType % 2 == VIEW_TYPE_THIS ? LayoutInflater.from(parent.getContext())
                .inflate(R.layout.this_user_message_item, parent, false) :
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.other_user_message_item, parent, false);

        addAttachmentViews(view, viewType / 2);

        return new MessageViewHolder(view, attachmentLongPressListener);
    }

    private void addAttachmentViews(View view, int count) {
        LinearLayout attachmentContainer = view.findViewById(R.id.attachmentContainer);
        for(int i=0; i < count; i++) {
            LayoutInflater.from(view.getContext()).inflate(R.layout.attachment_item, attachmentContainer, true);
        }
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        holder.bindView(getItem(position), getItemViewType(position));
    }

    @Override
    public int getItemViewType(int position) {
        CompleteMessage completeMessage = getItem(position);
        int viewType = VIEW_TYPE_OTHER;
        if(completeMessage != null) {
            viewType = completeMessage.users.get(0).id == 1 ? VIEW_TYPE_THIS : VIEW_TYPE_OTHER;
            // instruct onCreateViewHolder() to add number of attachment cards while creating view
            // viewType = 2 means VIEW_TYPE_OTHER and (2/2) = 1 attachment
            // viewType = 7 means VIEW_TYPE_THIS and (7/2) = 3 attachments
            // this way, we can prevent adding and removing views in bindView()
            viewType += 2 * completeMessage.attachments.size();
        }
        return viewType;
    }

    @Override
    public long getItemId(int position) {
        CompleteMessage completeMessage = getItem(position);
        if(completeMessage != null) {
            return completeMessage.message.id;
        }
        return 0;
    }
}
